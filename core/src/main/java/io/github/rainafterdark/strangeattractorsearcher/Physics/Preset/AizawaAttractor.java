package io.github.rainafterdark.strangeattractorsearcher.Physics.Preset;

import com.badlogic.gdx.math.Vector3;
import io.github.rainafterdark.strangeattractorsearcher.Physics.Attractor;

public class AizawaAttractor implements Attractor {
    @Override
    public Vector3 initial() {
        return new Vector3();
    }

    @Override
    public Vector3 step(Vector3 point, float deltaTime) {
        float x = point.x;
        float y = point.y;
        float z = point.z;

        float a = 0.95f;
        float b = 0.7f;
        float c = 0.6f;
        float d = 3.5f;
        float e = 0.25f;
        float f = 0.1f;
        float dt = 0.5f * deltaTime;

        float dx = ((z - b) * x - d * y);
        float dy = (d * x + (z - b) * y);
        float dz = c + a * z - (z * z * z / 3) - (x * x + y * y) * (1 + e * z) + f * z * x * x * x;

        Vector3 nP = point.cpy();
        nP.x += dx * dt;
        nP.y += dy * dt;
        nP.z += dz * dt;
        return nP;
    }
}
