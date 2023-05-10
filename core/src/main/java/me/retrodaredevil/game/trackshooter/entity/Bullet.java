package me.retrodaredevil.game.trackshooter.entity;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

import me.retrodaredevil.game.trackshooter.CollisionIdentity;
import me.retrodaredevil.game.trackshooter.entity.movement.FixedVelocityMoveComponent;
import me.retrodaredevil.game.trackshooter.entity.player.Player;
import me.retrodaredevil.game.trackshooter.level.LevelEndState;
import me.retrodaredevil.game.trackshooter.render.components.ImageRenderComponent;
import me.retrodaredevil.game.trackshooter.util.CannotHitException;
import me.retrodaredevil.game.trackshooter.world.World;

public class Bullet extends SimpleEntity implements Entity {

	private final Entity shooter;
	private final float shotDistance2; // the shot distance squared

	private Entity hitEntity = null; // right now this is just used to check if it's null. Maybe use in future // init when hit

	private Vector2 startingLocation = null;

	/**
	 *
	 * @param shooter The entity that shot this bullet
	 * @param start The starting location of this bullet. It can be mutated after constructor is called
	 * @param velocity The velocity of the bullet. It cannot be mutated after the constructor is called
	 */
	public Bullet(World world, Entity shooter, Vector2 start, Vector2 velocity, float rotation, float shotDistance, CollisionIdentity collisionIdentity){
		super(world);
		this.shooter = shooter;
		this.shotDistance2 = shotDistance * shotDistance;
		setHitboxSize(.25f);
		setMoveComponent(new FixedVelocityMoveComponent(this, velocity));
		setLocation(start, rotation);
		canRespawn = false;
		// TODO This may cause bugs in the future when enemies shoot bullets. Maybe provide a better way to change levelEndStateWhenActive
		levelEndStateWhenActive = collisionIdentity == CollisionIdentity.ENEMY_PROJECTILE ? LevelEndState.CANNOT_END : LevelEndState.CAN_END_SOON;
		this.collisionIdentity = collisionIdentity; // super.collisionIdentity same thing
	}

	@Override
	public void beforeSpawn() {
		super.beforeSpawn();
		setRenderComponent(new ImageRenderComponent(new Image(world.getMainSkin().getDrawable("bullet")), this, .5f, .5f));
	}

	@Override
	public void update(float delta) {
		super.update(delta);
		if(startingLocation == null){
			startingLocation = this.getLocation();
		}
	}

	public static Bullet createFromEntity(World world, Entity entity, float speed, float directionOffsetDegrees, float shotDistance, CollisionIdentity collisionIdentity){
		float rotation = entity.getRotation() + directionOffsetDegrees;
		Vector2 velocity = new Vector2(MathUtils.cosDeg(rotation), MathUtils.sinDeg(rotation));
		velocity.scl(speed);
		return new Bullet(world, entity, entity.getLocation(), velocity, rotation, shotDistance, collisionIdentity);
	}

	@Override
	public Entity getShooter(){
		return shooter;
	}

	@Override
	public void onHit(Entity other) {
		if(hitEntity != null){
			throw new IllegalStateException("I hit something twice!!");
		}
		CollisionIdentity identity = other.getCollisionIdentity();
		if(other == shooter || identity == CollisionIdentity.ENEMY_PROJECTILE
				|| identity == CollisionIdentity.FRIENDLY_PROJECTILE || identity == CollisionIdentity.POWERUP){
			throw new CannotHitException(other, this);
		}
		if(shooter != null){
		    if(shooter instanceof Player){
		        Player player = (Player) shooter;
		        player.getScoreObject().onBulletHit(other, this);
            }
        }
		this.hitEntity = other;
	}

	@Override
	public boolean shouldRemove() {
		return super.shouldRemove() || hitEntity != null
				|| (this.startingLocation != null && startingLocation.dst2(getX(), getY()) > shotDistance2);
	}

	public enum ShotType{
		/** A single bullet is fired straight*/
		STRAIGHT,
		/** 3 bullets are fired, one goes straight the other two go off to the left and right respectively at the same angle*/
		TRIPLE,
		/** Multiple bullets fire in a general direction randomly */
		SHOT_GUN,
		/** Multiple shots fire in all directions evenly spaced*/
		FULL
	}
}
