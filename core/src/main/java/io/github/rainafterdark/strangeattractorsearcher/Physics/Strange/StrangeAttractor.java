package io.github.rainafterdark.strangeattractorsearcher.Physics.Strange;

import imgui.ImGui;
import io.github.rainafterdark.strangeattractorsearcher.Physics.Attractor;
import io.github.rainafterdark.strangeattractorsearcher.Util.ImGuiHelper;

import java.util.Arrays;

public abstract class StrangeAttractor implements Attractor {
    protected final float[] coefficients;
    protected transient float[] c;

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

    @Override
    public void initParams() {
        c = Arrays.copyOf(coefficients, coefficients.length);
    }

    @Override
    public void renderParams() {
        ImGui.textWrapped("See implementation of " + this.getClass().getSimpleName());
        for (int i = 0; i < coefficients.length; i++) {
            int finalI = i;
            ImGuiHelper.floatWidget("C" + i, null,
                () -> c[finalI], v -> c[finalI] = v,
                0.01f, -10, 10, "%.2f");
        }
    }
}
