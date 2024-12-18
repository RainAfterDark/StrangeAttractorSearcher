package io.github.rainafterdark.strangeattractorsearcher.Physics;

import com.badlogic.gdx.math.Vector3;

public interface Attractor {
    public Vector3 step(Vector3 point, float deltaTime);
}
