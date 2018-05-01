package me.retrodaredevil.game.trackshooter.entity;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import me.retrodaredevil.game.trackshooter.entity.movement.FixedVelocityMoveComponent;
import me.retrodaredevil.game.trackshooter.render.ImageRenderComponent;
import me.retrodaredevil.game.trackshooter.world.World;

public class Bullet extends SimpleEntity implements Hittable {

	private Entity shooter;

	private boolean hit = false;

	/**
	 *
	 * @param shooter The entity that shot this bullet
	 * @param start The starting location of this bullet. It can be mutated after constructor is called
	 * @param velocity The velocity of the bullet. It cannot be mutated after the constructor is called
	 */
	public Bullet(Entity shooter, Vector2 start, Vector2 velocity, float rotation){
		this.shooter = shooter;
		getHitbox().setSize(0.25f, 0.25f);
		setMoveComponent(new FixedVelocityMoveComponent(this, velocity));
		setRenderComponent(new ImageRenderComponent(new Image(new Texture("bullet.png")), this, .5f, .5f));
		setLocation(start);
		setRotation(rotation);
	}

	public static Bullet createFromEntity(Entity entity){
		float rotation = entity.getRotation();
		Vector2 velocity = new Vector2(MathUtils.cosDeg(rotation), MathUtils.sinDeg(rotation));
		velocity.scl(22);
		return new Bullet(entity, entity.getLocation(), velocity, rotation);
	}

	public Entity getShooter(){
		return shooter;
	}

	@Override
	public void onHit(World world, Entity other) {
		this.hit = true;
	}

	@Override
	public boolean shouldRemove(World world) {
		return hit || super.shouldRemove(world);
	}
}
