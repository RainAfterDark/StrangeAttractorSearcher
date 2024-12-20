package io.github.rainafterdark.strangeattractorsearcher.Physics;

import com.badlogic.gdx.math.Vector3;

public class NewtonLeipnikAttractor implements Attractor {
    @Override
    public Vector3 initial() {
        return new Vector3();
    }

    @Override
    public Vector3 step(Vector3 point, float deltaTime) {
        float x = point.x;
        float y = point.y;
        float z = point.z;

        float a = 0.4f;
        float b = 0.175f;
        float dt = 0.75f * deltaTime;

        float dx = -(a * x) + y + (10 * y * z);
        float dy = -x - (0.4f * y) + (5f * x * z);
        float dz = (b * z) - (5 * x * y);

        Vector3 nP = point.cpy();
        nP.x += dx * dt;
        nP.y += dy * dt;
        nP.z += dz * dt;
        return nP;
    }
}
