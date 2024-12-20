package io.github.rainafterdark.strangeattractorsearcher.GUI;

import imgui.ImGui;
import imgui.flag.ImGuiTreeNodeFlags;
import imgui.flag.ImGuiWindowFlags;
import imgui.type.ImInt;
import io.github.rainafterdark.strangeattractorsearcher.Data.ColorConfig;
import io.github.rainafterdark.strangeattractorsearcher.Data.ConfigSingleton;
import io.github.rainafterdark.strangeattractorsearcher.Data.ParticleConfig;
import io.github.rainafterdark.strangeattractorsearcher.Physics.AttractorType;
import io.github.rainafterdark.strangeattractorsearcher.Util.Widget;

public class ConfigWindow implements Window {
    private final ImInt selectedAttractor = new ImInt(0);
    @Override
    public void render() {
        ImGui.begin("Config", ImGuiWindowFlags.AlwaysAutoResize);
        int tabFlags = ImGuiTreeNodeFlags.DefaultOpen | ImGuiTreeNodeFlags.Leaf
                        | ImGuiTreeNodeFlags.Bullet | ImGuiTreeNodeFlags.Framed;
        if (ImGui.treeNodeEx("Physics", tabFlags)) {
            ImGui.unindent(ImGui.getTreeNodeToLabelSpacing());
            ParticleConfig physics = ConfigSingleton.getInstance().getParticle();
            Widget.floatWidget("Simulation Speed",
                "Controls the speed of the particles. Can cause lag.",
                physics::getSimulationSpeed, physics::setSimulationSpeed, 0.01f, 10f, 0.01f);
            Widget.intWidget("Particle Count",
                "Controls the amount of particles. Can cause lag.",
                physics::getParticleCount, physics::setParticleCount, 1, 2000, 1);
            Widget.intWidget("Trail Length",
                "Controls the trail segments of each particle.\n" +
                    "Can cause severe lag. Tweak with caution.",
                physics::getTrailLength, physics::setTrailLength, 10, 2000, 1);
            Widget.enumWidget("Attractor Type",
                "Select from a set of preset equations,\n" +
                    "or a randomly parameterized one (Quadratic Map).",
                AttractorType.class, physics::getAttractorType, physics::setAttractorTypeGeneric);
            ImGui.indent();
            ImGui.treePop();
        }
        if (ImGui.treeNodeEx("Color", tabFlags)) {
            ImGui.unindent(ImGui.getTreeNodeToLabelSpacing());
            ColorConfig colorConfig = ConfigSingleton.getInstance().getColor();
            Widget.floatWidget("Gradient Scaling",
                "Controls how spread out the gradient for\n" +
                    "particles is. Color is based on velocity.",
                colorConfig::getGradientScaling, colorConfig::setGradientScaling, 0.001f, 2f, 0.001f);
            ImGui.colorEdit3("Test", new float[]{0f, 0f, 0f});
            ImGui.indent();
            ImGui.treePop();
        }
        if (ImGui.treeNodeEx("Camera", tabFlags)) {
            ImGui.unindent(ImGui.getTreeNodeToLabelSpacing());
            ImGui.text("Hello World");
            ImGui.indent();
            ImGui.treePop();
        }
        ImGui.end();
    }
}
