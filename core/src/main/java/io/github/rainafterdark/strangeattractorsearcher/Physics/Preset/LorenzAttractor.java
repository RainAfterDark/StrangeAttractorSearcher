package io.github.rainafterdark.strangeattractorsearcher.Physics.Preset;

import com.badlogic.gdx.math.Vector3;
import imgui.ImGui;
import io.github.rainafterdark.strangeattractorsearcher.Physics.Attractor;
import io.github.rainafterdark.strangeattractorsearcher.Util.ImGuiHelper;

public class LorenzAttractor implements Attractor {
    private float sigma;
    private float rho;
    private float beta;

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

        float dx = sigma * (y - x);
        float dy = x * (rho - z) - y;
        float dz = x * y - beta * z;

        Vector3 nP = point.cpy();
        nP.x += dx * dt;
        nP.y += dy * dt;
        nP.z += dz * dt;
        return nP;
    }

    @Override
    public void initParams() {
        sigma = 10.0f;
        rho = 28.0f;
        beta = 8.0f / 3.0f;
    }

    @Override
    public void renderParams() {
        ImGui.textWrapped("dx = sigma * (y - x)");
        ImGui.textWrapped("dy = x * (rho - z) - y");
        ImGui.textWrapped("dz = x * y - beta * z");
        ImGuiHelper.floatWidget("sigma", null, () -> sigma, v -> sigma = v, 0.01f, -100, 100, "%.2f");
        ImGuiHelper.floatWidget("rho", null, () -> rho, v -> rho = v, 0.01f, -100, 100, "%.2f");
        ImGuiHelper.floatWidget("beta", null, () -> beta, v -> beta = v, 0.01f, -100, 100, "%.2f");
    }
}
