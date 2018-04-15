package me.retrodaredevil.game.trackshooter;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import me.retrodaredevil.game.trackshooter.render.ImageRenderComponent;
import me.retrodaredevil.game.trackshooter.render.RenderComponent;
import me.retrodaredevil.game.trackshooter.world.World;

public class SimpleEntity implements Entity {

	private Vector2 location = new Vector2();
	private float rotation = 0; // in degrees

	private MoveComponent moveComponent = null;
	private RenderComponent renderComponent = null;

	private EntityController entityController = null;

	private Rectangle hitbox = new Rectangle(0, 0, 1, 1); // must call getHitbox() to update position

	protected SimpleEntity(){
	}

	@Override
	public Vector2 getLocation() {
		return location.cpy();
	}

	@Override
	public void setLocation(Vector2 location) {
		this.location = location;
	}

	@Override
	public float getRotation() {
		return rotation;
	}

	@Override
	public void setRotation(float rotation) {
		this.rotation = rotation;
	}

	@Override
	public Rectangle getHitbox() {
		hitbox.setPosition(location.x - (hitbox.width / 2f), location.y - (hitbox.height / 2f));
		return hitbox;
	}

	@Override
	public MoveComponent getMoveComponent() {
		return moveComponent;
	}

	protected void setMoveComponent(MoveComponent moveComponent){ this.moveComponent = moveComponent; }

	@Override
	public RenderComponent getRenderComponent() {
		return renderComponent;
	}

	protected void setRenderComponent(RenderComponent renderComponent){ this.renderComponent = renderComponent; }

	@Override
	public EntityController getEntityController() {
		return entityController;
	}

	@Override
	public void setEntityController(EntityController entityController) {
		this.entityController = entityController;
	}

	@Override
	public void update(float delta, World world) {
		entityController.update(delta, world);
		moveComponent.update(delta, world);
	}
}
