package io.github.rainafterdark.strangeattractorsearcher.GUI;

import com.badlogic.gdx.math.Vector3;
import imgui.ImGui;
import imgui.flag.ImGuiCond;
import imgui.flag.ImGuiWindowFlags;
import io.github.rainafterdark.strangeattractorsearcher.Data.DebugSingleton;

public class DebugWindow implements Window {
    private final DebugSingleton debug = DebugSingleton.getInstance();
    private float timeAccumulator = 0f;
    private float frameRate = 0f;
    private int calculations = 0;
    private int lineSegments = 0;
    private Vector3 centerPoint = new Vector3();
    private float zoom = 0f;

    @Override
    public void render() {
        timeAccumulator += ImGui.getIO().getDeltaTime();
        float pollRate = 0.25f;
        if (timeAccumulator > pollRate) {
            frameRate = ImGui.getIO().getFramerate();
            calculations = (int) (debug.getCalculations() * frameRate);
            lineSegments = debug.getLineSegments();
            centerPoint = debug.getAutoCenterPoint();
            zoom = debug.getAutoZoom();
            timeAccumulator = 0f;
        }
        ImGui.begin("Debug", ImGuiWindowFlags.AlwaysAutoResize);
        ImGui.setWindowPos(ImGui.getMainViewport().getSizeX() - 200,10, ImGuiCond.Once);
        ImGui.text(String.format("FPS: %.3f", frameRate));
        ImGui.text(String.format("Calculations: %,d/s", calculations));
        ImGui.text(String.format("Line Segments: %,d", lineSegments));
        Vector3 p = centerPoint;
        ImGui.text(String.format("Camera Center Point:\n" +
            "(%.3f, %.3f, %.3f)", p.x, p.y, p.z));
        ImGui.text(String.format("Camera Zoom: %.3f", zoom));
        ImGui.end();
    }
}
