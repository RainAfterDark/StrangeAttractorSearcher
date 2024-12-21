package io.github.rainafterdark.strangeattractorsearcher.GUI;

import imgui.ImGui;
import imgui.flag.ImGuiCond;
import imgui.flag.ImGuiWindowFlags;
import io.github.rainafterdark.strangeattractorsearcher.Data.ConfigSingleton;
import io.github.rainafterdark.strangeattractorsearcher.Data.StrangeConfig;
import io.github.rainafterdark.strangeattractorsearcher.Physics.AttractorType;
import io.github.rainafterdark.strangeattractorsearcher.Physics.StrangeAttractor;
import io.github.rainafterdark.strangeattractorsearcher.Physics.StrangeAttractorCallback;
import io.github.rainafterdark.strangeattractorsearcher.Physics.StrangeAttractorSearcher;
import io.github.rainafterdark.strangeattractorsearcher.Util.ImGuiHelper;

public class AttractorSearchWindow implements Window {
    private final StrangeConfig config = ConfigSingleton.getInstance().getStrange();
    private StrangeAttractorSearcher searcher;
    private Thread searchThread;
    private int lastAttempts = -1;
    private boolean lastSuccess = false;

    private void startSearch(AttractorType attractorType) {
        searcher = new StrangeAttractorSearcher(
            new StrangeAttractorCallback() {
                @Override
                public void onAttractorFound(StrangeAttractor attractor, int attempts) {
                    System.out.println("Found attractor after " + attempts + " attempts");
                    config.getSavedAttractors().add(attractor);
                    config.setSelectedAttractor(config.getSavedAttractors().size() - 1);
                    ConfigSingleton.getInstance().saveToFile();
                }
                @Override
                public void onAttractorSearchFinished(boolean success, int attempts) {
                    lastSuccess = success;
                    lastAttempts = attempts;
                    searchThread = null;
                }
            }, attractorType);
        searchThread = new Thread(searcher);
        searchThread.start();
    }

    @Override
    public void render() {
        ImGui.begin("Strange Attractor Search", ImGuiWindowFlags.AlwaysAutoResize);
        ImGui.setWindowPos(10, ImGui.getMainViewport().getSizeY() - 225, ImGuiCond.Once);
        ImGuiHelper.enumWidget("Attractor Type",
                "Select from a set of preset equations,\n" +
                "or a randomly parameterized strange one.",
            AttractorType.class, config::getAttractorType, config::setAttractorTypeGeneric);
        boolean shouldDisable = config.getSavedAttractors().isEmpty() ||
            (config.getAttractorType() != AttractorType.StrangeQuadratic &&
                config.getAttractorType() != AttractorType.StrangeCubic);
        if (searchThread != null || shouldDisable) ImGui.beginDisabled();
        ImGuiHelper.intWidget("Max Iterations",
            "Controls the maximum iterations for attractor search.",
            config::getMaxIterations, config::setMaxIterations, 10f, 500, 10000);
        ImGuiHelper.floatWidget("Search Radius",
            "Controls the search radius for attractor coefficients.",
            config::getSearchRadius, config::setSearchRadius, 0.01f, 0.01f, 10f, "%.2f");
        ImGuiHelper.floatWidget("Divergence Threshold",
            "Controls the divergence threshold for attractor search.",
            config::getDivergenceThreshold, config::setDivergenceThreshold, 1000f, 1f, 1e8f, "%.2e");
        ImGuiHelper.floatWidget("Convergence Threshold",
            "Controls the convergence threshold for attractor search.",
            config::getConvergenceThreshold, config::setConvergenceThreshold, 0.001f, 1e-8f, 1f, "%.2e");
        ImGuiHelper.floatWidget("Lyapunov Threshold",
            "Controls the lyapunov threshold for attractor search.",
            config::getLyapunovThreshold, config::setLyapunovThreshold, 0.001f, 1e-8f, 0.01f, "%.2e");
        if (searchThread != null && !shouldDisable) ImGui.endDisabled();

        ImGuiHelper.listWidget("Attractor",
            "Select a saved attractor to view.",
            config.getSavedAttractors(), config::getSelectedAttractor, config::setSelectedAttractor);

        if (searchThread != null) {
            if (ImGui.button("Stop Search")) {
                searcher.terminate();
                searchThread.interrupt();
                searcher = null;
                searchThread = null;
                lastAttempts = -1;
            }
        } else {
            if (ImGui.button("Start Search")) {
                startSearch(config.getAttractorType());
            }
        }

        if (lastAttempts != -1) {
            ImGui.sameLine();
            if (lastSuccess) {
                ImGui.text("Success! " + lastAttempts + " attempts.");
            } else {
                ImGui.text("Failed. " + lastAttempts + " attempts.");
            }
        }
        if (shouldDisable) ImGui.endDisabled();
        ImGui.end();
    }
}
