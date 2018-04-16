package me.retrodaredevil.game.trackshooter;

import com.badlogic.gdx.math.Vector2;
import me.retrodaredevil.game.trackshooter.world.World;

public class VelocityMoveComponent extends MoveComponent {

	private Entity entity;
	private Vector2 velocity;

	/**
	 *
	 * @param entity The entity to move
	 * @param velocity The amount to move per second. Should not be altered after being passed to constructor
	 */
	public VelocityMoveComponent(Entity entity, Vector2 velocity){
		this.entity = entity;
		this.velocity = velocity;
	}

	@Override
	public void update(float delta, World world) {
		Vector2 current = entity.getLocation();
		current.add(velocity.x * delta, velocity.y * delta);
		entity.setLocation(current);
	}
}
