package io.github.rainafterdark.strangeattractorsearcher.Render;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import io.github.rainafterdark.strangeattractorsearcher.Data.Config.ColorConfig;
import io.github.rainafterdark.strangeattractorsearcher.Data.Config.ParticleConfig;
import io.github.rainafterdark.strangeattractorsearcher.Data.Config.StrangeConfig;
import io.github.rainafterdark.strangeattractorsearcher.Data.ConfigSingleton;
import io.github.rainafterdark.strangeattractorsearcher.Data.DebugSingleton;
import io.github.rainafterdark.strangeattractorsearcher.Physics.Attractor;
import io.github.rainafterdark.strangeattractorsearcher.Physics.Preset.AizawaAttractor;
import io.github.rainafterdark.strangeattractorsearcher.Physics.Preset.HalvorsenAttractor;
import io.github.rainafterdark.strangeattractorsearcher.Physics.Preset.LorenzAttractor;
import io.github.rainafterdark.strangeattractorsearcher.Physics.Preset.NewtonLeipnikAttractor;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

public class ParticleRenderer {
    private final ConfigSingleton config;
    private final StrangeConfig strangeConfig;
    private final ParticleConfig particleConfig;
    private final ColorConfig colorConfig;
    private final DebugSingleton debug;

    private final LorenzAttractor lorenzAttractor = new LorenzAttractor();
    private final AizawaAttractor aizawaAttractor = new AizawaAttractor();
    private final HalvorsenAttractor halvorsenAttractor = new HalvorsenAttractor();
    private final NewtonLeipnikAttractor newtonLeipnikAttractor = new NewtonLeipnikAttractor();

    private final Camera camera;
    private final ShapeRenderer shapeRenderer;
    private final PostProcessing postProcessing;
    private final List<Particle> particles = new ArrayList<>();
    private final ForkJoinPool forkJoinPool = new ForkJoinPool();

    private float stepAccumulator = 0f;
    private float respawnAccumulator = 0f;

    private class ParticleUpdateTask extends RecursiveAction {
        private static final int THRESHOLD = 50;
        private final int start;
        private final int end;
        private final float fixedPhysicsStep;

        public ParticleUpdateTask(int start, int end, float fixedPhysicsStep) {
            this.start = start;
            this.end = end;
            this.fixedPhysicsStep = fixedPhysicsStep;
        }

        @Override
        protected void compute() {
            if (end - start <= THRESHOLD) {
                for (int i = start; i < end; i++) {
                    Particle particle = particles.get(i);
                    particle.update(config.getSelectedAttractor(), fixedPhysicsStep,
                        particleConfig.getSimulationSpeed(), particleConfig.getTrailLength());
                }
            } else {
                int mid = (start + end) / 2;
                ParticleUpdateTask leftTask = new ParticleUpdateTask(start, mid, fixedPhysicsStep);
                ParticleUpdateTask rightTask = new ParticleUpdateTask(mid, end, fixedPhysicsStep);
                invokeAll(leftTask, rightTask);
            }
        }
    }

    public ParticleRenderer() {
        config = ConfigSingleton.getInstance();
        strangeConfig = config.getStrange();
        particleConfig = config.getParticle();
        colorConfig = config.getColor();
        debug = DebugSingleton.getInstance();
        camera = new Camera();
        shapeRenderer = new ShapeRenderer(10000);
        shapeRenderer.setAutoShapeType(true);
        postProcessing = new PostProcessing();
        Gdx.gl.glEnable(GL20.GL_DEPTH_TEST);
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
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) Gdx.app.exit();
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
        if (newAttractor == null || !newAttractor.equals(config.getSelectedAttractor())) {
            reset();
            if (newAttractor != null)
                newAttractor.initParams();
            config.setSelectedAttractor(newAttractor);
        }
    }

    private void spawnParticles(float deltaTime) {
        int particleCount = particleConfig.getParticleCount();
        for (int i = particles.size(); i < particleCount; i++) {
            particles.add(new Particle(config.getSelectedAttractor(),
                particleConfig, colorConfig, camera.getAutoCenterPoint()));
        }
        for (int i = particles.size(); i > particleCount; i--) {
            particles.removeFirst();
        }
        respawnAccumulator += deltaTime;
        if (respawnAccumulator >= particleConfig.getRespawnTime()) {
            particles.add(new Particle(config.getSelectedAttractor(),
                particleConfig, colorConfig, camera.getAutoCenterPoint()));
            respawnAccumulator = 0f;
        }
    }

    private void updatePhysics(float deltaTime) {
        int sumCalculations = 0;
        stepAccumulator += deltaTime * particleConfig.getSimulationSpeed();
        float fixedPhysicsStep = 1f / particleConfig.getStepResolution();
        while (stepAccumulator >= fixedPhysicsStep) {
            forkJoinPool.invoke(new ParticleUpdateTask(0, particles.size(), fixedPhysicsStep));
            sumCalculations += particles.size();
            stepAccumulator -= fixedPhysicsStep;
        }
        debug.setCalculations(sumCalculations);
    }

    public void render(float deltaTime) {
        handleInput();
        updateAttractor();

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT |
        (Gdx.graphics.getBufferFormat().coverageSampling ? GL20.GL_COVERAGE_BUFFER_BIT_NV : 0));

        if (config.getSelectedAttractor() == null)
            return;

        spawnParticles(deltaTime);
        updatePhysics(deltaTime);
        float maxDistance = 0f;
        int sumLineSegments = 0;
        Vector3 sumPosition = new Vector3();

        postProcessing.capture();
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE);
        shapeRenderer.setProjectionMatrix(camera.getCamera().combined);
        shapeRenderer.begin();

        for (Particle particle : particles) {
            if (particle.isOutOfBounds()) continue;
            for (int j = 1; j < particle.trail.size; j++) {
                ParticlePoint p1 = particle.trail.get(j - 1);
                ParticlePoint p2 = particle.trail.get(j);
                if (!Float.isFinite(p2.getDistance())) {
                    particle.setOutOfBounds(true);
                    break;
                }
                maxDistance = Math.max(p2.getDistance(), maxDistance);
                sumPosition.add(p2.getPosition());

                float velocity = p2.getVelocity();
                if (velocity > particleConfig.getMaxVelocity()) continue;
                p2.getColor().a = (float) j / particle.trail.size;
                shapeRenderer.setColor(p2.getColor());
                shapeRenderer.line(p1.getPosition(), p2.getPosition());
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
