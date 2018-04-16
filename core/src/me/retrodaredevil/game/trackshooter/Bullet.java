package me.retrodaredevil.game.trackshooter;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import me.retrodaredevil.game.trackshooter.render.ImageRenderComponent;

public class Bullet extends SimpleEntity {

	private Entity shooter;

	/**
	 *
	 * @param shooter The entity that shot this bullet
	 * @param start The starting location of this bullet. It can be mutated after constructor is called
	 * @param velocity The velocity of the bullet. It cannot be mutated after the constructor is called
	 */
	public Bullet(Entity shooter, Vector2 start, Vector2 velocity, float rotation){
		this.shooter = shooter;
		this.hitbox.setSize(0.25f, 0.25f);
		setMoveComponent(new VelocityMoveComponent(this, velocity));
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

}
