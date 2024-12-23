package io.github.rainafterdark.strangeattractorsearcher.Render;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import io.github.rainafterdark.strangeattractorsearcher.Data.*;
import io.github.rainafterdark.strangeattractorsearcher.Data.Config.ColorConfig;
import io.github.rainafterdark.strangeattractorsearcher.Data.Config.ParticleConfig;
import io.github.rainafterdark.strangeattractorsearcher.Data.Config.StrangeConfig;
import io.github.rainafterdark.strangeattractorsearcher.Physics.*;
import io.github.rainafterdark.strangeattractorsearcher.Physics.Preset.AizawaAttractor;
import io.github.rainafterdark.strangeattractorsearcher.Physics.Preset.HalvorsenAttractor;
import io.github.rainafterdark.strangeattractorsearcher.Physics.Preset.LorenzAttractor;
import io.github.rainafterdark.strangeattractorsearcher.Physics.Preset.NewtonLeipnikAttractor;

import java.util.ArrayList;
import java.util.List;

public class ParticleRenderer {
    private StrangeConfig strangeConfig;
    private ParticleConfig particleConfig;
    private ColorConfig colorConfig;
    private DebugSingleton debug;

    private final LorenzAttractor lorenzAttractor = new LorenzAttractor();
    private final AizawaAttractor aizawaAttractor = new AizawaAttractor();
    private final HalvorsenAttractor halvorsenAttractor = new HalvorsenAttractor();
    private final NewtonLeipnikAttractor newtonLeipnikAttractor = new NewtonLeipnikAttractor();

    private Camera camera;
    private ShapeRenderer shapeRenderer;
    private PostProcessing postProcessing;

