package io.github.rainafterdark.strangeattractorsearcher.Physics;

import com.badlogic.gdx.math.Vector3;

public class QuadraticMapAttractor implements Attractor {
    @Override
    public Vector3 initial() {
        return null;
    }

    @Override
    public Vector3 step(Vector3 point, float deltaTime) {
        return new Vector3();
    }
}
