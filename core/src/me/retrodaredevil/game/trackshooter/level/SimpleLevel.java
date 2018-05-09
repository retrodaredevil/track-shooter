package me.retrodaredevil.game.trackshooter.level;

import me.retrodaredevil.game.trackshooter.world.World;

/**
 * A very abstract class that helps out with start time, and level number
 */
public abstract class SimpleLevel implements Level {

	private final int number;
	private Long startTime = null;

	protected SimpleLevel(int number){
		this.number = number;
	}

	@Override
	public int getNumber() {
		return number;
	}

	@Override
	public void update(float delta, World world) {
		if(startTime == null){
			startTime = System.currentTimeMillis();
			onStart(world);
		}
		onUpdate(delta, world);
	}
	protected abstract void onUpdate(float delta, World world);
	protected abstract void onStart(World world);
}
