package me.retrodaredevil.game.trackshooter.entity.movement;

import me.retrodaredevil.game.trackshooter.Updateable;
import me.retrodaredevil.game.trackshooter.world.World;

/**
 * This can have many implementations and is used for being able to call methods that will move an Entity.
 * <p>
 * When update() is called, this should move the entity inside update, it should not use any external input that it
 * did not receive from one of its methods. Ex: Don't take input from a controller directly and have a method that
 * allows you to start going to a target location instead of choosing that target location
 * <p>
 * Note that if start() then x number of update() are called then end() is called, this should still be able to be
 * reused (stop using the MoveComponent, then start again)
 */
public interface MoveComponent extends Updateable {


	/**
	 * @return Will return the next MoveComponent or null if there is no next MoveComponent
	 */
	MoveComponent getNextComponent();
	<T extends MoveComponent> T setNextComponent(T nextComponent);

	/**
	 * If this returns true, then it is safe to call #setNextComponent()
	 * @return true if this is able to have a "next" component
	 */
	boolean canHaveNext();

	/**
	 * Called after isDone() returns true, or when something else caused this to stop
	 * SHOULD NOT be called just because you want to end this MoveComponent and move onto the next one. However, if you
	 * are going to force this one to end (for whatever reason), this should be called
	 */
	void end();

	/**
	 * Should return true whenever this MoveComponent is done
	 * @return true if the MoveComponent is done, false otherwise
	 */
	boolean isDone();
}
