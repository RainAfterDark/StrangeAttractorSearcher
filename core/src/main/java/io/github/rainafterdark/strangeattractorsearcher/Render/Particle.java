package io.github.rainafterdark.strangeattractorsearcher.Render;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import io.github.rainafterdark.strangeattractorsearcher.Physics.Attractor;

public class Particle {
    public Array<Vector3> trail;

    public Particle() {
        this.trail = new Array<Vector3>();
        Vector3 initPoint = new Vector3();
        float offset = 0.1f;
        initPoint.x = MathUtils.random(offset, -offset);
        initPoint.y = MathUtils.random(offset, -offset);
        initPoint.z = MathUtils.random(offset, -offset);
        trail.add(initPoint);
    }

    public void update(Attractor attractor, float deltaTime, float speed) {
        Vector3 head = trail.get(trail.size - 1);
        float dt = deltaTime * speed;
        trail.add(attractor.step(head, dt));
        if (trail.size > 50 / speed) {
            trail.removeIndex(0);
        }
    }
}
