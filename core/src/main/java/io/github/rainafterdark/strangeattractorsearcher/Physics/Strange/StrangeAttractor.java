package io.github.rainafterdark.strangeattractorsearcher.Physics.Strange;

import io.github.rainafterdark.strangeattractorsearcher.Physics.Attractor;

import java.util.Arrays;

public abstract class StrangeAttractor implements Attractor {
    protected final float[] coefficients;

    protected StrangeAttractor(float[] coefficients) {
        this.coefficients = Arrays.copyOf(coefficients, coefficients.length);
    }

    @Override
    public String toString() {
        return String.format("%04X-%s", this.hashCode() & 0xFFFF,
            this.getClass().getSimpleName()
                .replace("Strange", "")
                .replace("Attractor", ""));
    }
}
