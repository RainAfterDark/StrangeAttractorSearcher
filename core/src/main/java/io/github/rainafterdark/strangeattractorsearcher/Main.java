package io.github.rainafterdark.strangeattractorsearcher;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
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
        config = ConfigSingleton.getInstance();
        guiRenderer = new GUIRenderer();
        guiRenderer.initImGui();
        particleRenderer = new ParticleRenderer();
        particleRenderer.init();
        Gdx.gl.glEnable(GL20.GL_DEPTH_TEST);
    }

    @Override
    public void render() {
        // Clear the screen
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE);
        //Gdx.gl.glBlendEquation(GL20.GL_FUNC_ADD);
        particleRenderer.render(Gdx.graphics.getDeltaTime());
        Gdx.gl.glDisable(GL20.GL_BLEND);
        guiRenderer.render();
    }

    @Override
    public void dispose() {
        guiRenderer.disposeImGui();
        particleRenderer.dispose();
        config.saveToFile();
    }
}
