package io.github.rainafterdark.strangeattractorsearcher.Util;

import com.badlogic.gdx.graphics.Color;
import lombok.Getter;

public class ColorHelper {
    public static Color HSVtoRGB(float hue, float saturation, float value, float alpha) {
        int h = (int) (hue * 6);
        float f = hue * 6 - h;
        float p = value * (1 - saturation);
        float q = value * (1 - f * saturation);
        float t = value * (1 - (1 - f) * saturation);
        switch (h) {
            case 0:
                return new Color(value, t, p, alpha);
            case 1:
                return new Color(q, value, p, alpha);
            case 2:
                return new Color(p, value, t, alpha);
            case 3:
                return new Color(p, q, value, alpha);
            case 4:
                return new Color(t, p, value, alpha);
            case 5:
                return new Color(value, p, q, alpha);
            default:
                return new Color(0, 0, 0, alpha);
        }
    }

    @Getter
    private final static Color[] rainbow = {
        HSVtoRGB(0f, 0.5f, 1f, 1f),
        HSVtoRGB(1f / 6f, 0.5f, 1f, 1f),
        HSVtoRGB(2f / 6f, 0.5f, 1f, 1f),
        HSVtoRGB(3f / 6f, 0.5f, 1f, 1f),
        HSVtoRGB(4f / 6f, 0.5f, 1f, 1f),
        HSVtoRGB(5f / 6f, 0.5f, 1f, 1f),
    };

    public static Color getRandom() {
        return HSVtoRGB((float) Math.random(), 0.5f, 1f, 1f);
    }
}
