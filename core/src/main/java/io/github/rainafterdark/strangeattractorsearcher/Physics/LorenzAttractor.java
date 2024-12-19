package io.github.rainafterdark.strangeattractorsearcher.Physics;

import com.badlogic.gdx.math.Vector3;

public class LorenzAttractor implements Attractor {
    public Vector3 step(Vector3 p, float deltaTime) {
        float sigma = 10.0f;
        float rho = 28.0f;
        float beta = 8.0f / 3.0f;
        float dt = 0.5f * deltaTime;

        float dx = sigma * (p.y - p.x);
        float dy = p.x * (rho - p.z) - p.y;
        float dz = p.x * p.y - beta * p.z;

        Vector3 nP = p.cpy();
        nP.x += dx * dt;
        nP.y += dy * dt;
        nP.z += dz * dt;
        return nP;
    }
}
