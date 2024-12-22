package io.github.rainafterdark.strangeattractorsearcher.Physics;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum AttractorType {
    @JsonProperty PresetLorenz,
    @JsonProperty PresetAizawa,
    @JsonProperty PresetHalvorsen,
    @JsonProperty PresetNewtonLeipnik,
    @JsonProperty StrangeQuadratic,
    @JsonProperty StrangeCubic,
    @JsonProperty StrangeQuartic
}
