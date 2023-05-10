package me.retrodaredevil.game.trackshooter.entity.movement;

import me.retrodaredevil.game.trackshooter.world.World;

public class TimedMoveComponent extends SimpleMoveComponent{

	private final World world;
	private Float startTime = null;
	private final float time;

	public TimedMoveComponent(World world, float time, MoveComponent nextComponent, boolean canHaveNext, boolean canRecycle){
		super(nextComponent, canHaveNext, canRecycle);
		this.world = world;
		this.time = time;
	}
	public TimedMoveComponent(World world, float time, MoveComponent nextComponent){
		this(world, time, nextComponent, true, true);

	}

	public TimedMoveComponent(World world, float time){
		this(world, time, null);
	}

	@Override
	protected void onStart() {
		startTime = world.getTime();
	}
	@Override
	protected void onUpdate(float delta) {
		done = startTime + time <= world.getTime();
	}
	@Override
	protected void onEnd() {
		startTime = null;
	}

	@Override
	public boolean isDone() {
		return super.isDone();
	}

}
