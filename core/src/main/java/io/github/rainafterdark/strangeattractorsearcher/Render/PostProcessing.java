package io.github.rainafterdark.strangeattractorsearcher.Render;

import com.badlogic.gdx.Gdx;
import com.bitfire.postprocessing.PostProcessor;
import com.bitfire.postprocessing.effects.Bloom;
import com.bitfire.postprocessing.effects.MotionBlur;
import com.bitfire.postprocessing.effects.Vignette;
import com.bitfire.utils.ShaderLoader;

public class PostProcessing {
    private PostProcessor postProcessor;

    public void init() {
        ShaderLoader.BasePath = "shaders/";
        postProcessor = new PostProcessor( false, false, true);
        Bloom bloom = new Bloom((int)(Gdx.graphics.getWidth() * 0.25f), (int)(Gdx.graphics.getHeight() * 0.25f));
        MotionBlur motionBlur = new MotionBlur();
        Vignette vignette = new Vignette(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false);
        vignette.setIntensity(1.5f);
        postProcessor.addEffect(bloom);
        postProcessor.addEffect(motionBlur);
        postProcessor.addEffect(vignette);
    }

    public void capture() {
        postProcessor.capture();
    }

    public void render() {
        postProcessor.render();
    }

    public void rebind() {
        postProcessor.rebind();
    }

    public void dispose() {
        postProcessor.dispose();
    }
}
