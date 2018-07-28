package me.retrodaredevil.game.trackshooter.entity.movement;

import me.retrodaredevil.game.trackshooter.util.VelocitySetter;

public interface TravelVelocityMoveComponent extends MoveComponent{
	/**
	 * @return The velocity of the Entity in units/second. This should also take into account SpeedEffects on the Entity
	 */
	float getTravelVelocity();

}
