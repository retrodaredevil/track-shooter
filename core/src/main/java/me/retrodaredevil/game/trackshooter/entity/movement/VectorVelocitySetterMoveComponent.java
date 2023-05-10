package me.retrodaredevil.game.trackshooter.entity.movement;

import me.retrodaredevil.game.trackshooter.util.VectorVelocitySetter;

public interface VectorVelocitySetterMoveComponent extends VectorVelocityMoveComponent {
	VectorVelocitySetter getVectorVelocitySetter();
}
