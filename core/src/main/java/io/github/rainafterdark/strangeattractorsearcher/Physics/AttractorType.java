package io.github.rainafterdark.strangeattractorsearcher.Physics;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum AttractorType {
    @JsonProperty Lorenz,
    @JsonProperty Halvorsen,
    @JsonProperty NewtonLeipnik,
    @JsonProperty QuadraticMap
}
