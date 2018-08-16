package me.retrodaredevil.controller.types;

import me.retrodaredevil.controller.input.InputPart;

/**
 * A flight joystick that has 6 extra buttons in a 2x3 grid
 * <p>
 * The most common use for this would be for a Logitech Extreme 3D Pro Joystick
 */
public interface ExtremeFlightJoystickControllerInput extends FlightJoystickControllerInput {
	InputPart getGridUpperLeft();
	InputPart getGridUpperRight();
	InputPart getGridMiddleLeft();
	InputPart getGridMiddleRight();
	InputPart getGridLowerLeft();
	InputPart getGridLowerRight();
}
