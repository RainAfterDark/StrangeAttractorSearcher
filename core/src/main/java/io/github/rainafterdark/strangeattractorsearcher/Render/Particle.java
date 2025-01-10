package io.github.rainafterdark.strangeattractorsearcher.Render;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import io.github.rainafterdark.strangeattractorsearcher.Data.Config.ColorConfig;
import io.github.rainafterdark.strangeattractorsearcher.Data.Config.ParticleConfig;
import io.github.rainafterdark.strangeattractorsearcher.Physics.Attractor;
import lombok.Getter;
import lombok.Setter;

public class Particle {
    public final Array<ParticlePoint> trail = new Array<>();
    private final ColorConfig colorConfig;
    private final Vector3 center;
    @Getter @Setter private boolean outOfBounds = false;

    public Particle(Attractor attractor, ParticleConfig particleConfig, ColorConfig colorConfig, Vector3 center) {
        Vector3 initPoint = attractor.initial();
        float offset = particleConfig.getSpawnRadius();
        initPoint.x = MathUtils.random(offset, -offset);
        initPoint.y = MathUtils.random(offset, -offset);
        initPoint.z = MathUtils.random(offset, -offset);
        this.colorConfig = colorConfig;
        this.center = center;
        trail.add(new ParticlePoint(initPoint, initPoint, center, colorConfig));
    }

    public void update(Attractor attractor, float deltaTime, float speed, int trailLength) {
        for (int i = trail.size; i > trailLength / speed; i--) {
            trail.removeIndex(0);
        }
        if (!outOfBounds) {
            ParticlePoint head = trail.get(trail.size - 1);
            float dt = deltaTime * speed;
            Vector3 step = attractor.step(head.position, dt);
            trail.add(new ParticlePoint(step, head.position, center, colorConfig));
            return;
        }
        if (!trail.isEmpty())
            trail.removeIndex(0);
    }
}
