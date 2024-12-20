package io.github.rainafterdark.strangeattractorsearcher.Data;

import io.github.rainafterdark.strangeattractorsearcher.Physics.AttractorType;
import lombok.Data;

@Data
public class ParticleConfig {
    private float simulationSpeed = 1f;
    private int particleCount = 100;
    private int trailLength = 200;
    private AttractorType attractorType = AttractorType.Lorenz;

    public void setAttractorTypeGeneric(Enum<?> e) {
        this.attractorType = (AttractorType) e;
    }
}
