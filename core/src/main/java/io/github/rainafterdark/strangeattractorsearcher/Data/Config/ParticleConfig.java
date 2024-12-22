package io.github.rainafterdark.strangeattractorsearcher.Data.Config;

import lombok.Data;

@Data
public class ParticleConfig {
    private float simulationSpeed = 1f;
    private float maxVelocity = 5f;
    private float spawnRadius = 1f;
    private float cutoffDistance = 100f;
    private float respawnTime = 0.5f;
    private int particleCount = 100;
    private int trailLength = 200;
    private int stepResolution = 120;
}
