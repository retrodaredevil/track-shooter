package me.retrodaredevil.game.trackshooter.render;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.viewport.Viewport;

public class UIViewport extends Viewport {
	private final float minimumVirtualSize;
	public UIViewport(float minimumVirtualSize){
		setCamera(new OrthographicCamera());
		this.minimumVirtualSize = minimumVirtualSize;
	}
	@Override
	public void update (int screenWidth, int screenHeight, boolean centerCamera) {
		setScreenBounds(0, 0, screenWidth, screenHeight);
		if(screenWidth > screenHeight){
			float height = minimumVirtualSize;
			float width = screenWidth * height / screenHeight;

			setWorldSize(width, height);
		} else {
			float width = minimumVirtualSize;
			float height = screenHeight * width / screenWidth;
			setWorldSize(width, height);
		}
		apply(centerCamera);
	}
}
