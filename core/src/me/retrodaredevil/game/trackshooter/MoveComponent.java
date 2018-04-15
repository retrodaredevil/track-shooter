package me.retrodaredevil.game.trackshooter;

/**
 * This can have many implementations and is used for being able to call methods that will move an Entity.
 *
 * When update() is called, this should move the entity inside update, it should not use any external input that it
 * did not receive from one of its methods. Ex: Don't take input from a controller directly and have a method that
 * allows you to start going to a target location instead of choosing that target location
 */
public abstract class MoveComponent implements Updateable {
	private MoveComponent next = null;

	/**
	 * This is protected because only subclasses should choose if they want to use this. If this is something that makes
	 * sense, you can change the access modifier to public in your implementation
	 *
	 * @param next The next MoveComponent that should replace this one
	 */
	protected void setNextController(MoveComponent next){
		this.next = next;
	}

	/**
	 * @return Will return the MoveComponent to replace this current one, or will return this. Never null.
	 */
	public MoveComponent getNextComponent(){
		return next != null ? next : this;
	}
}
