package io.github.rainafterdark.strangeattractorsearcher.Render;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Matrix4;

public class Camera {
    private PerspectiveCamera camera;
    private float cameraRadius = 100f; // Distance from the origin
    private float theta = 0f;        // Horizontal angle (radians)
    private float basePhi = (float) Math.PI / 2; // Base angle (horizontal plane)
    private float phiAmplitude = (float) Math.PI / 4; // 45 degrees oscillation above/below
    private float oscillationSpeed = 0.1f; // Speed of oscillation
    private float time = 0f; // Time tracker for oscillation

    public void init() {
        camera = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.position.set(0f, 0f, cameraRadius); // Adjust as needed
        camera.lookAt(0f, 0f, 0f);
        camera.far = 300f;
        camera.update();
    }

    public void update(float deltaTime) {
        // Increment angles
        time += deltaTime;
        theta += deltaTime * 0.25f; // Horizontal rotation speed
         // Calculate the oscillating polar angle
        float phi = basePhi + phiAmplitude * MathUtils.sin(oscillationSpeed * time);

        // Clamp phi to avoid flipping at poles (optional)
        phi = MathUtils.clamp(phi, 0.1f, (float) Math.PI - 0.1f);

        // Update camera position using spherical coordinates
        float x = cameraRadius * MathUtils.sin(phi) * MathUtils.cos(theta);
        float y = cameraRadius * MathUtils.cos(phi);
        float z = cameraRadius * MathUtils.sin(phi) * MathUtils.sin(theta);

        camera.position.set(x, y, z);
        camera.lookAt(0, 0, 0); // Always look at the origin
        camera.up.set(0, 1, 0); // Ensure the "up" direction is correct
        camera.update();
    }

    public Matrix4 getCombined() {
        return camera.combined;
    }
}
