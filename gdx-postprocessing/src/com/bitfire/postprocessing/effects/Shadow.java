package com.bitfire.postprocessing.effects;

import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.bitfire.postprocessing.PostProcessorEffect;
import com.bitfire.postprocessing.filters.ShadowFilter;

public final class Shadow extends PostProcessorEffect {
	private ShadowFilter shadow = null;

	public Shadow (int viewportWidth, int viewportHeight) {
		setup(viewportWidth, viewportHeight);
	}

	private void setup (int viewportWidth, int viewportHeight) {
		shadow = new ShadowFilter(viewportWidth, viewportHeight);
	}
	
	public void SetSize(int width, int height){
		shadow.SetSize(width, height);
	}
	
	public void SetColor(float a){
		shadow.SetColor(a);
	}

	@Override
	public void dispose () {
		shadow.dispose();
	}

	@Override
	public void rebind () {
		shadow.rebind();
	}

	@Override
	public void render (FrameBuffer src, FrameBuffer dest) {
		restoreViewport(dest);
		shadow.setInput(src).setOutput(dest).render();
	}
}
