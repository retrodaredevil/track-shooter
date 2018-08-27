package me.retrodaredevil.game.trackshooter.render.viewports;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.viewport.Viewport;

/**
 * Has a keeps the aspect ratio by only using a square as world space in the center of the screen
 * @deprecated does the same thing as {@link com.badlogic.gdx.utils.viewport.FitViewport}
 */
@Deprecated
public class SquareViewport extends Viewport {
	private final Float virtualSize;

	/**
	 *
	 * @param virtualSize The virtual or null to use the smallest screen size
	 */
	public SquareViewport(Float virtualSize){
		setCamera(new OrthographicCamera());
		this.virtualSize = virtualSize;
	}
	public SquareViewport(float virtualSize){
		this((Float) virtualSize);
	}
	@Override
	public void update (int screenWidth, int screenHeight, boolean centerCamera) {
//		System.out.println("working");
		if(screenWidth > screenHeight){ // wide screen
			setScreenBounds((screenWidth - screenHeight) / 2, 0, screenHeight, screenHeight);
			final float size = virtualSize != null ? virtualSize : screenHeight;
			setWorldSize(size, size);
		} else { // tall screen
			setScreenBounds(0, (screenHeight - screenWidth) / 2, screenWidth, screenWidth);
			final float size = virtualSize != null ? virtualSize : screenWidth;
			setWorldSize(size, size);

		}
		apply(centerCamera);
	}
}
