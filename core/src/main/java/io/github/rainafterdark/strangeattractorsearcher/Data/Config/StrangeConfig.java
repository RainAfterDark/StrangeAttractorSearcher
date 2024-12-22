package io.github.rainafterdark.strangeattractorsearcher.Data.Config;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.github.rainafterdark.strangeattractorsearcher.Physics.AttractorType;
import io.github.rainafterdark.strangeattractorsearcher.Physics.Strange.StrangeAttractor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class StrangeConfig {
    private AttractorType attractorType = AttractorType.PresetLorenz;
    private List<StrangeAttractor> savedAttractors = new ArrayList<>();
    private int selectedAttractor = 0;
    private int maxIterations = 3000;
    private float searchRadius = 0.69f;
    private float divergenceThreshold = 1e3f;
    private float convergenceThreshold = 1e-3f;
    private float lyapunovThreshold = 1e-3f;

    public void setAttractorTypeGeneric(Enum<?> e) {
        this.attractorType = (AttractorType) e;
    }
}
