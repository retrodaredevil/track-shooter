package me.retrodaredevil.game.trackshooter.entity.movement;

import me.retrodaredevil.game.trackshooter.world.World;

/**
 * A class which simplifies MoveComponents, setting next components, ending, starting etc. When overriding, you should
 * never override update(), start, or end() always use onUpdate(), onStart(), onEnd(). You may optionally override
 * isDone(), but should still call super.
 */
public abstract class SimpleMoveComponent implements MoveComponent {

	private MoveComponent nextComponent;
	private final boolean canHaveNext; // if false, isDone() should not return true
	private final boolean canRecycle;

	protected boolean done = false; // set back to false when starting
	private boolean active = false;
	private boolean oneWayDone = false; // never set back to false, only set to true when end() called

	/**
	 *
	 * @param nextComponent The next MoveComponent or null. (Only not null if canHaveNext == true)
	 * @param canHaveNext If true, this instance can have a "next component" and can be "done." If false, isDone()
	 *                    should not return true and setNextComponent() will result in an exception
	 * @param canRecycle true if this should be allowed to end and start again. If false, this will throw an exception
	 *                   if end() is called and update() is called again
	 */
	protected SimpleMoveComponent(MoveComponent nextComponent, boolean canHaveNext, boolean canRecycle){
		this.nextComponent = nextComponent;
		this.canHaveNext = canHaveNext;
		this.canRecycle = canRecycle;
		assert nextComponent == null || canHaveNext;
	}

	@Override
	public MoveComponent getNextComponent() {
		return nextComponent;
	}

	@Override
	public <T extends MoveComponent> T setNextComponent(T nextComponent) {
		if(!canHaveNext){
			throw new UnsupportedOperationException(getClass().getSimpleName() + " does not support a next MoveComponent");
		}
		this.nextComponent = nextComponent;
		return nextComponent;
	}

	@Override
	public boolean canHaveNext() {
		return canHaveNext;
	}

	@Override
	public void end() {
		active = false;
		oneWayDone = true;
		onEnd();
	}

	/**
	 *
	 * @return returns true if this ended using isDone()
	 */
	protected boolean endedPeacefully(){
		if(!canHaveNext){
			throw new IllegalStateException("This cannot have a next MoveComponent, so it cannot be end.");
		}
		if(active){
			throw new IllegalStateException("You cannot call endedPeacefully() when this is active.");
		}
		return done;
	}

	/**
	 * For subclass implementations: When overriding, it is recommended that you set done to whatever you would like
	 * then return super.isDone()
	 * @return Whether or not this MoveComponent is done
	 */
	@Override
	public boolean isDone() {
		if(!canHaveNext){
			return false;
		}
		return done;
	}

	public boolean isActive(){
		return active;
	}

	@Override
	public void update(float delta, World world) {
		if(!active){
			if(!canRecycle && oneWayDone){
				throw new IllegalStateException(getClass().getSimpleName() + " does not support recycling");
			}
			active = true;
			done = false;
			onStart(world);
		}
		onUpdate(delta, world);
	}

	/**
	 * Called when the first #update() is called (assuming super is called)
	 * @param world
	 */
	protected abstract void onStart(World world);
	protected abstract void onEnd();
	protected abstract void onUpdate(float delta, World world);
}
