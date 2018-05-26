package me.retrodaredevil.game.trackshooter.entity;

/**
 * A type of entity that is able to be removed by calling a method allowing other places in the code to remove entities
 * that support it.
 */
public interface RemovableEntity extends Entity {
	/**
	 * Once this is called, it should make the next time shouldRemove() is called return true essentially removing this
	 */
	void setToRemove();
}
