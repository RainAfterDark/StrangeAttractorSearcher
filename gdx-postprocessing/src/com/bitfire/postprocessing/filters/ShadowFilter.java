package com.bitfire.postprocessing.filters;

import com.badlogic.gdx.math.Vector2;
import com.bitfire.utils.ShaderLoader;

public class ShadowFilter extends Filter<ShadowFilter>{

	public enum Param implements Parameter {
		Texture("u_texture0", 0), Sky("u_sky", 0), Resolution("resolution", 0);

		private String mnemonic;
		private int elementSize;

		private Param (String mnemonic, int arrayElementSize) {
			this.mnemonic = mnemonic;
			this.elementSize = arrayElementSize;
		}

		@Override
		public String mnemonic () {
			return this.mnemonic;
		}

		@Override
		public int arrayElementSize () {
			return this.elementSize;
		}
	}

	public ShadowFilter (int width, int height) {
		super(ShaderLoader.fromFile("screenspace", "shadow"));
		rebind();
	}
	
	public void SetSize(int width, int height){
		setParams(Param.Resolution, new Vector2(width,height));
		endParams();
	}
	
	public void SetColor (float a){
		//setParam(Param.Color, a);
		endParams();
	}

	@Override
	public void rebind () {
		// reimplement super to batch every parameter
		setParams(Param.Texture, u_texture0);
		//setParams(Param.Shadow, 17);
		setParams(Param.Sky, 8);
		//setParams(Param.Tile, 16);
	//	setParams(Param.Resolution, new Vector2(width,height));
		
	}

	@Override
	protected void onBeforeRender () {
		inputTexture.bind(u_texture0);
	}
}
