package io.github.rainafterdark.strangeattractorsearcher.Render;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import io.github.rainafterdark.strangeattractorsearcher.Config;
import io.github.rainafterdark.strangeattractorsearcher.Physics.Attractor;
import io.github.rainafterdark.strangeattractorsearcher.Physics.HalvorsenAttractor;
import io.github.rainafterdark.strangeattractorsearcher.Physics.LorenzAttractor;
import io.github.rainafterdark.strangeattractorsearcher.Util.Gradient;

public class ParticleRenderer {
    private Config config;
    private Camera camera;
    private ShapeRenderer shapeRenderer;
    private Array<Particle> particles;
    private Attractor attractor;
    private float stepAccumulator = 0f;

    public void init() {
        config = Config.getInstance();
        camera = new Camera();
        camera.init();
        shapeRenderer = new ShapeRenderer();
        particles = new Array<>();
        attractor = new LorenzAttractor();
    }

    public void render(float deltaTime) {
        for (int i = particles.size; i < config.getParticleAmount(); i++) {
            particles.add(new Particle());
        }
        for (int i = particles.size; i > config.getParticleAmount(); i--) {
            particles.removeIndex(0);
        }
        // Update particles
        stepAccumulator += deltaTime;
        float fixedPhysicsStep = 1f / 100f;
        while (stepAccumulator >= fixedPhysicsStep) {
            for (Particle particle : particles) {
                particle.update(attractor, fixedPhysicsStep,
                    config.getSimulationSpeed(), config.getTrailLength());
            }
            stepAccumulator -= fixedPhysicsStep;
        }

        // Draw axes
        float axisLength = 50f; // Length of the axes
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);

        // X-axis (Red)
        shapeRenderer.setColor(Color.RED.cpy().sub(0, 0, 0, 0.75f));
        //shapeRenderer.line(-axisLength, 0, 0, axisLength, 0, 0);

        // Y-axis (Green)
        shapeRenderer.setColor(Color.GREEN.cpy().sub(0, 0, 0, 0.75f));
        //shapeRenderer.line(0, -axisLength, 0, 0, axisLength, 0);

        // Z-axis (Blue)
        shapeRenderer.setColor(Color.SKY.cpy().sub(0, 0, 0, 0.75f));
        //shapeRenderer.line(0, 0, -axisLength, 0, 0, axisLength);

        shapeRenderer.end();

        Gradient rainbowGradient = new Gradient()
            .addColor(new Color(0.8f, 0.4f, 0.9f, 1f))  // Light purple (#CC66E6)
            .addColor(new Color(0.6f, 0.6f, 1.0f, 1f))  // Light blue (#99CCFF)
            .addColor(new Color(0.6f, 1.0f, 0.6f, 1f))  // Light green (#99FF99)
            .addColor(new Color(1.0f, 1.0f, 0.6f, 1f))  // Light yellow (#FFFF99)
            .addColor(new Color(1.0f, 0.8f, 0.6f, 1f))  // Light orange (#FFCC99)
            .addColor(new Color(1.0f, 0.6f, 0.6f, 1f)); // Light red (#FF9999)

        // Draw trails
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        for (Particle particle : particles) {
            for (int i = 1; i < particle.trail.size; i++) {
                Vector3 p1 = particle.trail.get(i - 1);
                Vector3 p2 = particle.trail.get(i);

                // Calculate velocity magnitude
                float velocityMagnitude = p2.dst(p1);

                // Normalize velocity
                float minVelocity = 0f;
                float maxVelocity = config.getSimulationSpeed();
                float normalizedVelocity = MathUtils.clamp((velocityMagnitude - minVelocity) / (maxVelocity - minVelocity), 0f, 1f);

                // Map velocity to color
                Color trailColor = rainbowGradient.getColor(normalizedVelocity);
                // Fade trail
                trailColor.a = (float) i / particle.trail.size;
                shapeRenderer.setColor(trailColor);
                shapeRenderer.line(p1, p2);
                //shapeRenderer.point(p1.x, p1.y, p1.z);
            }
        }
        shapeRenderer.end();

        camera.update(deltaTime);
        shapeRenderer.setProjectionMatrix(camera.getCombined());
    }

    public void dispose() {
        shapeRenderer.dispose();
    }
}
