package me.retrodaredevil.game.trackshooter.entity.movement;

import me.retrodaredevil.game.trackshooter.Updateable;

/**
 * This can have many implementations and is used for being able to call methods that will move an Entity.
 *
 * When update() is called, this should move the entity inside update, it should not use any external input that it
 * did not receive from one of its methods. Ex: Don't take input from a controller directly and have a method that
 * allows you to start going to a target location instead of choosing that target location
 */
public interface MoveComponent extends Updateable {


	/**
	 * @return Will return the MoveComponent to replace this current one, or will return this. Never null.
	 */
	MoveComponent getNextComponent();
}
