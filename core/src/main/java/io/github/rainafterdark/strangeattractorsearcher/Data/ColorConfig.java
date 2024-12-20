package io.github.rainafterdark.strangeattractorsearcher.Data;

import io.github.rainafterdark.strangeattractorsearcher.Util.Gradient;
import lombok.Data;

@Data
public class ColorConfig {
    private float gradientScaling = 1f;
    private Gradient gradientColor = Gradient.getRainbowGradient();
}
