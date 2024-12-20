package io.github.rainafterdark.strangeattractorsearcher.Render;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import io.github.rainafterdark.strangeattractorsearcher.Data.CameraConfig;
import io.github.rainafterdark.strangeattractorsearcher.Data.ConfigSingleton;
import lombok.Getter;

public class Camera {
    private CameraConfig config;
    private PerspectiveCamera camera;
    @Getter private final Vector3 autoCenterPoint = new Vector3();
    private float autoZoom = 0f;
    private float basePhi = (float) Math.PI / 2; // Base angle (horizontal plane)
    private float phiAmplitude = (float) Math.PI / 4; // 45 degrees oscillation above/below
    private float oscillationSpeed = 0.1f; // Speed of oscillation
    private float time = 0f; // Time tracker for oscillation

    public void init() {
        config = ConfigSingleton.getInstance().getCamera();
        camera = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.position.set(0f, 0f, autoZoom);
        camera.lookAt(autoCenterPoint);
        camera.near = 0.01f;
        camera.far = 1000f;
        camera.update();
    }

    public void update(float deltaTime) {
        // Increment angles
        time += deltaTime;
        float theta = time * 0.25f; // Horizontal rotation speed
         // Calculate the oscillating polar angle
        float phi = basePhi - phiAmplitude * MathUtils.sin(oscillationSpeed * time);

        // Clamp phi to avoid flipping at poles (optional)
        phi = MathUtils.clamp(phi, 0.01f, (float) Math.PI - 0.1f);

        // Update camera position using spherical coordinates
        float radius = MathUtils.clamp(autoZoom, 0.1f, 100f);
        //float radius = 1f;
        float x = radius * MathUtils.sin(phi) * MathUtils.cos(theta);
        float y = radius * MathUtils.cos(phi);
        float z = radius * MathUtils.sin(phi) * MathUtils.sin(theta);
        Vector3 position = autoCenterPoint.cpy().add(x, y, z);

        camera.position.set(position);
        camera.lookAt(autoCenterPoint); // Always look at the origin
        camera.up.set(0, 1, 0); // Ensure the "up" direction is correct
        camera.update();
    }

    public void setAutoCenterPoint(Vector3 autoCenterPoint) {
        this.autoCenterPoint.lerp(autoCenterPoint, 0.01f);
    }

    public void setAutoZoom(float autoZoom) {
        this.autoZoom = MathUtils.lerp(this.autoZoom, autoZoom, 0.01f);
    }

    public Matrix4 getCombined() {
        return camera.combined;
    }
}
