package me.retrodaredevil.game.trackshooter.level;

import me.retrodaredevil.game.trackshooter.entity.Entity;
import me.retrodaredevil.game.trackshooter.world.Track;
import me.retrodaredevil.game.trackshooter.world.World;

import java.util.ArrayList;
import java.util.List;

/**
 * A very abstract class that helps out with start time, and level number
 */
public abstract class SimpleLevel implements Level {

	private final int number;
	private Track track;
	private Long startTime = null;

	private LevelMode mode = null;
	private Long modeStartTime = null;

	private List<Entity> entityList = new ArrayList<>(); // list of entities handled by the level

	private List<LevelFunction> functions = new ArrayList<>();

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
		World.updateEntityList(entityList);
		onUpdate(delta, world);
//		for(Iterator<LevelFunction> it = functions.listIterator(); it.hasNext(); ){
//			LevelFunction function = it.next();
//			boolean done = function.update(delta, world);
//			if (done) {
//				it.remove();
//			}
//		}
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

	@Override
	public long getLevelTime(){
		if(startTime == null){
			return -1;
		}
		return System.currentTimeMillis() - startTime;
	}
}
