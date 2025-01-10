package io.github.rainafterdark.strangeattractorsearcher.GUI;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Graphics;
import imgui.ImGui;
import imgui.ImGuiIO;
import imgui.gl3.ImGuiImplGl3;
import imgui.glfw.ImGuiImplGlfw;

import java.util.ArrayList;
import java.util.List;

public class GUIRenderer {
    private static final String INI_FILE = "sas_imgui.ini";
    private static final String GLSL_VERSION = "#version 150";

    private ImGuiImplGlfw imGuiGlfw;
    private ImGuiImplGl3 imGuiGl3;
    private InputProcessor tmpProcessor;
    private final List<Window> windows = new ArrayList<>();
    private boolean showGUI = true;

    public GUIRenderer() {
        imGuiGlfw = new ImGuiImplGlfw();
        imGuiGl3 = new ImGuiImplGl3();
        long windowHandle = ((Lwjgl3Graphics) Gdx.graphics).getWindow().getWindowHandle();
        ImGui.createContext();
        ImGuiIO io = ImGui.getIO();
        io.setIniFilename(INI_FILE);
        io.getFonts().addFontDefault();
        io.getFonts().build();
        imGuiGlfw.init(windowHandle, true);
        imGuiGl3.init(GLSL_VERSION);
        ImGui.styleColorsClassic();

        windows.add(new SearchWindow());
        windows.add(new ConfigWindow());
        windows.add(new InfoWindow());
    }

    private void startImGui() {
       if (tmpProcessor != null) { // Restore the input processor after ImGui caught all inputs, see #end()
          Gdx.input.setInputProcessor(tmpProcessor);
          tmpProcessor = null;
       }
       imGuiGl3.newFrame();
       imGuiGlfw.newFrame();
       ImGui.newFrame();
    }

    private void endImGui() {
       ImGui.render();
       imGuiGl3.renderDrawData(ImGui.getDrawData());
       // If ImGui wants to capture the input, disable libGDX's input processor
       if (ImGui.getIO().getWantCaptureKeyboard() || ImGui.getIO().getWantCaptureMouse()) {
          tmpProcessor = Gdx.input.getInputProcessor();
          Gdx.input.setInputProcessor(null);
       }
    }

    private void handleInput() {
       if (Gdx.input.isKeyJustPressed(Input.Keys.H))
            showGUI = !showGUI;
    }

    public void render() {
        handleInput();
        if (!showGUI) return;
        startImGui();
        for (Window window : windows) {
            window.render();
        }
        endImGui();
    }

    public void disposeImGui() {
       imGuiGl3.shutdown();
       imGuiGl3 = null;
       imGuiGlfw.shutdown();
       imGuiGlfw = null;
       ImGui.destroyContext();
    }
}
