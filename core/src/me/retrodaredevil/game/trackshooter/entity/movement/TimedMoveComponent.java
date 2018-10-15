package me.retrodaredevil.game.trackshooter.entity.movement;

import me.retrodaredevil.game.trackshooter.world.World;

public class TimedMoveComponent extends SimpleMoveComponent{

	private Float startTime = null;
	private final float time;

	public TimedMoveComponent(float time, MoveComponent nextComponent, boolean canHaveNext, boolean canRecycle){
		super(nextComponent, canHaveNext, canRecycle);
		this.time = time;
	}
	public TimedMoveComponent(float time, MoveComponent nextComponent){
		this(time, nextComponent, true, true);

	}

	public TimedMoveComponent(float time){
		this(time, null);
	}

	@Override
	protected void onStart(World world) {
		startTime = world.getTime();
	}
	@Override
	protected void onUpdate(float delta, World world) {
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
