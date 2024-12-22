package io.github.rainafterdark.strangeattractorsearcher.GUI;

import imgui.ImGui;
import imgui.flag.ImGuiCond;
import imgui.flag.ImGuiWindowFlags;
import io.github.rainafterdark.strangeattractorsearcher.Data.Config.CameraConfig;
import io.github.rainafterdark.strangeattractorsearcher.Data.Config.ColorConfig;
import io.github.rainafterdark.strangeattractorsearcher.Data.ConfigSingleton;
import io.github.rainafterdark.strangeattractorsearcher.Data.Config.ParticleConfig;
import io.github.rainafterdark.strangeattractorsearcher.Util.ImGuiHelper;

public class ConfigWindow implements Window {
    private void renderPhysicsTab() {
        ParticleConfig physics = ConfigSingleton.getInstance().getParticle();
        ImGuiHelper.floatWidget("Simulation Speed",
            "Controls the speed of the particles.\n" +
                "Can cause severe lag. Tweak with caution.",
            physics::getSimulationSpeed, physics::setSimulationSpeed, 0.01f, 0.01f, 10f, "%.2f");
        ImGuiHelper.floatWidget("Max Velocity",
                "Culls the particles if they exceed this velocity\n" +
                "(per frame). Can remove erratic lines, but can\n" +
                "also remove too many particles for some attractors.",
            physics::getMaxVelocity, physics::setMaxVelocity, 0.01f, 0.01f, 10f, "%.2f");
        ImGuiHelper.floatWidget("Spawn Radius",
            "Starting random spawn radius of particles.",
            physics::getSpawnRadius, physics::setSpawnRadius, 0.01f, 0.01f, 10f, "%.2f");
        ImGuiHelper.floatWidget("Cutoff Distance",
            "Distance where particles are destroyed.\n" +
                "Used to zoom in on certain attractors.",
            physics::getCutoffDistance, physics::setCutoffDistance, 1f, 10f, 200f, "%.2f");
        ImGuiHelper.floatWidget("Respawn Time",
            "Time in seconds between particle respawns.\n" +
                "0 means particles respawn every frame.",
            physics::getRespawnTime, physics::setRespawnTime, 0.01f, 0f, 10f, "%.2f");
        ImGuiHelper.intWidget("Particle Count",
            "Controls the amount of particles. Can cause lag.",
            physics::getParticleCount, physics::setParticleCount, 5f, 1, 2000);
        ImGuiHelper.intWidget("Trail Length",
            "Controls the trail segment length.\n" +
                "Can cause severe lag. Tweak with caution.",
            physics::getTrailLength, physics::setTrailLength, 5f, 10, 2000);
        ImGuiHelper.intWidget("Step Resolution",
                "The number of physics steps per frame.\n" +
                "Can make the simulation smoother and\n" +
                "more accurate. Can cause heavy lag.",
            physics::getStepResolution, physics::setStepResolution, 1f, 60, 500);
    }

    private void renderColorTab() {
        ColorConfig colorConfig = ConfigSingleton.getInstance().getColor();
        ImGuiHelper.floatWidget("Gradient Scaling",
            "Controls how spread out the gradient for\n" +
                "particles is. Color is based on velocity.",
            colorConfig::getGradientScaling, colorConfig::setGradientScaling, 0.01f, 0.001f, 2f, "%.3f");
        ImGuiHelper.gradientWidget("Gradient", colorConfig::getGradientColor, colorConfig::setGradientColor);
    }

    private void renderCameraTab() {
        CameraConfig cameraConfig = ConfigSingleton.getInstance().getCamera();
        ImGuiHelper.floatWidget("Horizontal Speed",
            "Controls the horizontal speed of the camera.",
            cameraConfig::getHorizontalMovementSpeed, cameraConfig::setHorizontalMovementSpeed, 0.01f, 0.01f, 3.14f, "%.2f");
        ImGuiHelper.floatWidget("Vertical Speed",
            "Controls the vertical speed of the camera.",
            cameraConfig::getVerticalMovementSpeed, cameraConfig::setVerticalMovementSpeed, 0.01f, 0.01f, 3.14f, "%.2f");
    }

    @Override
    public void render() {
        ImGui.begin("Config", ImGuiWindowFlags.AlwaysAutoResize);
        ImGui.setWindowPos(10, 10, ImGuiCond.Once);
        ImGuiHelper.treeTab("Physics", true, this::renderPhysicsTab);
        ImGuiHelper.treeTab("Color", true, this::renderColorTab);
        ImGuiHelper.treeTab("Camera", true, this::renderCameraTab);
        ImGui.end();
    }
}
