package me.retrodaredevil.controller;

/**
 * This class can be used to store and access the standard layout on most modern controllers. Note that when
 * implementing, you can return null if the controller doesn't support something.
 */
public abstract class StandardControllerInput extends ControllerInput {

	public abstract JoystickPart dPad();
	public abstract JoystickPart leftJoy();
	public abstract JoystickPart rightJoy();

	public abstract InputPart leftStick();
	public abstract InputPart rightStick();

	public abstract InputPart start();
	public abstract InputPart select();

	public abstract InputPart faceUp();
	public abstract InputPart faceDown();
	public abstract InputPart faceLeft();
	public abstract InputPart faceRight();

	public abstract InputPart leftBumper();
	public abstract InputPart rightBumper();
	public abstract InputPart leftTrigger();
	public abstract InputPart rightTrigger();
}
