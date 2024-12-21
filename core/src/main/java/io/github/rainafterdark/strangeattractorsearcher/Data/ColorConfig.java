package io.github.rainafterdark.strangeattractorsearcher.Data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.github.rainafterdark.strangeattractorsearcher.Util.Gradient;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class ColorConfig {
    private float gradientScaling = 1f;
    private Gradient gradientColor = Gradient.getRainbowGradient();
}
