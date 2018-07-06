package me.retrodaredevil.controller.types;

import me.retrodaredevil.controller.ControllerInput;
import me.retrodaredevil.controller.input.InputPart;
import me.retrodaredevil.controller.input.JoystickPart;

/**
 * This class can be used to store and access the standard layout on most modern controllers. Note that when
 * implementing, you can return null if the controller doesn't support something.
 */
public interface StandardControllerInput extends ControllerInput {

	JoystickPart dPad();
	JoystickPart leftJoy();
	JoystickPart rightJoy();

	InputPart leftStick();
	InputPart rightStick();

	InputPart start();
	InputPart select();

	InputPart faceUp();
	InputPart faceDown();
	InputPart faceLeft();
	InputPart faceRight();

	InputPart leftBumper();
	InputPart rightBumper();
	InputPart leftTrigger();
	InputPart rightTrigger();
}
