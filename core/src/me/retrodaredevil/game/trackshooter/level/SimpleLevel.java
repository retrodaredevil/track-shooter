package me.retrodaredevil.game.trackshooter.level;

import me.retrodaredevil.game.trackshooter.world.Track;
import me.retrodaredevil.game.trackshooter.world.World;

/**
 * A very abstract class that helps out with start time, and level number
 */
public abstract class SimpleLevel implements Level {

	private final int number;
	private Track track;
	private Long startTime = null;

	private LevelMode mode = null;
	private Long modeStartTime = null;

	protected SimpleLevel(int number, Track track){
		this.number = number;
		this.track = track;
	}

	@Override
	public int getNumber() {
		return number;
	}

	@Override
	public void update(float delta, World world) {
		if(startTime == null){
			startTime = System.currentTimeMillis();
			assert mode == null;
			setMode(LevelMode.RESET);
			onStart(world);
		}
		assert mode != null;
		onUpdate(delta, world);
	}
	protected abstract void onUpdate(float delta, World world);
	protected abstract void onStart(World world);

	protected boolean isStarted(){
		return startTime != null;
	}

	@Override
	public void setMode(LevelMode mode) {
		assert mode != null;
		if(mode != this.mode){
			LevelMode previousMode = this.mode;
			this.mode = mode;
			modeStartTime = System.currentTimeMillis();
			onModeChange(mode, previousMode);
		}
	}
	@Override
	public LevelMode getMode() {
		return mode;
	}

	@Override
	public long getModeTime() {
		if(modeStartTime == null || getMode() == null){
			throw new IllegalStateException();
		}
		return System.currentTimeMillis() - modeStartTime;
	}

	@Override
	public Track getTrack() {
		return track;
	}

	/**
	 * This method should never call setMode() because this method is called by setMode whenever the mode changes
	 * @param mode
	 * @param previousMode
	 */
	protected abstract void onModeChange(LevelMode mode, LevelMode previousMode);

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