    private List<Particle> particles;
    private Attractor selectedAttractor;
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
        shapeRenderer = new ShapeRenderer(10000);
        shapeRenderer.setAutoShapeType(true);
        postProcessing = new PostProcessing();
        postProcessing.init();
        Gdx.gl.glEnable(GL20.GL_DEPTH_TEST);
        particles = new ArrayList<>();
    }

    public void resume() {
        postProcessing.rebind();
    }

    private void reset() {
        particles.clear();
        camera.reset();
    }

    private void previousAttractor() {
        int index = strangeConfig.getSelectedAttractor() - 1;
        if (index < 0) {
            index = strangeConfig.getSavedAttractors().size() - 1;
        }
        strangeConfig.setSelectedAttractor(index);
    }

    private void nextAttractor() {
        int index = strangeConfig.getSelectedAttractor() + 1;
        if (index >= strangeConfig.getSavedAttractors().size()) {
            index = 0;
        }
        strangeConfig.setSelectedAttractor(index);
    }

    private void handleInput() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.R)) reset();
        if (Gdx.input.isKeyJustPressed(Input.Keys.LEFT)) previousAttractor();
        if (Gdx.input.isKeyJustPressed(Input.Keys.RIGHT)) nextAttractor();
    }

    private void drawAxes() {
        float axisLength = particleConfig.getCutoffDistance() * 2f;
        shapeRenderer.begin();
        shapeRenderer.setColor(Color.RED);
        shapeRenderer.line(-axisLength, 0, 0, axisLength, 0, 0);
        shapeRenderer.setColor(Color.GREEN);
        shapeRenderer.line(0, -axisLength, 0, 0, axisLength, 0);
        shapeRenderer.setColor(Color.SKY);
        shapeRenderer.line(0, 0, -axisLength, 0, 0, axisLength);
        shapeRenderer.end();
    }

    private void updateAttractor() {
        Attractor newAttractor = null;
        switch (strangeConfig.getAttractorType()) {
            case PresetLorenz:
                newAttractor = lorenzAttractor;
                break;
            case PresetAizawa:
                newAttractor = aizawaAttractor;
                break;
            case PresetHalvorsen:
                newAttractor = halvorsenAttractor;
                break;
            case PresetNewtonLeipnik:
                newAttractor = newtonLeipnikAttractor;
                break;
            default:
                if (!strangeConfig.getSavedAttractors().isEmpty()) {
                    int index = MathUtils.clamp(strangeConfig.getSelectedAttractor(),
                        0, strangeConfig.getSavedAttractors().size() - 1);
                    newAttractor = strangeConfig.getSavedAttractors().get(index);
                }
                break;
        }
        if (newAttractor == null || !newAttractor.equals(selectedAttractor)) {
            reset();
            selectedAttractor = newAttractor;
        }
    }

    private void spawnParticles(float deltaTime) {
        int particleCount = particleConfig.getParticleCount();
        for (int i = particles.size(); i < particleCount; i++) {
            particles.add(new Particle(selectedAttractor));
        }
        for (int i = particles.size(); i > particleCount; i--) {
            particles.remove(0);
        }
        respawnAccumulator += deltaTime;
        if (respawnAccumulator >= particleConfig.getRespawnTime()) {
            particles.add(new Particle(selectedAttractor));
            respawnAccumulator = 0f;
        }
    }

    private void updatePhysics(float deltaTime) {
        int sumCalculations = 0;
        stepAccumulator += deltaTime;
        float fixedPhysicsStep = 1f / (particleConfig.getStepResolution() *
            MathUtils.clamp(particleConfig.getSimulationSpeed(), 1f, 10f));
        while (stepAccumulator >= fixedPhysicsStep) {
            for (Particle particle : particles) {
                particle.update(selectedAttractor, fixedPhysicsStep,
                    particleConfig.getSimulationSpeed(),
                    particleConfig.getTrailLength());
                sumCalculations++;
            }
            stepAccumulator -= fixedPhysicsStep;
        }
        debug.setCalculations(sumCalculations);
    }

    public void render(float deltaTime) {
        handleInput();
        updateAttractor();
        if (selectedAttractor == null) return;
        spawnParticles(deltaTime);
        updatePhysics(deltaTime);

        float maxDistance = 0f;
        int sumLineSegments = 0;
        Vector3 sumPosition = new Vector3();

        postProcessing.capture();
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT |
        (Gdx.graphics.getBufferFormat().coverageSampling ? GL20.GL_COVERAGE_BUFFER_BIT_NV : 0));
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE);
        shapeRenderer.setProjectionMatrix(camera.getCamera().combined);
        shapeRenderer.begin();

        for (Particle particle : particles) {
            if (particle.isOutOfBounds()) continue;
            for (int j = 1; j < particle.trail.size; j++) {
                Vector3 p1 = particle.trail.get(j - 1);
                Vector3 p2 = particle.trail.get(j);
                float centerDistance = p2.dst(camera.getAutoCenterPoint());
                if (!Float.isFinite(centerDistance)) {
                    particle.setOutOfBounds(true);
                    break;
                }
                maxDistance = Math.max(centerDistance, maxDistance);
                sumPosition.add(p2);

                float velocity = p2.dst(p1);
                if (velocity > particleConfig.getMaxVelocity()) continue;
                float minVelocity = 0f;
                float maxVelocity = colorConfig.getGradientScaling();
                float normalizedVelocity = MathUtils.clamp((velocity - minVelocity) / (maxVelocity - minVelocity), 0f, 1f);
                Color trailColor = colorConfig.getGradientColor().getColor(normalizedVelocity);
                trailColor.a = (float) j / particle.trail.size;

                shapeRenderer.setColor(trailColor);
                shapeRenderer.line(p1, p2);
                sumLineSegments++;
            }
        }

        particles.removeIf(Particle::isOutOfBounds);
        shapeRenderer.end();
        Gdx.gl.glDisable(GL20.GL_BLEND);
        postProcessing.render();

        float halfDistance = particleConfig.getCutoffDistance() / 2f;
        Vector3 centerPoint = new Vector3(
                sumPosition.x / sumLineSegments,
                sumPosition.y / sumLineSegments,
                sumPosition.z / sumLineSegments);
        if (centerPoint.dst(Vector3.Zero) < particleConfig.getCutoffDistance())
            camera.setAutoCenterPoint(centerPoint);
        camera.setAutoZoom(MathUtils.clamp(maxDistance, 0f, halfDistance));
        camera.update(deltaTime);

        if (debug.getDrawAxes().get()) drawAxes();
        debug.setLineSegments(sumLineSegments);
    }

    public void dispose() {
        shapeRenderer.dispose();
        postProcessing.dispose();
    }
}
