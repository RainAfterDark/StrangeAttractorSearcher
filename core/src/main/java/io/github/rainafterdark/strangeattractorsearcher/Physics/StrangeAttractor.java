package io.github.rainafterdark.strangeattractorsearcher.Physics;

import com.fasterxml.jackson.annotation.*;

import java.util.Arrays;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY)
@JsonSubTypes({
    @JsonSubTypes.Type(StrangeQuadraticAttractor.class),
    @JsonSubTypes.Type(StrangeCubicAttractor.class)
})
public abstract class StrangeAttractor implements Attractor {
    @JsonProperty protected final float[] coefficients;

    @JsonCreator
    protected StrangeAttractor(@JsonProperty("coefficients") float[] coefficients) {
        this.coefficients = Arrays.copyOf(coefficients, coefficients.length);
    }

    @Override public String toString() {
        return String.format("%08X", this.hashCode());
    }
}
