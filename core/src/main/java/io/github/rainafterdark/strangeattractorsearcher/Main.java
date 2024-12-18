package io.github.rainafterdark.strangeattractorsearcher;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import io.github.rainafterdark.strangeattractorsearcher.Render.GUI;
import io.github.rainafterdark.strangeattractorsearcher.Render.ParticleRenderer;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends ApplicationAdapter {
    private GUI gui;
    private ParticleRenderer particleRenderer;

    @Override
    public void create() {
        gui = new GUI();
        gui.initImGui();
        particleRenderer = new ParticleRenderer();
        particleRenderer.init();
        Gdx.gl.glEnable(GL20.GL_DEPTH_TEST);
    }

    @Override
    public void render() {
        // Clear the screen
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        gui.startImGui();
        //ImGui.button("Hello World!");
        gui.endImGui();
        particleRenderer.render(Gdx.graphics.getDeltaTime());
    }

    @Override
    public void dispose() {
        gui.disposeImGui();
        particleRenderer.dispose();
    }
}
