package io.github.rainafterdark.strangeattractorsearcher.Physics;

import com.badlogic.gdx.math.Vector3;

public class HalvorsenAttractor implements Attractor {
    @Override
    public Vector3 step(Vector3 p, float deltaTime) {
        float a = 1.4f;
        float dt = 0.5f * deltaTime;

        float dx = (-a * p.x) - (4 * p.y) - (4 * p.z) - (p.y * p.y);
        float dy = (-a * p.y) - (4 * p.z) - (4 * p.x) - (p.z * p.z);
        float dz = (-a * p.z) - (4 * p.x) - (4 * p.y) - (p.x * p.x);

        Vector3 nP = p.cpy();
        nP.x += dx * dt;
        nP.y += dy * dt;
        nP.z += dz * dt;
        return nP;
    }
}
