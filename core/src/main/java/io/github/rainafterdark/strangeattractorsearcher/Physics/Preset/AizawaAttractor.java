package io.github.rainafterdark.strangeattractorsearcher.Physics.Preset;

import com.badlogic.gdx.math.Vector3;
import imgui.ImGui;
import io.github.rainafterdark.strangeattractorsearcher.Physics.Attractor;
import io.github.rainafterdark.strangeattractorsearcher.Util.ImGuiHelper;

public class AizawaAttractor implements Attractor {
    private float a;
    private float b;
    private float c;
    private float d;
    private float e;
    private float f;

    @Override
    public Vector3 initial() {
        return new Vector3();
    }

    @Override
    public Vector3 step(Vector3 point, float deltaTime) {
        float x = point.x;
        float y = point.y;
        float z = point.z;
        float dt = 0.5f * deltaTime;

        float dx = ((z - b) * x - d * y);
        float dy = (d * x + (z - b) * y);
        float dz = c + a * z - (z * z * z / 3) - (x * x + y * y) * (1 + e * z) + f * z * x * x * x;

        Vector3 nP = point.cpy();
        nP.x += dx * dt;
        nP.y += dy * dt;
        nP.z += dz * dt;
        return nP;
    }

    @Override
    public void initParams() {
        a = 0.95f;
        b = 0.7f;
        c = 0.6f;
        d = 3.5f;
        e = 0.25f;
        f = 0.1f;
    }

    @Override
    public void renderParams() {
        ImGui.textWrapped("dx = ((z - b) * x - d * y)");
        ImGui.textWrapped("dy = (d * x + (z - b) * y)");
        ImGui.textWrapped("dz = c + a * z - (z ^ 2 / 3) - (x ^ 2 + y ^ 2) * (1 + e * z) + f * z * x ^ 3");
        ImGuiHelper.floatWidget("a", null, () -> a, v -> a = v, 0.01f, -10, 10, "%.2f");
        ImGuiHelper.floatWidget("b", null, () -> b, v -> b = v, 0.01f, -10, 10, "%.2f");
        ImGuiHelper.floatWidget("c", null, () -> c, v -> c = v, 0.01f, -10, 10, "%.2f");
        ImGuiHelper.floatWidget("d", null, () -> d, v -> d = v, 0.01f, -10, 10, "%.2f");
        ImGuiHelper.floatWidget("e", null, () -> e, v -> e = v, 0.01f, -10, 10, "%.2f");
        ImGuiHelper.floatWidget("f", null, () -> f, v -> f = v, 0.01f, -10, 10, "%.2f");
    }
}
