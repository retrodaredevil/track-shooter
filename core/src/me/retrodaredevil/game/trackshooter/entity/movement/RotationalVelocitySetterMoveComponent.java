package me.retrodaredevil.game.trackshooter.entity.movement;

import me.retrodaredevil.game.trackshooter.util.VelocitySetter;

public interface RotationalVelocitySetterMoveComponent extends RotationalVelocityMoveComponent {
	VelocitySetter getRotationalVelocitySetter();
}
