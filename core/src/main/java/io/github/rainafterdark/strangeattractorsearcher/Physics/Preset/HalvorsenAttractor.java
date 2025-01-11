package io.github.rainafterdark.strangeattractorsearcher.Physics.Preset;

import com.badlogic.gdx.math.Vector3;
import imgui.ImGui;
import io.github.rainafterdark.strangeattractorsearcher.Physics.Attractor;
import io.github.rainafterdark.strangeattractorsearcher.Util.ImGuiHelper;

public class HalvorsenAttractor implements Attractor {
    private float a;

    @Override
    public Vector3 initial() {
        return new Vector3(1f, 0f, 0f);
    }

    @Override
    public Vector3 step(Vector3 point, float deltaTime) {
        float x = point.x;
        float y = point.y;
        float z = point.z;
        float dt = 0.5f * deltaTime;

        float dx = (-a * x) - (4 * y) - (4 * z) - (y * y);
        float dy = (-a * y) - (4 * z) - (4 * x) - (z * z);
        float dz = (-a * z) - (4 * x) - (4 * y) - (x * x);

        Vector3 nP = point.cpy();
        nP.x += dx * dt;
        nP.y += dy * dt;
        nP.z += dz * dt;
        return nP;
    }

    @Override
    public void initParams() {
        a = 1.4f;
    }

    @Override
    public void renderParams() {
        ImGui.textWrapped("dx = (-a * x) - (4 * y) - (4 * z) - (y * y)");
        ImGui.textWrapped("dy = (-a * y) - (4 * z) - (4 * x) - (z * z)");
        ImGui.textWrapped("dz = (-a * z) - (4 * x) - (4 * y) - (x * x)");
        ImGuiHelper.floatWidget("a", null, () -> a, v -> a = v, 0.01f, -10, 10, "%.2f");
    }
}
