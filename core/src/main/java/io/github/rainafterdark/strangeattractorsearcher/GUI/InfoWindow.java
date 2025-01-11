package io.github.rainafterdark.strangeattractorsearcher.GUI;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector3;
import imgui.ImGui;
import imgui.flag.ImGuiCond;
import imgui.flag.ImGuiWindowFlags;
import io.github.rainafterdark.strangeattractorsearcher.Data.DebugSingleton;
import io.github.rainafterdark.strangeattractorsearcher.Util.ImGuiHelper;

public class InfoWindow implements Window {
    private final DebugSingleton debug = DebugSingleton.getInstance();
    private float timeAccumulator = 0f;
    private float frameRate = 0f;
    private long heap = 0;
    private int calculations = 0;
    private int lineSegments = 0;
    private Vector3 centerPoint = new Vector3();
    private float zoom = 0f;

    private void poll() {
        timeAccumulator += ImGui.getIO().getDeltaTime();
        float pollRate = 0.25f;
        if (timeAccumulator > pollRate) {
            frameRate = ImGui.getIO().getFramerate();
            heap = Gdx.app.getJavaHeap() / 1024 / 1024;
            calculations = (int) (debug.getCalculations() * frameRate);
            lineSegments = debug.getLineSegments();
            centerPoint = debug.getAutoCenterPoint().cpy();
            zoom = debug.getAutoZoom();
            timeAccumulator = 0f;
        }
    }

    private void renderDebugTab() {
        ImGui.text(String.format("FPS: %.3f", frameRate));
        ImGui.text(String.format("Heap: %,d MB", heap));
        ImGui.text(String.format("Calculations: %,d/s", calculations));
        ImGui.text(String.format("Line Segments: %,d", lineSegments));
        Vector3 p = centerPoint;
        ImGui.text(String.format("Camera Center Point:\n" +
            "(%.3f, %.3f, %.3f)", p.x, p.y, p.z));
        ImGui.text(String.format("Camera Zoom: %.3f", zoom));
        ImGui.checkbox("Draw Axes", debug.getDrawAxes());
    }

    private void renderKeybindingsTab() {
        ImGui.text("R: Reset Simulation");
        ImGui.text("H: Hide/Show UI");
        ImGui.text("<: Previous Attractor");
        ImGui.text(">: Next Attractor");
        ImGui.text("Esc: Exit");
    }

    @Override
    public void render() {
        poll();
        ImGui.begin("Info", ImGuiWindowFlags.AlwaysAutoResize);
        ImGui.setWindowPos(ImGui.getMainViewport().getSizeX() - 250,10, ImGuiCond.FirstUseEver);
        ImGui.text("Strange Attractor Searcher v1.1.0");
        ImGuiHelper.treeTab("Debug", true, this::renderDebugTab);
        ImGuiHelper.treeTab("Keybindings", false, this::renderKeybindingsTab);
        ImGui.end();
    }
}
