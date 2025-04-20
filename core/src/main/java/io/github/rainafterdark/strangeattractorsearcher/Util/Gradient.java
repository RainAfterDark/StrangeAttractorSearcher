package io.github.rainafterdark.strangeattractorsearcher.Util;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class Gradient {
    private final List<Color> colors = new ArrayList<>();

    public Color getColor(float value) {
        value = MathUtils.clamp(value, 0f, 1f);

        if (colors.isEmpty()) {
            return new Color(Color.WHITE);
        }

        if (colors.size() == 1) {
            return colors.getFirst();
        }

        float segmentLength = 1f / (colors.size() - 1);
        int startIndex = Math.max(MathUtils.floor(value / segmentLength), 0);
        int endIndex = Math.min(startIndex + 1, colors.size() - 1);
        float t = (value % segmentLength) / segmentLength;
        return colors.get(startIndex).cpy().lerp(colors.get(endIndex), t);
    }

    public static Gradient getRainbowGradient() {
        Gradient rainbowGradient = new Gradient();
        for (Color color : ColorHelper.getRainbow()) {
            rainbowGradient.getColors().add(color);
        }
        return rainbowGradient;
    }
}

