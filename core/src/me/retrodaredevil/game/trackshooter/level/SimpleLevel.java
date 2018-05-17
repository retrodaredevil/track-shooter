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

	protected boolean isStarted(){
		return startTime != null;
	}

	/**
	 * Note that instead of testing this for -1, you should use isStarted()
	 *
	 * @return The amount of time since this onStart() was called or -1 if it hasn't started yet.
	 */
	protected long getActiveTime(){
		if(startTime == null){
			return -1;
		}
		return System.currentTimeMillis() - startTime;
	}
}
