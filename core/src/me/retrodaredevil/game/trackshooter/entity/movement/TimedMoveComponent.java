package me.retrodaredevil.game.trackshooter.entity.movement;

import me.retrodaredevil.game.trackshooter.world.World;

public class TimedMoveComponent extends SimpleMoveComponent{

	private Long startTime;
	private final long time;

	public TimedMoveComponent(long time, MoveComponent nextComponent, boolean canHaveNext, boolean canRecycle){
		super(nextComponent, canHaveNext, canRecycle);
		this.time = time;
	}
	public TimedMoveComponent(long time, MoveComponent nextComponent){
		this(time, nextComponent, true, true);

	}

	public TimedMoveComponent(long time){
		this(time, null);
	}

	@Override
	protected void onStart(World world) {
		startTime = world.getTimeMillis();
	}
	@Override
	protected void onUpdate(float delta, World world) {
		done = startTime + time <= world.getTimeMillis();
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
