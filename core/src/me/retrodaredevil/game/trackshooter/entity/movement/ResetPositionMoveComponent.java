package me.retrodaredevil.game.trackshooter.entity.movement;

import com.badlogic.gdx.math.Vector2;
import me.retrodaredevil.game.trackshooter.entity.Entity;
import me.retrodaredevil.game.trackshooter.world.World;

public class ResetPositionMoveComponent extends SimpleMoveComponent {

	private Entity entity;
	private Vector2 startingPosition;
	private float startingRotation;

	protected ResetPositionMoveComponent(Entity entity, Vector2 startingPosition, float startingRotation) {
		super(null, true, true); // this is designed to be recycled
		this.entity = entity;
		this.startingPosition = startingPosition;
		this.startingRotation = startingRotation;
	}

	@Override
	protected void onStart(World world) {
		
	}

	@Override
	protected void onEnd() {

	}

	@Override
	protected void onUpdate(float delta, World world) {

	}
}
