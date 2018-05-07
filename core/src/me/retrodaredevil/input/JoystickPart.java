package me.retrodaredevil.input;

import static java.lang.Math.*;

public abstract class JoystickPart extends ControllerPart{
	private JoystickType type;

	protected double angleDegrees;
	protected double magnitude;

	public JoystickPart(JoystickType type){
		this.type = type;
	}

	public JoystickType getJoystickType(){
		return type;
	}

	/**
	 * When overriding, if there are SingleInputs as instance variables, their update should not be called unless
	 * they were created by this instance.
	 */
	@Override
	public void update(ControlConfig config) {
		super.update(config);
	}

	@Override
	public void lateUpdate() {
		super.lateUpdate();
		applyCalculations();
	}

	/**
	 * This method is called in update() if you override getAngle() and getMagnitude() and you do not use
	 * calculateAngle() or calculateMagnitude(), you should override this with a blank method so no unnecessary
	 * calculations are done
	 *
	 * You can optionally override this but it is not recommended
	 */
	protected void applyCalculations(){
		double x = getX();
		double y = getY();
		this.angleDegrees = calculateAngle(x, y);
		this.magnitude = calculateMagnitude(x, y);
	}
	/** used to calculate angleDegrees. You can optionally override this but it is not recommended */
	protected double calculateAngle(double x, double y){
		if(y == 0){
			return x >= 0 ? 0 : 180;
		} else if(x == 0){
			return y > 0 ? 90 : 270;
		} else if(abs(x) == 1 && abs(y) == 1){
			if(x == 1){
				return y == 1 ? 45 : 360 - 45;
			} else { // x must be -1
				return y == 1 ? 90 + 45 : 180 + 45;
			}
		}
		return toDegrees(atan2(y, x));
	}
	/** Used to calculate magnitude. You can optionally override this but it is not recommended */
	protected double calculateMagnitude(double x, double y){
		return hypot(x, y);
	}

	/**
	 * @return The angle the joystick is pointing. If getMagnitude() is 0, then this value may not be accurate
	 */
	public double getAngle(){
		return angleDegrees;
	}
	/**
	 * @return The magnitude of the joystick
	 */
	public double getMagnitude(){
		return magnitude;
	}

	/**
	 * When implementing: You should try to cache the value in update() for this so if this method is called 100 times
	 * per frame, it won't affect performance.
	 *
	 * @return The X value of the joystick -1 to 1
	 */
	public abstract double getX();

	/**
	 * When implementing: You should try to cache the value in update() for this so if this method is called 100 times
	 * per frame, it won't affect performance.
	 *
	 * NOTE: When joystick is up, this is positive, when joystick is down, this is negative
	 *
	 * @return The Y value of the joystick -1 to 1
	 */
	public abstract double getY();

	/**
	 * @return isXDeadzone() && isYDeadzone();
	 */
	public boolean isDeadzone(){
		return isXDeadzone() && isYDeadzone();
	}
	public abstract boolean isXDeadzone();
	public abstract boolean isYDeadzone();

	/**
	 * This method is a util method that assumes that x is -1 to 1 and y is -1 to 1 meaning that an (x, y) value of
	 * (1, 1). This method scales the passed x and y values accordingly so that when something like (1, 1) is passed,
	 * it is scaled down to a magnitude of 1 so it is then (1/sqrt(2), 1/sqrt(2)). When (1, 0) is passed, it is not
	 * scaled at all
	 *
	 *
	 * @param x The x value of the joystick
	 * @param y The y value of the joystick
	 * @param angleDegrees If the atan2 of y, x or atan of y/x has already been calculated, this will use that value
	 *                     instead of calculating it essentially increasing performance
	 * @return The number you should scale x and y by
	 */
	public static double getScaled(double x, double y, Double angleDegrees){
		if(x == 0 && y == 0){
			return 0;
		}

		double angle; // in degrees
		if(angleDegrees == null){
			angle = toDegrees(atan(y / x));
		} else {
			angle = angleDegrees % 360;
			angle = angle < 0 ? angle + 360 : angle;

			if(angle > 90){
				angle -= 180;
			} else if(angle < -90){
				angle += 180;
			}
		}
		// angle is between -90 and 90
		if(angle > 45){
			angle = 45 - (angle - 45); // = 90 - angle
		} else if (angle < -46){
			angle = -90 - angle;
		}
		return cos(toRadians(angle));
	}

	public enum JoystickType{
		NORMAL, POV
	}
}
