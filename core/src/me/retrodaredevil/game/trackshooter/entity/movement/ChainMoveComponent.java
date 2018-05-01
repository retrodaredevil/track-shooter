package me.retrodaredevil.game.trackshooter.entity.movement;

public interface ChainMoveComponent extends MoveComponent {

	/**
	 * Should set the next MoveComponent to be returned when getNextComponent() is called
	 * @param nextComponent The next MoveComponent
	 * @return nextComponent for chaining
	 */
	<T extends MoveComponent> T setNextComponent(T nextComponent);
}
