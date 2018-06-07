package me.retrodaredevil.game.trackshooter.entity.enemies;

import me.retrodaredevil.game.trackshooter.entity.Enemy;
import me.retrodaredevil.game.trackshooter.entity.SimpleEntity;

public class SnakePart extends SimpleEntity implements Enemy {
	@Override
	public void goToStart() {

	}

	@Override
	public boolean isGoingToStart() {
		return false;
	}

	@Override
	public void goNormalMode() {

	}
}
