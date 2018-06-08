package me.retrodaredevil.game.trackshooter.effect;

import me.retrodaredevil.game.trackshooter.world.World;

public abstract class TimedEffect implements Effect {
	private final long timeLast; // the amount of time for the effect to last in ms
	private Long startTime = null;
	private boolean done = false;

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
		float r = (float) (System.currentTimeMillis() - startTime) / timeLast;
		return Math.min(1, r);
	}

	@Override
	public void update(float delta, World world) {
		long now = System.currentTimeMillis();
		if(startTime == null){
			startTime = now;
			onStart(world);
//			System.out.println("Effect started. startTime: " + startTime + " timeLast: " + timeLast);
		}
		if(resetCount()){
			startTime = now;
		}
		onUpdate(delta, world);
//		System.out.println("Effect updated. " + now + " percentDone: " + percentDone());
		if(startTime + timeLast < now){
			done = true;
			onEnd(world);
//			System.out.println("Effect ended");
		}
	}

	/**
	 * Called in TimedEffect's update. If you want to override.
	 * @return if true, then the start time will be reset, by default false
	 */
	protected boolean resetCount(){
		return false;
	}
	protected abstract void onStart(World world);
	protected abstract void onUpdate(float delta, World world);
	protected abstract void onEnd(World world);
}
