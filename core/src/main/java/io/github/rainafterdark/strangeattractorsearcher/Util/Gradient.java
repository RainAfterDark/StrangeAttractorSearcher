package io.github.rainafterdark.strangeattractorsearcher.Util;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class Gradient {
    private final List<Color> colors = new ArrayList<>();

    // Add a color to the gradient
    public Gradient addColor(Color color) {
        colors.add(new Color(color));
        return this;
    }

    // Generate a color based on the normalized value
    public Color getColor(float value) {
        // Clamp the input value between 0.0 and 1.0
        value = MathUtils.clamp(value, 0f, 1f);

        if (colors.isEmpty()) {
            // Default to white if no colors are defined
            return new Color(Color.WHITE);
        }

        if (colors.size() == 1) {
            // Return the single color if only one is defined
            return colors.getFirst();
        }

        // Determine the segment based on the normalized value
        float segmentLength = 1f / (colors.size() - 1);
        int startIndex = Math.max(MathUtils.floor(value / segmentLength), 0);
        int endIndex = Math.min(startIndex + 1, colors.size() - 1);

        // Interpolate between the two colors
        float t = (value % segmentLength) / segmentLength;
        return colors.get(startIndex).cpy().lerp(colors.get(endIndex), t);
    }

    private final static Color[] rainbow = {
        new Color(0.8f, 0.4f, 0.9f, 1f), // Light purple (#CC66E6)
        new Color(0.6f, 0.6f, 1.0f, 1f), // Light blue (#99CCFF)
        new Color(0.6f, 1.0f, 0.6f, 1f), // Light green (#99FF99)
        new Color(1.0f, 1.0f, 0.6f, 1f), // Light yellow (#FFFF99)
        new Color(1.0f, 0.8f, 0.6f, 1f), // Light orange (#FFCC99)
        new Color(1.0f, 0.6f, 0.6f, 1f), // Light red (#FF9999)
        Color.WHITE.cpy()};

    public static Gradient getRainbowGradient() {
        Gradient rainbowGradient = new Gradient();
        for (Color color : rainbow) {
            rainbowGradient.addColor(color);
        }
        return rainbowGradient;
    }
}

