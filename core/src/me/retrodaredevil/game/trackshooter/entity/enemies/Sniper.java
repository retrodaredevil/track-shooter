package me.retrodaredevil.game.trackshooter.entity.enemies;

import me.retrodaredevil.game.trackshooter.CollisionIdentity;
import me.retrodaredevil.game.trackshooter.entity.Enemy;
import me.retrodaredevil.game.trackshooter.entity.SimpleEntity;

public class Sniper extends SimpleEntity implements Enemy {

	public Sniper(){

		collisionIdentity = CollisionIdentity.ENEMY;
		canLevelEndWithEntityActive = false;
	}

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
