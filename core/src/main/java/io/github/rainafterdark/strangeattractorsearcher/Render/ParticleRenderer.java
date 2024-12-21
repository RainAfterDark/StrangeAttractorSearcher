package io.github.rainafterdark.strangeattractorsearcher.Render;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import io.github.rainafterdark.strangeattractorsearcher.Data.*;
import io.github.rainafterdark.strangeattractorsearcher.Physics.*;

import static java.lang.Float.NaN;

public class ParticleRenderer {
    private StrangeConfig strangeConfig;
    private ParticleConfig particleConfig;
    private ColorConfig colorConfig;
    private DebugSingleton debug;

    private final LorenzAttractor lorenzAttractor = new LorenzAttractor();
    private final HalvorsenAttractor halvorsenAttractor = new HalvorsenAttractor();
    private final NewtonLeipnikAttractor newtonLeipnikAttractor = new NewtonLeipnikAttractor();

    private Camera camera;
    private ShapeRenderer shapeRenderer;
    private Array<Particle> particles;
    private AttractorType selectedAttractorType;
    private Attractor selectedAttractor;
    private Thread attractorSearchThread;
    private float stepAccumulator = 0f;
    private float respawnAccumulator = 0f;

    public void init() {
        ConfigSingleton config = ConfigSingleton.getInstance();
        strangeConfig = config.getStrange();
        particleConfig = config.getParticle();
        colorConfig = config.getColor();
        debug = DebugSingleton.getInstance();

        camera = new Camera();
        camera.init();

        shapeRenderer = new ShapeRenderer();
        shapeRenderer.setAutoShapeType(true);
        particles = new Array<>();
    }

    private void drawAxes() {
        float axisLength = 50f;
        shapeRenderer.begin();
        shapeRenderer.setColor(Color.RED.cpy().sub(0, 0, 0, 0.75f));
        shapeRenderer.line(-axisLength, 0, 0, axisLength, 0, 0);
        shapeRenderer.setColor(Color.GREEN.cpy().sub(0, 0, 0, 0.75f));
        shapeRenderer.line(0, -axisLength, 0, 0, axisLength, 0);
        shapeRenderer.setColor(Color.SKY.cpy().sub(0, 0, 0, 0.75f));
        shapeRenderer.line(0, 0, -axisLength, 0, 0, axisLength);
        shapeRenderer.end();
    }

    private void updateAttractor() {
        Attractor newAttractor = null;
        switch (strangeConfig.getAttractorType()) {
            case Lorenz:
                newAttractor = lorenzAttractor;
                break;
            case Halvorsen:
                newAttractor = halvorsenAttractor;
                break;
            case NewtonLeipnik:
                newAttractor = newtonLeipnikAttractor;
                break;
            case StrangeQuadratic:
            case StrangeCubic:
                if (!strangeConfig.getSavedAttractors().isEmpty()) {
                    int index = MathUtils.clamp(strangeConfig.getSelectedAttractor(),
                        0, strangeConfig.getSavedAttractors().size() - 1);
                    newAttractor = strangeConfig.getSavedAttractors().get(index);
                }
                break;
        }
        if (newAttractor == null || !newAttractor.equals(selectedAttractor)) {
            particles.clear();
            camera.reset();
            selectedAttractor = newAttractor;
        }
    }

    public void render(float deltaTime) {
        updateAttractor();
        if (selectedAttractor == null) return;

        int particleCount = particleConfig.getParticleCount();
        for (int i = particles.size; i < particleCount; i++) {
            particles.add(new Particle(selectedAttractor));
        }
        for (int i = particles.size; i > particleCount; i--) {
            particles.removeIndex(0);
        }
        respawnAccumulator += deltaTime;
        if (respawnAccumulator >= particleConfig.getRespawnTime()) {
            particles.add(new Particle(selectedAttractor));
            respawnAccumulator = 0f;
        }

        int sumCalculations = 0;
        stepAccumulator += deltaTime;
        float fixedPhysicsStep = 1f / (120f *
            Math.clamp(particleConfig.getSimulationSpeed(), 1f, 10f));
        while (stepAccumulator >= fixedPhysicsStep) {
            for (Particle particle : particles) {
                particle.update(selectedAttractor, fixedPhysicsStep,
                    particleConfig.getSimulationSpeed(),
                    particleConfig.getTrailLength());
                sumCalculations++;
            }
            stepAccumulator -= fixedPhysicsStep;
        }

        float maxDistance = 0f;
        int sumLineSegments = 0;
        Vector3 sumPosition = new Vector3();

        int toRemove = -1;
        shapeRenderer.begin();
        for (int i = 0; i < particles.size; i++) {
            Particle particle = particles.get(i);
            if (particle.trail.size > 0) {
                sumLineSegments += particle.trail.size;
                float cutoff = particleConfig.getCutoffDistance();
                Vector3 head = particle.trail.get(particle.trail.size - 1);
                if (head.dst(camera.getAutoCenterPoint()) > cutoff) {
                    particle.setOutOfBounds(true);
                    toRemove = i;
                }
            }
            if (particle.isOutOfBounds()) continue;
            for (int j = 1; j < particle.trail.size; j++) {
                Vector3 p1 = particle.trail.get(j - 1);
                Vector3 p2 = particle.trail.get(j);
                maxDistance = Math.max(p2.dst(camera.getAutoCenterPoint()), maxDistance);
                sumPosition.add(p2);

                float velocity = p2.dst(p1);
                float minVelocity = 0f;
                float maxVelocity = colorConfig.getGradientScaling();
                float normalizedVelocity = MathUtils.clamp((velocity - minVelocity) / (maxVelocity - minVelocity), 0f, 1f);
                Color trailColor = colorConfig.getGradientColor().getColor(normalizedVelocity);
                trailColor.a = (float) j / particle.trail.size;

                shapeRenderer.setColor(trailColor);
                shapeRenderer.line(p1, p2);
            }
        }
        if (toRemove != -1)
            particles.removeIndex(toRemove);

        shapeRenderer.end();
        shapeRenderer.setProjectionMatrix(camera.getCamera().combined);

        if(Float.isFinite(sumPosition.x) && Float.isFinite(sumPosition.y) && Float.isFinite(sumPosition.z)) {
            Vector3 centerPoint = new Vector3(
                sumPosition.x / sumLineSegments,
                sumPosition.y / sumLineSegments,
                sumPosition.z / sumLineSegments);
            if (centerPoint.dst(Vector3.Zero) < particleConfig.getCutoffDistance())
                camera.setAutoCenterPoint(centerPoint);
        }
        if (Float.isFinite(maxDistance))
            camera.setAutoZoom(Math.min(maxDistance, particleConfig.getCutoffDistance() / 2));
        camera.update(deltaTime);

        debug.setCalculations(sumCalculations);
        debug.setLineSegments(sumLineSegments);
    }

    public void dispose() {
        shapeRenderer.dispose();
    }
}
