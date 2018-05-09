package me.retrodaredevil.game.trackshooter.level;

import me.retrodaredevil.game.trackshooter.world.World;

/**
 * The most basic level. It has enemies!
 */
public class EnemyLevel extends SimpleLevel {

	protected EnemyLevel(int number) {
		super(number);
	}

	@Override
	protected void onUpdate(float delta, World world) {

	}

	@Override
	protected void onStart(World world) {

	}

	@Override
	public boolean isDone() {
		return false;
	}

	@Override
	public boolean resetAll() {
		return false;
	}
}
