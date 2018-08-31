package me.retrodaredevil.game.trackshooter.entity;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import me.retrodaredevil.game.trackshooter.CollisionIdentity;
import me.retrodaredevil.game.trackshooter.effect.Effect;
import me.retrodaredevil.game.trackshooter.entity.movement.MoveComponent;
import me.retrodaredevil.game.trackshooter.item.Item;
import me.retrodaredevil.game.trackshooter.level.LevelEndState;
import me.retrodaredevil.game.trackshooter.render.components.RenderComponent;
import me.retrodaredevil.game.trackshooter.util.CannotHitException;
import me.retrodaredevil.game.trackshooter.util.HitboxUtil;
import me.retrodaredevil.game.trackshooter.util.Util;
import me.retrodaredevil.game.trackshooter.world.World;

import java.util.*;

public class SimpleEntity implements Entity {
//	private static final Vector2 temp = new Vector2();

	/** If you do not want this entity to support respawning, set this to false so the program will crash when that happens. */
	protected boolean canRespawn = true;
	/** If you want to be able to remove this entity at will, set this to true */
	protected boolean canSetToRemove = false;
	protected CollisionIdentity collisionIdentity = CollisionIdentity.UNKNOWN;
	/** Should only set set in a subclass constructor. Defaults to CAN_END. */
	protected LevelEndState levelEndStateWhenActive = LevelEndState.CAN_END;


	private int spawnTimes = 0; // the amount of times the entity has spawned
	/** Changed only in afterRemove() and beforeSpawn() */
	private boolean removed = false;
	/** Changed in setToRemove() */
	private boolean forceRemove = false;

	private MoveComponent moveComponent = null;
	private RenderComponent renderComponent = null;
	private EntityController entityController = null;

	private final Rectangle hitbox; // also stores location data but must retrieve using HitboxUtil
	private float rotation = 0; // in degrees

	private final List<Effect> effects = new ArrayList<>();
	private final List<Item> items = new ArrayList<>();

	protected SimpleEntity(){
		hitbox = HitboxUtil.createHitbox(0, 0, 1, 1);
	}

	protected void setHitboxSize(float width, float height){
		HitboxUtil.hitboxSetSize(hitbox, width, height);
	}
	protected void setHitboxSize(float size){
		setHitboxSize(size, size);
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

	/**
	 *
	 * @param renderComponent The RenderComponent to use to render this Entity
	 * @param autoDispose If renderComponent is different from the last, it will dispose the last RenderComponent
	 */
	protected void setRenderComponent(RenderComponent renderComponent, boolean autoDispose){
		if(autoDispose && this.renderComponent != renderComponent && this.renderComponent != null){
			this.renderComponent.dispose();
		}
		this.renderComponent = renderComponent;
	}

	/**
	 * calls {@link #setRenderComponent(RenderComponent,boolean)} with autoDispose=true
	 * @param renderComponent The RenderComponent to use to render this Entity
	 */
	protected void setRenderComponent(RenderComponent renderComponent){
		setRenderComponent(renderComponent, true);
	}

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
		for(Iterator<Effect> it = effects.iterator(); it.hasNext(); ){
			Effect effect = it.next();
			effect.update(delta, world);
			if(effect.isDone()){
				it.remove();
			}
		}
		for(Iterator<Item> it = items.iterator(); it.hasNext(); ){
			Item item = it.next();
			item.update(delta, world);
			if(item.isUsed()){
				it.remove();
			}
		}
		if (entityController != null) {
			entityController.update(delta, world);
		}
		if (moveComponent != null) {
			moveComponent.update(delta, world);
			if(moveComponent.isDone()){
				moveComponent.end();
				moveComponent = moveComponent.getNextComponent();
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
		disposeRenderComponent();
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
		if(this.isRemoved()){
			throw new IllegalStateException(this + " is already removed!");
		}
		if(!canSetToRemove){
			throw new IllegalStateException(this + " cannot be forcibly removed.");
		}
		this.forceRemove = true;
	}

	@Override
	public void onHit(World world, Entity other)  {
		if(this.collisionIdentity == CollisionIdentity.UNKNOWN){
			throw new CannotHitException(other, this, "The collisionIdentity of this entity is UNKNOWN so it never should have collided in the first place!");
		}
		throw new CannotHitException(other, this, "This entity is probably able to collide, but the onHit() method was not overridden!");
	}

	@Override
	public CollisionIdentity getCollisionIdentity() {
		return collisionIdentity;
	}

	@Override
	public List<Effect> getEffects() {
		return effects;
	}
	@Override
	public <T extends Effect> Collection<T> getEffects(Class<T> clazz) {
		return Util.getElementsOfClass(effects, clazz);
	}
	@Override
	public void addEffect(Effect effect) {
		effects.add(effect);
	}

	@Override
	public List<Item> getItems() {
		return items;
	}
	@Override
	public <T extends Item> Collection<T> getItems(Class<T> clazz) {
		return Util.getElementsOfClass(items, clazz);
	}
	@Override
	public void addItem(Item item) {
		items.add(item);
	}

	@Override
	public LevelEndState canLevelEnd(World world) {
		return levelEndStateWhenActive;
	}
}
