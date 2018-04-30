package me.retrodaredevil.game.trackshooter.entity.movement;

import com.badlogic.gdx.math.Vector2;
import me.retrodaredevil.game.trackshooter.entity.Entity;
import me.retrodaredevil.game.trackshooter.world.World;

public class FixedVelocityMoveComponent implements MoveComponent {

	private Entity entity;
	private Vector2 velocity;

	/**
	 *
	 * @param entity The entity to move
	 * @param velocity The amount to move per second. Should not be altered after being passed to constructor
	 */
	public FixedVelocityMoveComponent(Entity entity, Vector2 velocity){
		this.entity = entity;
		this.velocity = velocity;
	}

	@Override
	public void update(float delta, World world) {
		Vector2 current = entity.getLocation();
		current.add(velocity.x * delta, velocity.y * delta);
		entity.setLocation(current);
	}

	@Override
	public MoveComponent getNextComponent() {
		return this;
	}
}
