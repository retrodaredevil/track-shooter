package me.retrodaredevil.game.trackshooter.entity.movement;

import me.retrodaredevil.game.trackshooter.world.World;

public class PointTargetMoveComponent implements MoveComponent {
	@Override
	public MoveComponent getNextComponent() {
		return this;
	}

	@Override
	public void update(float delta, World world) {

	}
}
