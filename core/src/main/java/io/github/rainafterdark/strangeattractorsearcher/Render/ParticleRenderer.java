package io.github.rainafterdark.strangeattractorsearcher.Render;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import io.github.rainafterdark.strangeattractorsearcher.Physics.Attractor;
import io.github.rainafterdark.strangeattractorsearcher.Physics.LorenzAttractor;

public class ParticleRenderer {
    private Camera camera;
    private ShapeRenderer shapeRenderer;
    private Array<Particle> particles;
    private Attractor attractor;

    public void init() {
        camera = new Camera();
        camera.init();
        shapeRenderer = new ShapeRenderer();
        particles = new Array<Particle>();
        int numParticles = 50;
        for (int i = 0; i < numParticles; i++) {
            particles.add(new Particle());
        }
        attractor = new LorenzAttractor();
    }

    public void render(float deltaTime) {
        camera.update(deltaTime);
        shapeRenderer.setProjectionMatrix(camera.getCombined());

        // Update particles
        for (Particle particle : particles) {
            particle.update(attractor, deltaTime, 1);
        }

        // Draw axes
        float axisLength = 50f; // Length of the axes
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);

        // X-axis (Red)
        shapeRenderer.setColor(Color.RED);
        shapeRenderer.line(-axisLength, 0, 0, axisLength, 0, 0);

        // Y-axis (Green)
        shapeRenderer.setColor(Color.GREEN);
        shapeRenderer.line(0, -axisLength, 0, 0, axisLength, 0);

        // Z-axis (Blue)
        shapeRenderer.setColor(Color.SKY);
        shapeRenderer.line(0, 0, -axisLength, 0, 0, axisLength);

        shapeRenderer.end();

        // Draw trails
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        for (Particle particle : particles) {
            for (int i = 1; i < particle.trail.size; i++) {
                Vector3 p1 = particle.trail.get(i - 1);
                Vector3 p2 = particle.trail.get(i);

                // Optional: Color gradient based on index or depth
                float alpha = (float) i / particle.trail.size; // Fade trail
                shapeRenderer.setColor(1.0f, 1.0f, 1.0f, alpha);
                shapeRenderer.line(p1, p2);
            }
        }
        shapeRenderer.end();
    }

    public void dispose() {
        shapeRenderer.dispose();
    }
}
