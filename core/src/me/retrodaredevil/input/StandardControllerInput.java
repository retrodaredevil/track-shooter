package me.retrodaredevil.input;

/**
 * This class can be used to store and access the standard layout on most modern controllers. Note that when
 * implementing, you can return null if the controller doesn't support something.
 */
public abstract class StandardControllerInput extends ControllerInput {

	public abstract JoystickInput dPad();
	public abstract JoystickInput leftJoy();
	public abstract JoystickInput rightJoy();

	public abstract SingleInput start();
	public abstract SingleInput select();

	public abstract SingleInput faceUp();
	public abstract SingleInput faceDown();
	public abstract SingleInput faceLeft();
	public abstract SingleInput faceRight();

	public abstract SingleInput leftBumper();
	public abstract SingleInput rightBumper();
	public abstract SingleInput leftTrigger();
	public abstract SingleInput rightTrigger();
}
