package me.retrodaredevil.game.trackshooter.entity.movement;

import me.retrodaredevil.game.trackshooter.util.VelocitySetter;

public interface TravelVelocitySetterMoveComponent extends TravelVelocityMoveComponent {

	/**
	 * NOTE: When calling methods on the returned object, you should not take into account
	 * things such as SpeedEffects applied to the entity. The implementation of this class should
	 * take that into account
	 */
	VelocitySetter getTravelVelocitySetter();

}
