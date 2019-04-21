package me.retrodaredevil.game.trackshooter.effect;

import me.retrodaredevil.game.trackshooter.world.World;

public abstract class TimedEffect implements Effect {
	protected final World world;
	private final long timeLast; // the amount of time for the effect to last in ms
	private Long startTime = null;
	private boolean done = false;

	protected TimedEffect(World world, long last){
		this.world = world;
		this.timeLast = last;
	}

	@Override
	public boolean isDone() {
		return done;
	}

	@Override
	public float percentDone(World world) {
		if(startTime == null){
			return 1;
		}
		double r = (world.getTimeMillis() - startTime) / (double) timeLast;
		return (float) Math.min(1, r);
	}

	@Override
	public void update(float delta) {
		long now = world.getTimeMillis();
		if(startTime == null){
			startTime = now;
			onStart();
//			System.out.println("Effect started. startTime: " + startTime + " timeLast: " + timeLast);
		}
		if(resetCount()){
			startTime = now;
		}
		onUpdate(delta);
//		System.out.println("Effect updated. " + now + " percentDone: " + percentDone());
		if(startTime + timeLast < now){
			done = true;
			onEnd();
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
	protected abstract void onStart();
	protected abstract void onUpdate(float delta);
	protected abstract void onEnd();
}
