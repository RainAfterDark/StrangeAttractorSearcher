package io.github.rainafterdark.strangeattractorsearcher.Physics;

import com.badlogic.gdx.math.Vector3;

public interface Attractor {
    Vector3 initial();
    Vector3 step(Vector3 point, float deltaTime);
    void initParams();
    void renderParams();
}
