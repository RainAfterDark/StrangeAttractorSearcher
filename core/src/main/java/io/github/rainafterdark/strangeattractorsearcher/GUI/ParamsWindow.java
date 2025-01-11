package io.github.rainafterdark.strangeattractorsearcher.GUI;

import imgui.ImGui;
import imgui.flag.ImGuiCond;
import imgui.flag.ImGuiWindowFlags;
import io.github.rainafterdark.strangeattractorsearcher.Data.ConfigSingleton;

public class ParamsWindow implements Window {
    private final ConfigSingleton config = ConfigSingleton.getInstance();

    @Override
    public void render() {
        ImGui.begin("Attractor Parameters", ImGuiWindowFlags.AlwaysAutoResize);
        ImGui.setWindowPos(420, ImGui.getMainViewport().getSizeY() - 225, ImGuiCond.FirstUseEver);
        if (config.getSelectedAttractor() != null) {
            ImGui.beginChild("Params", ImGui.getContentRegionAvailX(), 155, false);
            config.getSelectedAttractor().renderParams();
            ImGui.endChild();
            if (ImGui.button("Reset"))
                config.getSelectedAttractor().initParams();
            ImGui.sameLine();
            ImGui.text("(Changes here are not saved)");
        } else {
            ImGui.text("No attractor selected");
        }
        ImGui.end();
    }
}
