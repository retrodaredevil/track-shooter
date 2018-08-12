package me.retrodaredevil.game.trackshooter.entity.movement;

import me.retrodaredevil.game.trackshooter.Updateable;

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
	 * NOTE the return value is meant for chaining meaning DO NOT do this:
	 * <p>
	 * aMoveComponent = firstMoveComponent.setNextMoveComponent(secondMoveComponent).setNextComponent(thirdMoveComponent);
	 * <p>
	 * <p>
	 * Instead use:
	 * <br/>
	 * firstMoveComponent.setNextMoveComponent(secondMoveComponent).setNextComponent(thirdMoveComponent);
	 * <br/>
	 * aMoveComponent = firstMoveComponent;
	 * <p>
	 * NOTE: This should and will be called after end() is called
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
	 * Called after isDone() returns true, or when something else caused this to stop.
	 * If something else causes this to stop, you should still call this.
	 */
	void end();

	/**
	 * Should return true whenever this MoveComponent is done. If this returns true, it is likely
	 * that end() will be called followed by getNextComponent(), however, that may not always be the case.. immediately.
	 * @return true if the MoveComponent is done, false otherwise
	 */
	boolean isDone();
	boolean isActive();
}
