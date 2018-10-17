package me.retrodaredevil.game.trackshooter.level;

import me.retrodaredevil.game.trackshooter.entity.Entity;
import me.retrodaredevil.game.trackshooter.level.functions.LevelFunction;
import me.retrodaredevil.game.trackshooter.world.Track;
import me.retrodaredevil.game.trackshooter.world.World;

import java.util.*;

/**
 * A very abstract class that helps out with start time, and level number
 */
public abstract class SimpleLevel implements Level {

	private final int number;
	private final Track track;
//	private Long startTime = null;
	private float time = 0;

	private LevelMode mode = null; // initialized in first call to update()
//	private Long modeStartTime = null; // changed and first initialized in setMode()
	private float modeTime = 0; // reset when mode changes

	private final List<Entity> entityList = new ArrayList<>(); // list of entities handled by the level

	private final List<LevelFunction> functions = new ArrayList<>();
	private final Queue<LevelFunction> addFunctionsQueue = new ArrayDeque<>(); // used to poll new functions from

	private LevelEndState lastLevelEndState = null;

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
		assert !done : "Level wasn't terminated correctly!";
		final boolean firstRun = time == 0;
		time += delta;
		modeTime += delta;
		if(firstRun){
			assert mode == null;
			setMode(LevelMode.RESET);
			onStart(world);
		}
		assert mode != null;
		World.updateEntityList(entityList);
		onUpdate(delta, world);

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
		if(!firstRun) { // we don't want to check if it's done on the first run because not everything may have been initialized
			this.done = shouldLevelEnd(world);
			if (this.done) {
				end(world);
			}
		}
	}
	/**
	 * Should be called once every update() call
	 * @return true if this level is able to end, false otherwise
	 */
	private boolean shouldLevelEnd(World world){
		Collection<CanLevelEnd> endCheckCollection = new ArrayList<>();
		endCheckCollection.addAll(world.getEntities());
		endCheckCollection.addAll(functions);

		LevelEndState highest = LevelEndState.CAN_END;
		for(CanLevelEnd endCheck : endCheckCollection){
			LevelEndState state = endCheck.canLevelEnd(world);
			if(state.value > highest.value){
				highest = state;
			}
		}
		lastLevelEndState = highest;
//		System.out.println("level: " + getNumber() + " is ending. checks: " + endCheckCollection);
		return highest.value <= LevelEndState.CAN_END.value;
	}
	private void end(World world){
		for(Entity entity : entityList){
			if(entity.canSetToRemove()){
				entity.setToRemove();
			}
		}
		for(LevelFunction function : functions){
			function.levelEnd(world);
		}
		onEnd(world);
	}

	@Override
	public boolean isEndingSoon() {
		return lastLevelEndState == LevelEndState.CAN_END_SOON || lastLevelEndState == LevelEndState.CAN_END;
	}

	protected abstract void onStart(World world);

	protected abstract void onUpdate(float delta, World world);
	protected abstract void onEnd(World world);


	protected boolean isStarted(){
		return time != 0;
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
	public Collection<Entity> getEntities() {
		return entityList;
	}

	@Override
	public void setMode(LevelMode mode) {
		assert mode != null;
		if(mode != this.mode){
			LevelMode previousMode = this.mode;
			this.mode = mode;
			modeTime = 0;
//			System.out.println("changing level mode! mode: " + mode + " previous: " + previousMode);
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
	public long getModeTimeMillis() {
		return (long) (modeTime * 1000L);
	}

	@Override
	public float getModeTime() {
		return modeTime;
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
	public long getLevelTimeMillis(){
		return (long) (time * 1000L);
	}
	@Override
	public float getLevelTime(){
		return time;
	}

}
