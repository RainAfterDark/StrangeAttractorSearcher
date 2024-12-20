package io.github.rainafterdark.strangeattractorsearcher.Render;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import io.github.rainafterdark.strangeattractorsearcher.Data.ColorConfig;
import io.github.rainafterdark.strangeattractorsearcher.Data.ParticleConfig;
import io.github.rainafterdark.strangeattractorsearcher.Data.ConfigSingleton;
import io.github.rainafterdark.strangeattractorsearcher.Physics.Attractor;
import io.github.rainafterdark.strangeattractorsearcher.Physics.HalvorsenAttractor;
import io.github.rainafterdark.strangeattractorsearcher.Physics.LorenzAttractor;
import io.github.rainafterdark.strangeattractorsearcher.Physics.NewtonLeipnikAttractor;

public class ParticleRenderer {
    private ConfigSingleton config;
    private ParticleConfig particleConfig;
    private ColorConfig colorConfig;
    private Camera camera;
    private ShapeRenderer shapeRenderer;
    private Array<Particle> particles;
    private float stepAccumulator = 0f;

    public void init() {
        config = ConfigSingleton.getInstance();
        particleConfig = config.getParticle();
        colorConfig = config.getColor();
        camera = new Camera();
        camera.init();
        shapeRenderer = new ShapeRenderer();
        particles = new Array<>();
    }

    private void drawAxes() {
        // Draw axes
        float axisLength = 50f; // Length of the axes
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        // X-axis (Red)
        shapeRenderer.setColor(Color.RED.cpy().sub(0, 0, 0, 0.75f));
        shapeRenderer.line(-axisLength, 0, 0, axisLength, 0, 0);
        // Y-axis (Green)
        shapeRenderer.setColor(Color.GREEN.cpy().sub(0, 0, 0, 0.75f));
        shapeRenderer.line(0, -axisLength, 0, 0, axisLength, 0);
        // Z-axis (Blue)
        shapeRenderer.setColor(Color.SKY.cpy().sub(0, 0, 0, 0.75f));
        shapeRenderer.line(0, 0, -axisLength, 0, 0, axisLength);
        shapeRenderer.end();
    }

    private Attractor getAttractor() {
        return switch (particleConfig.getAttractorType()) {
            case Halvorsen -> new HalvorsenAttractor();
            case NewtonLeipnik -> new NewtonLeipnikAttractor();
            default -> new LorenzAttractor();
        };
    }

    public void render(float deltaTime) {
        int particleCount = particleConfig.getParticleCount();
        Attractor attractor = getAttractor();
        for (int i = particles.size; i < particleCount + 1; i++) {
            particles.add(new Particle(attractor));
        }
        for (int i = particles.size; i > particleCount; i--) {
            particles.removeIndex(0);
        }
        // Update particles
        stepAccumulator += deltaTime;
        float fixedPhysicsStep = 1f / (120f * particleConfig.getSimulationSpeed());
        while (stepAccumulator >= fixedPhysicsStep) {
            for (Particle particle : particles) {
                particle.update(attractor, fixedPhysicsStep,
                    particleConfig.getSimulationSpeed(),
                    particleConfig.getTrailLength());
            }
            stepAccumulator -= fixedPhysicsStep;
        }

        float maxDistance = 0f;
        int sumParticles = 0;
        Vector3 sumPosition = new Vector3();

        int toRemove = -1;
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        for (int i = 0; i < particles.size; i++) {
            Particle particle = particles.get(i);
            if (particle.isOutOfBounds() && particle.trail.size < 10) {
                toRemove = i;
                continue;
            }
            sumParticles += particle.trail.size;
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
        shapeRenderer.setProjectionMatrix(camera.getCombined());

        camera.setAutoCenterPoint(new Vector3(
            sumPosition.x / sumParticles,
            sumPosition.y / sumParticles,
            sumPosition.z / sumParticles));
        camera.setAutoZoom(maxDistance);
        camera.update(deltaTime);
    }

    public void dispose() {
        shapeRenderer.dispose();
    }
}
