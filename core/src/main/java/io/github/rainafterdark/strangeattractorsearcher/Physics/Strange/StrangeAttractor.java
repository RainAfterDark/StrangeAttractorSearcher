package io.github.rainafterdark.strangeattractorsearcher.Physics.Strange;

import com.fasterxml.jackson.annotation.*;
import io.github.rainafterdark.strangeattractorsearcher.Physics.Attractor;

import java.util.Arrays;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY)
@JsonSubTypes({
    @JsonSubTypes.Type(StrangeQuadraticAttractor.class),
    @JsonSubTypes.Type(StrangeCubicAttractor.class),
    @JsonSubTypes.Type(StrangeQuarticAttractor.class)
})
public abstract class StrangeAttractor implements Attractor {
    @JsonProperty protected final float[] coefficients;

    @JsonCreator
    protected StrangeAttractor(@JsonProperty("coefficients") float[] coefficients) {
        this.coefficients = Arrays.copyOf(coefficients, coefficients.length);
    }

    @Override public String toString() {
        return String.format("%04X-%s", this.hashCode() & 0xFFFF,
            this.getClass().getSimpleName()
                .replace("Strange", "")
                .replace("Attractor", ""));
    }
}
