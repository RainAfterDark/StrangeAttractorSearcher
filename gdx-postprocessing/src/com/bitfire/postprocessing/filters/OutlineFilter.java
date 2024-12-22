package com.bitfire.postprocessing.filters;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.bitfire.utils.ShaderLoader;

public class OutlineFilter extends Filter<OutlineFilter>{
	private int width, height;
	Vector3 color;

	public OutlineFilter (int width, int height) {
		super(ShaderLoader.fromFile("outline", "outline"));
		SetSize(width,height);
		color = new Vector3(1,1,1);
		rebind();
	}
	
	public void SetSize(int width, int height){
		// set screensize
		setParams("u_viewportInverse", new Vector2(1f/width,1f/height));
		endParams();
		this.width = width;
		this.height = height;
	}
	
	public void SetOutlineColor (float r, float g, float b){
		//set color
		setParams("u_color", new Vector3(r,g,b));
		endParams();
	}
	
	public void SetStep (float a){
		setParams("u_step", a);
		endParams();
	}
	
	public void SetOutlineWidth (float a){
		setParams("u_offset", a);
		endParams();
	}

	@Override
	public void rebind () {
		SetOutlineWidth(1f);
		SetStep(Math.min(1f, width / 70f));
		SetOutlineColor(color.x,color.y,color.z);
		SetSize(width,height);
		endParams();
	}

	@Override
	protected void onBeforeRender () {
		inputTexture.bind(u_texture0);
	}
}