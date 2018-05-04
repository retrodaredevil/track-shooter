package me.retrodaredevil.game.trackshooter.entity;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import me.retrodaredevil.game.trackshooter.entity.movement.MoveComponent;
import me.retrodaredevil.game.trackshooter.render.RenderComponent;
import me.retrodaredevil.game.trackshooter.util.HitboxUtil;
import me.retrodaredevil.game.trackshooter.world.World;

public class SimpleEntity implements Entity {

	private float rotation = 0; // in degrees

	/** Normally changed only in afterRemove() so either set this to true or call super of afterRemove() */
	private boolean removed = false;

	private MoveComponent moveComponent = null;
	private RenderComponent renderComponent = null;

	private EntityController entityController = null;

	private final Rectangle hitbox; // also stores location data but must retrieve using HitboxUtil

	protected SimpleEntity(){
		hitbox = HitboxUtil.createHitbox(0, 0, 1, 1);
	}

	protected void setHitboxSize(float width, float height){
		HitboxUtil.hitboxSetSize(hitbox, width, height);
	}

	@Override
	public String toString() {
		return this.getClass().getSimpleName() + "{hitbox:" + this.getHitbox().toString() +
				",hashCode:" + this.hashCode() + "}";
	}

	@Override
	public Vector2 getLocation() {
		return hitbox.getCenter(new Vector2());
	}

	@Override
	public void setLocation(Vector2 location) {
		hitbox.setCenter(location);
	}

	/**
	 * Note, you should NEVER edit the values of this hitbox unless you use HitboxUtil and you know what you are doing
	 * Also, the x and y values are probably not the actual location values
	 *
	 * @return The hitbox of the entity
	 */
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
