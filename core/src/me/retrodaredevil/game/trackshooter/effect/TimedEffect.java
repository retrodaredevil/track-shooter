package me.retrodaredevil.game.trackshooter.effect;

import me.retrodaredevil.game.trackshooter.world.World;

public abstract class TimedEffect implements Effect {
	private final long timeLast;
	private Long startTime = null;
	private boolean done;

	protected TimedEffect(long last){
		this.timeLast = last;
	}

	@Override
	public boolean isDone() {
		return done;
	}

	@Override
	public float percentDone() {
		if(startTime == null){
			return 1;
		}
		float r = (System.currentTimeMillis() - startTime) / timeLast;
		return Math.min(1, r);
	}

	@Override
	public void update(float delta, World world) {
		long now = System.currentTimeMillis();
		if(startTime == null){
			startTime = now;
			onStart(world);
		}
		if(startTime + timeLast < now){
			done = true;
			onEnd(world);
			return;
		}
		onUpdate(delta, world);
	}
	protected abstract void onStart(World world);
	protected abstract void onUpdate(float delta, World world);
	protected abstract void onEnd(World world);
}
