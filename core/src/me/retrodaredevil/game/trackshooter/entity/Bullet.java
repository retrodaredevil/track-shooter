package me.retrodaredevil.game.trackshooter.entity;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import me.retrodaredevil.game.trackshooter.entity.movement.FixedVelocityMoveComponent;
import me.retrodaredevil.game.trackshooter.entity.powerup.Powerup;
import me.retrodaredevil.game.trackshooter.render.ImageRenderComponent;
import me.retrodaredevil.game.trackshooter.util.CannotHitException;
import me.retrodaredevil.game.trackshooter.util.Resources;
import me.retrodaredevil.game.trackshooter.world.World;

public class Bullet extends SimpleEntity implements Hittable {

	private Entity shooter;

	private Entity hitEntity = null; // right now this is just used to check if it's null. Maybe use in future

	/**
	 *
	 * @param shooter The entity that shot this bullet
	 * @param start The starting location of this bullet. It can be mutated after constructor is called
	 * @param velocity The velocity of the bullet. It cannot be mutated after the constructor is called
	 */
	public Bullet(Entity shooter, Vector2 start, Vector2 velocity, float rotation){
		this.shooter = shooter;
		setHitboxSize(.25f, .25f);
		setMoveComponent(new FixedVelocityMoveComponent(this, velocity));
		setRenderComponent(new ImageRenderComponent(new Image(Resources.BULLET_TEXTURE), this, .5f, .5f));
		setLocation(start);
		setRotation(rotation);
		canRespawn = false;
	}

	public static Bullet createFromEntity(Entity entity, float speed){
		float rotation = entity.getRotation();
		Vector2 velocity = new Vector2(MathUtils.cosDeg(rotation), MathUtils.sinDeg(rotation));
		velocity.scl(speed);
		return new Bullet(entity, entity.getLocation(), velocity, rotation);
	}

	@Override
	public Entity getShooter(){
		return shooter;
	}


	@Override
	public void onHit(World world, Entity other) throws CannotHitException{
		if(hitEntity != null){
			throw new IllegalStateException("I hit something twice!!");
		}
		if(other instanceof Bullet || other == shooter || other instanceof Powerup){
			throw new CannotHitException(other, this);
		}
		this.hitEntity = other;
	}

	@Override
	public boolean shouldRemove(World world) {
		return super.shouldRemove(world) || hitEntity != null;
	}
}
