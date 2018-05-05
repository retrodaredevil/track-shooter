package me.retrodaredevil.game.trackshooter.entity.powerup;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import me.retrodaredevil.game.trackshooter.render.ImageRenderComponent;
import me.retrodaredevil.game.trackshooter.render.ShakeImageRenderComponent;

public class Cherry extends Fruit {
	public Cherry(float startingTrackDistance) {
		super(100, 1.5f, startingTrackDistance);
		ImageRenderComponent renderComponent = new ShakeImageRenderComponent(new Image(new Texture("cherry.png")),
				this, 1, 1, 250, new Vector2(0, .03f));
		renderComponent.setFacingDirection(0);
		setRenderComponent(renderComponent);
	}
}
