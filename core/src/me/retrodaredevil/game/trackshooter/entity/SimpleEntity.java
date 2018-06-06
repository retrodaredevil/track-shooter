package me.retrodaredevil.game.trackshooter.entity;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import me.retrodaredevil.game.trackshooter.CollisionIdentity;
import me.retrodaredevil.game.trackshooter.entity.movement.MoveComponent;
import me.retrodaredevil.game.trackshooter.render.RenderComponent;
import me.retrodaredevil.game.trackshooter.util.CannotHitException;
import me.retrodaredevil.game.trackshooter.util.HitboxUtil;
import me.retrodaredevil.game.trackshooter.world.World;

public class SimpleEntity implements Entity {
//	private static final Vector2 temp = new Vector2();

	/** If you do not want this entity to support respawning, set this to false so the program will crash when that happens. */
	protected boolean canRespawn = true;
	/** If you want to be able to remove this entity at will, set this to true */
	protected boolean canSetToRemove = false;
	protected CollisionIdentity collisionIdentity = CollisionIdentity.UNKNOWN;


	private int spawnTimes = 0; // the amount of times the entity has spawned

	private float rotation = 0; // in degrees

	/** Changed only in afterRemove() */
	private boolean removed = false;
	/** Changed in setToRemove() */
	private boolean forceRemove = false;

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
	@Override
	public void setLocation(float x, float y){
		hitbox.setCenter(x, y);
	}

	@Override
	public void setLocation(float x, float y, float rotation){
		setLocation(x, y);
		setRotation(rotation);
	}

	@Override
	public void setLocation(Vector2 location, float rotation) {
		setLocation(location);
		setRotation(rotation);
	}

	@Override
	public float getX() {
		return hitbox.x + (hitbox.width / 2.0f);
	}

	@Override
	public float getY() {
		return hitbox.y + (hitbox.height / 2.0f);
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

	protected void setMoveComponent(MoveComponent moveComponent){
		if(this.moveComponent != moveComponent && this.moveComponent != null){
			this.moveComponent.end();
		}
		this.moveComponent = moveComponent;
	}

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
			if(moveComponent.isDone()){
				MoveComponent next = moveComponent.getNextComponent();
				moveComponent.end();
				moveComponent = next;
			}
		}
	}

	@Override
	public Entity getShooter() {
		return null;
	}

	@Override
	public void beforeSpawn(World world) {
		this.removed = false;
		spawnTimes++;
		if(!canRespawn && spawnTimes > 1){
			throw new IllegalStateException(this.toString() + " cannot respawn");
		}
	}
	/**
	 * By default returns false. It is recommended to call this and isInBounds()
	 */
	@Override
	public boolean shouldRemove(World world) {
		return forceRemove;
	}

	protected boolean isInBounds(World world){
		return world.getBounds().overlaps(this.getHitbox());
	}

	@Override
	public void afterRemove(World world) {
		if(this.removed){
			throw new IllegalStateException(this.toString() + " is already removed!");
		}
		this.removed = true;
		if (renderComponent != null) {
			renderComponent.dispose();
		}
	}

	@Override
	public boolean isRemoved() {
		return removed;
	}

	@Override
	public boolean canSetToRemove() {
		return canSetToRemove;
	}

	@Override
	public void setToRemove() {
		if(!canSetToRemove){
			throw new IllegalStateException(this + " cannot be forcibly removed.");
		}
		this.forceRemove = true;
	}

	@Override
	public void onHit(World world, Entity other) throws CannotHitException {
		throw new CannotHitException(other, this);
	}

	@Override
	public CollisionIdentity getCollisionIdentity() {
		return collisionIdentity;
	}

}
