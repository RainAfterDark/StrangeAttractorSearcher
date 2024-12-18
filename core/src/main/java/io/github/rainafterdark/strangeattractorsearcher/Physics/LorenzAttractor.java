package io.github.rainafterdark.strangeattractorsearcher.Physics;

import com.badlogic.gdx.math.Vector3;

public class LorenzAttractor implements Attractor {
    public Vector3 step(Vector3 point, float deltaTime) {
        float sigma = 10.0f;
        float rho = 28.0f;
        float beta = 8.0f / 3.0f;
        float dt = 0.5f * deltaTime;

        float dx = sigma * (point.y - point.x);
        float dy = point.x * (rho - point.z) - point.y;
        float dz = point.x * point.y - beta * point.z;

        Vector3 newPoint = point.cpy();
        newPoint.x += dx * dt;
        newPoint.y += dy * dt;
        newPoint.z += dz * dt;
        return newPoint;
    }
}
