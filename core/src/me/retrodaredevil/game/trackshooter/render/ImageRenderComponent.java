package me.retrodaredevil.game.trackshooter.render;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Align;
import me.retrodaredevil.game.trackshooter.entity.Entity;

public class ImageRenderComponent implements RenderComponent{

	protected final Image image;
	protected final Entity entity;

	private float width;
	private float height;

	private int facingDirection = 90;

	/**
	 * Note this assumes that the image is facing up and it rotates it accordingly.
	 *
	 * @param image The image to use
	 * @param entity The entity to follow
	 * @param width The width of the image
	 * @param height The height of the image
	 */
	public ImageRenderComponent(Image image, Entity entity, float width, float height){
		this.image = image;
		this.entity = entity;
		setSize(width, height);
	}

	/**
	 * This is used so you can make your image with the image facing up. (90 degrees is up and is the default if this method
	 * is not called)
	 * <br/><br/>
	 * If you create an image facing sideways or an image with text, you should call this method and pass 0 as facingDirection
	 * @param facingDirection The direction the image is facing. By default 90
	 */
	public ImageRenderComponent setFacingDirection(int facingDirection){
		this.facingDirection = facingDirection;
		return this;
	}
	@Override
	public void render(float delta, Stage stage) {
		if(image.getStage() != stage){
			stage.addActor(image);
		}
		Vector2 location = entity.getLocation();
		image.setPosition(location.x - image.getOriginX(), location.y - image.getOriginY());
		int rotation = (int) entity.getRotation() - facingDirection;
		image.setRotation(rotation);
	}

	/**
	 * Should be called as few times as possible.
	 */
	private void updateBounds(){
		image.setSize(width, height);
//		image.setOrigin(width / 2f, height / 2f);
		image.setOrigin(Align.center);
	}
	public void setSize(float width, float height){
		this.width = width;
		this.height = height;
		updateBounds();
	}

	@Override
	public void dispose() {
		image.remove();
	}
}
