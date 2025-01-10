package io.github.rainafterdark.strangeattractorsearcher.Render;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import io.github.rainafterdark.strangeattractorsearcher.Data.Config.ColorConfig;
import lombok.Getter;

@Getter
public class ParticlePoint {
    private final Vector3 position;
    private final float velocity;
    private final float distance;
    private final Color color;

    public ParticlePoint(Vector3 position, Vector3 previousPosition, Vector3 centerPosition, ColorConfig colorConfig) {
        this.position = position;
        this.velocity = position.dst(previousPosition);
        this.distance = position.dst(centerPosition);
        float minVelocity = 0f;
        float maxVelocity = colorConfig.getGradientScaling();
        float normalizedVelocity = MathUtils.clamp((velocity - minVelocity) / (maxVelocity - minVelocity), 0f, 1f);
        this.color = colorConfig.getGradientColor().getColor(normalizedVelocity);
    }
}
