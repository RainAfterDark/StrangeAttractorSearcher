package io.github.rainafterdark.strangeattractorsearcher.Data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.github.rainafterdark.strangeattractorsearcher.Physics.AttractorType;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class ParticleConfig {
    private float simulationSpeed = 1f;
    private float spawnRadius = 1f;
    private float cutoffDistance = 100f;
    private float respawnTime = 0.5f;
    private int particleCount = 100;
    private int trailLength = 200;
}
