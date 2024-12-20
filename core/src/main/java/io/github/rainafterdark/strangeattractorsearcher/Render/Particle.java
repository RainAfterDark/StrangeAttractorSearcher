package io.github.rainafterdark.strangeattractorsearcher.Render;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import io.github.rainafterdark.strangeattractorsearcher.Physics.Attractor;
import lombok.Getter;

public class Particle {
    public final Array<Vector3> trail = new Array<>();
    @Getter private boolean outOfBounds = false;

    public Particle(Attractor attractor) {
        Vector3 initPoint = attractor.initial();
        float offset = 0.01f;
        initPoint.x = MathUtils.random(offset, -offset);
        initPoint.y = MathUtils.random(offset, -offset);
        initPoint.z = MathUtils.random(offset, -offset);
        trail.add(initPoint);
    }

    public void update(Attractor attractor, float deltaTime, float speed, int trailLength) {
        for (int i = trail.size; i > trailLength / speed; i--) {
            trail.removeIndex(0);
        }
        if (!outOfBounds) {
            Vector3 head = trail.get(trail.size - 1);
            float dt = deltaTime * speed;
            Vector3 step = attractor.step(head, dt);
            if (step.dst(new Vector3()) > 500f)
                outOfBounds = true;
            trail.add(step);
            return;
        }
        if (!trail.isEmpty())
            trail.removeIndex(0);
    }
}
