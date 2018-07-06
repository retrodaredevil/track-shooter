package me.retrodaredevil.controller.input;

import me.retrodaredevil.controller.ControllerPart;

/**
 * Represents a ControllerPart where its input is an angle in degrees.
 * <br/>
 * Can have numerous uses such as a steering wheel, gyro, etc.
 */
public interface AnglePart extends ControllerPart {
	double getAngle();
}
