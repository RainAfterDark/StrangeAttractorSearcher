package io.github.rainafterdark.strangeattractorsearcher;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import io.github.rainafterdark.strangeattractorsearcher.Data.ConfigSingleton;
import io.github.rainafterdark.strangeattractorsearcher.GUI.GUIRenderer;
import io.github.rainafterdark.strangeattractorsearcher.Render.ParticleRenderer;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends ApplicationAdapter {
    private ConfigSingleton config;
    private GUIRenderer guiRenderer;
    private ParticleRenderer particleRenderer;

    @Override
    public void create() {
        Gdx.graphics.setUndecorated(true);
        Graphics.DisplayMode displayMode = Gdx.graphics.getDisplayMode();
        Gdx.graphics.setWindowedMode(displayMode.width, displayMode.height);
        config = ConfigSingleton.getInstance();
        guiRenderer = new GUIRenderer();
        particleRenderer = new ParticleRenderer();
    }

    @Override
    public void render() {
        particleRenderer.render(Gdx.graphics.getDeltaTime());
        guiRenderer.render();
    }

    @Override
    public void resume() {
        particleRenderer.resume();
    }

    @Override
    public void dispose() {
        guiRenderer.disposeImGui();
        particleRenderer.dispose();
        config.saveToFile();
    }
}
