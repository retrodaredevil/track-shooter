package me.retrodaredevil.game.trackshooter.entity.movement;

public interface TravelVelocityMoveComponent {
	/**
	 * @return The velocity of the Entity in units/second. This should also take into account SpeedEffects on the Entity
	 */
	float getTravelVelocity();
}
