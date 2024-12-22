package io.github.rainafterdark.strangeattractorsearcher.Render;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import io.github.rainafterdark.strangeattractorsearcher.Data.Config.CameraConfig;
import io.github.rainafterdark.strangeattractorsearcher.Data.ConfigSingleton;
import io.github.rainafterdark.strangeattractorsearcher.Data.DebugSingleton;
import io.github.rainafterdark.strangeattractorsearcher.Data.Config.ParticleConfig;
import lombok.Getter;

public class Camera {
    private CameraConfig cameraConfig;
    private ParticleConfig particleConfig;
    private DebugSingleton debug;

    @Getter PerspectiveCamera camera;
    @Getter private final Vector3 autoCenterPoint = new Vector3();
    @Getter float autoZoom = 0f;
    private float timeAccumulator = 0f;

    public void init() {
        ConfigSingleton config = ConfigSingleton.getInstance();
        cameraConfig = config.getCamera();
        particleConfig = config.getParticle();
        debug = DebugSingleton.getInstance();

        camera = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.position.set(0f, 0f, autoZoom);
        camera.lookAt(autoCenterPoint);
        camera.near = 0.01f;
        camera.far = 1000f;
        camera.update();
    }

    public void update(float deltaTime) {
        timeAccumulator += deltaTime;
        float theta = cameraConfig.getHorizontalMovementSpeed() * timeAccumulator;
        float vertical = cameraConfig.getVerticalMovementSpeed() * timeAccumulator;
        float basePhi = (float) Math.PI / 2;
        float phiAmplitude = (float) Math.PI / 4;
        float phi = basePhi - phiAmplitude * MathUtils.sin(vertical);
        phi = MathUtils.clamp(phi, 0.01f, (float) Math.PI - 0.1f);

        float radius = MathUtils.clamp(autoZoom, 0.1f, 100f);
        float x = radius * MathUtils.sin(phi) * MathUtils.cos(theta);
        float y = radius * MathUtils.cos(phi);
        float z = radius * MathUtils.sin(phi) * MathUtils.sin(theta);
        Vector3 position = autoCenterPoint.cpy().add(x, y, z);

        camera.position.set(position);
        camera.lookAt(autoCenterPoint);
        camera.up.set(0, 1, 0);
        camera.update();

        debug.setAutoCenterPoint(autoCenterPoint);
        debug.setAutoZoom(autoZoom);
    }

    public void setAutoCenterPoint(Vector3 autoCenterPoint) {
        this.autoCenterPoint.lerp(autoCenterPoint, 0.01f / particleConfig.getSimulationSpeed());
    }

    public void setAutoZoom(float autoZoom) {
        this.autoZoom = MathUtils.lerp(this.autoZoom, autoZoom, 0.01f / particleConfig.getSimulationSpeed());
    }

    public void reset() {
        autoCenterPoint.set(Vector3.Zero);
        autoZoom = 0f;
    }
}
