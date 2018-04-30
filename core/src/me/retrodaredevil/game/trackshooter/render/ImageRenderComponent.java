package me.retrodaredevil.game.trackshooter.render;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Align;
import me.retrodaredevil.game.trackshooter.entity.Entity;

public class ImageRenderComponent implements RenderComponent{

	private Image image;
	private Entity entity;

	private Float width;
	private Float height;

	/**
	 *
	 * @param image The image to use
	 * @param entity The entity to follow
	 * @param width The width of the image or null to use the hitbox height from the entity
	 * @param height The height of the image or null to use the hitbox height from the entity
	 */
	public ImageRenderComponent(Image image, Entity entity, Float width, Float height){
		this.image = image;
		this.entity = entity;
		this.width = width;
		this.height = height;
		updateBounds();
	}
	public ImageRenderComponent(Image image, Entity entity){
		this(image, entity, null, null);
	}
	@Override
	public void render(float delta, Stage stage) {
		if(image.getStage() != stage){
			stage.addActor(image);
		}
		Vector2 location = entity.getLocation();
		image.setPosition(location.x - image.getOriginX(), location.y - image.getOriginY());
		int rotation = (int) entity.getRotation() - 90;
		image.setRotation(rotation);
	}

	/**
	 * Should be called as few times as possible.
	 */
	private void updateBounds(){
		float width;
		float height;
		if (this.width != null) {
			width = this.width;
		} else {
			width = entity.getHitbox().getWidth();
		}
		if (this.height != null) {
			height = this.height;
		} else {
			height = entity.getHitbox().getHeight();
		}

		image.setSize(width, height);
//		image.setOrigin(width / 2f, height / 2f);
		image.setOrigin(Align.center);
	}

	@Override
	public void dispose() {
		image.remove();
	}
}
