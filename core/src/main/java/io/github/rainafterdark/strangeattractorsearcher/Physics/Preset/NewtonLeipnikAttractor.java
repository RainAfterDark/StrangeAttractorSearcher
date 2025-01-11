package io.github.rainafterdark.strangeattractorsearcher.Physics.Preset;

import com.badlogic.gdx.math.Vector3;
import imgui.ImGui;
import io.github.rainafterdark.strangeattractorsearcher.Physics.Attractor;
import io.github.rainafterdark.strangeattractorsearcher.Util.ImGuiHelper;

public class NewtonLeipnikAttractor implements Attractor {
    private float a;
    private float b;

    @Override
    public Vector3 initial() {
        return new Vector3();
    }

    @Override
    public Vector3 step(Vector3 point, float deltaTime) {
        float x = point.x;
        float y = point.y;
        float z = point.z;
        float dt = 0.75f * deltaTime;

        float dx = -(a * x) + y + (10 * y * z);
        float dy = -x - (0.4f * y) + (5f * x * z);
        float dz = (b * z) - (5 * x * y);

        Vector3 nP = point.cpy();
        nP.x += dx * dt;
        nP.y += dy * dt;
        nP.z += dz * dt;
        return nP;
    }

    @Override
    public void initParams() {
        a = 0.4f;
        b = 0.175f;
    }

    @Override
    public void renderParams() {
        ImGui.textWrapped("dx = -(a * x) + y + (10 * y * z)");
        ImGui.textWrapped("dy = -x - (0.4 * y) + (5 * x * z)");
        ImGui.textWrapped("dz = (b * z) - (5 * x * y)");
        ImGuiHelper.floatWidget("a", null, () -> a, v -> a = v, 0.001f, -1, 1, "%.3f");
        ImGuiHelper.floatWidget("b", null, () -> b, v -> b = v, 0.001f, -1, 1, "%.3f");
    }
}
