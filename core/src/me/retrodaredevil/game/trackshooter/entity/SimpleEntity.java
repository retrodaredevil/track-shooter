package me.retrodaredevil.game.trackshooter.entity;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import me.retrodaredevil.game.trackshooter.entity.movement.MoveComponent;
import me.retrodaredevil.game.trackshooter.render.RenderComponent;
import me.retrodaredevil.game.trackshooter.world.World;

public class SimpleEntity implements Entity {

	private float rotation = 0; // in degrees

	/** Normally changed only in afterRemove() so either set this to true or call super of afterRemove() */
	protected boolean removed = false;

	private MoveComponent moveComponent = null;
	private RenderComponent renderComponent = null;

	private EntityController entityController = null;

	private final Rectangle hitbox = new Rectangle(0, 0, 1, 1); // also stores location data

	protected SimpleEntity(){
	}

	@Override
	public String toString() {
		return this.getClass().getSimpleName() + "{hitbox:" + this.getHitbox().toString() +
				",hashCode:" + this.hashCode() + "}";
	}

	@Override
	public Vector2 getLocation() {
		return new Vector2(hitbox.x, hitbox.y);
	}

	@Override
	public void setLocation(Vector2 location) {
		hitbox.setPosition(location);
	}

	@Override
	public Rectangle getHitbox() {
		return hitbox;
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
		if (entityController != null) {
			entityController.update(delta, world);
		}
		if (moveComponent != null) {
			moveComponent.update(delta, world);
			moveComponent = moveComponent.getNextComponent();
		}
	}

	@Override
	public Entity getShooter() {
		return null;
	}

	@Override
	public boolean shouldRemove(World world) {
		if(removed){
			throw new IllegalStateException("Already removed entity!");
		}
		return !world.getBounds().overlaps(this.getHitbox());
	}

	@Override
	public void afterRemove(World world) {
		this.removed = true;
		if (renderComponent != null) {
			renderComponent.dispose();
		}
	}

	@Override
	public boolean isRemoved() {
		return removed;
	}
}
