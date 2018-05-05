package me.retrodaredevil.game.trackshooter.render;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import me.retrodaredevil.game.trackshooter.entity.Entity;

public class ShakeImageRenderComponent extends ImageRenderComponent{
	private long shakeTime;
	private Vector2 add;

	/**
	 *
	 * @param shakeTime The amount of time for the image to go up and down
	 * @param add The amount to add when the image is up, will use opposite when image is down
	 */
	public ShakeImageRenderComponent(Image image, Entity entity, float width, float height, long shakeTime, Vector2 add) {
		super(image, entity, width, height);
		this.shakeTime = shakeTime;
		this.add = add;
	}

	@Override
	public void render(float delta, Stage stage) {
		super.render(delta, stage);
		float x = image.getX();
		float y = image.getY();

		boolean up = System.currentTimeMillis() % shakeTime < shakeTime / 2;
		if(up){
			x += add.x;
			y += add.y;
		} else {
			x -= add.x;
			y -= add.y;
		}

		image.setPosition(x, y);
	}
}
