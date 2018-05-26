package me.retrodaredevil.game.trackshooter.level;

import me.retrodaredevil.game.trackshooter.entity.Entity;
import me.retrodaredevil.game.trackshooter.entity.RemovableEntity;
import me.retrodaredevil.game.trackshooter.level.functions.LevelFunction;
import me.retrodaredevil.game.trackshooter.world.Track;
import me.retrodaredevil.game.trackshooter.world.World;

import java.util.*;

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
	private final Queue<LevelFunction> addFunctionsQueue = new ArrayDeque<>();

	private boolean done = false; // set in update

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
		this.done = onUpdate(delta, world);

		for(Iterator<LevelFunction> it = functions.listIterator(); it.hasNext(); ){
			LevelFunction function = it.next();
			boolean functionDone = function.update(delta, world, addFunctionsQueue);
			if (functionDone) {
				it.remove();
			}
		}
		while(!addFunctionsQueue.isEmpty()){
			LevelFunction element = addFunctionsQueue.poll();
			functions.add(element);
		}
		if(this.done){
			end(world);
		}
	}
	private void end(World world){
		for(Entity entity : entityList){
			if(entity instanceof RemovableEntity){
				((RemovableEntity) entity).setToRemove();
			}
		}
		for(LevelFunction function : functions){
			function.levelEnd(world);
		}
		onEnd(world);
	}
	protected abstract void onStart(World world);

	/**
	 *
	 * @param delta
	 * @param world
	 * @return true if this level is done, false otherwise
	 */
	protected abstract boolean onUpdate(float delta, World world);
	protected abstract void onEnd(World world);

	protected boolean isStarted(){
		return startTime != null;
	}

	@Override
	public boolean isDone() {
		return done;
	}

	/**
	 * NOTE: This function should never be called in LevelFunction's update() because this would add function to a list
	 * that is being iterated over. This should be called at the start of a level
	 * @param function The function to add
	 */
	protected void addFunction(LevelFunction function){
		this.functions.add(function);
	}

	@Override
	public void addEntity(World world, Entity entity){
		world.addEntity(entity);
		entityList.add(entity);
	}

	@Override
	public void setMode(LevelMode mode) {
		assert mode != null;
		if(mode != this.mode){
			LevelMode previousMode = this.mode;
			this.mode = mode;
			modeStartTime = System.currentTimeMillis();
			onModeChange(mode, previousMode);
			for(LevelFunction function : functions){
				function.onModeChange(this, mode, previousMode);
			}
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
