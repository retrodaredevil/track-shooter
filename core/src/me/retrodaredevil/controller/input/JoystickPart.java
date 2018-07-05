package me.retrodaredevil.controller.input;

import me.retrodaredevil.controller.ControlConfig;
import me.retrodaredevil.controller.ControllerPart;

import static java.lang.Math.*;

public abstract class JoystickPart extends ControllerPart {
	private JoystickType type;

	protected Double angleDegrees;
	protected Double magnitude;

	public JoystickPart(JoystickType type){
		this.type = type;
	}

	public JoystickType getJoystickType(){
		return type;
	}

	/**
	 * NOTE: When calling super, make sure that if getX() or getY() is called, they are up to date
	 * <br/>
	 * When overriding, if there are SingleInputs as instance variables, their update should not be called unless
	 * they were created by this instance.
	 */
	@Override
	public void update(ControlConfig config) {
		super.update(config);
		if(config.cacheAngleAndMagnitudeInUpdate){
			double x = getX();
			double y = getY();
			angleDegrees = calculateAngle(x, y);
			magnitude = calculateMagnitude(x, y);
		} else {
			angleDegrees = null;
			magnitude = null;
		}
	}
	public abstract InputPart getXAxis();
	public abstract InputPart getYAxis();

	protected double calculateAngle(double x, double y){
		if(y == 0){
			return x >= 0 ? 0 : 180;
		} else if(x == 0){
			return y > 0 ? 90 : 270;
		} else if(abs(x) == abs(y)){
//			return x > 0 ? y > 0 ? 45 : 360 - 45 : y > 0 ? 90 + 45 : 180 + 45; you're welcome that I didn't use this
			if(x > 0){
				return y > 0 ? 45 : 360 - 45;
			} else {
				return y > 0 ? 90 + 45 : 180 + 45;
			}
		}
		double r = toDegrees(atan2(y, x));
		if(r < 0){
			r += 360;
		}
		return r;
	}
	protected double calculateMagnitude(double x, double y){
		return hypot(x, y);
	}

	/**
	 * The returned value will be 0 (inclusive) to 360 (exclusive)
	 * @return The angle the joystick is pointing in degrees. If getMagnitude() is 0, then this value may not be accurate
	 */
	public double getAngle(){
		if(angleDegrees == null){
			angleDegrees = calculateAngle(getX(), getY());
		}
		return angleDegrees;
	}
	/**
	 * @return The magnitude of the joystick
	 */
	public double getMagnitude(){
		if(magnitude == null){
			magnitude = calculateMagnitude(getX(), getY());
		}
		return magnitude;
	}

	/**
	 * When implementing: You should try to cache the value in update() for this so if this method is called 100 times
	 * per frame, it won't affect performance.
	 *
	 * @return The X value of the joystick -1 to 1. Or greater if getJoystickType() == MOUSE
	 */
	public abstract double getX();

	/**
	 * When implementing: You should try to cache the value in update() for this so if this method is called 100 times
	 * per frame, it won't affect performance.
	 *
	 * NOTE: When joystick is up, this is positive, when joystick is down, this is negative
	 *
	 * @return The Y value of the joystick -1 to 1 Or greater if getJoystickType() == MOUSE
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
	 * <p>
	 * <p>
	 * How this works: Takes the cosine of the smallest distance to the closest 45 degree angle. Ex: 45, 135, etc
	 *
	 * @param x The x value of the joystick
	 * @param y The y value of the joystick
	 * @param angleDegrees If the atan2 of y, x or atan of y/x has already been calculated, this will use that value
	 *                     instead of calculating it essentially increasing performance
	 * @return The number you should scale (multiply) x and y by (or the magnitude by)
	 */
	public static double getScaled(double x, double y, Double angleDegrees){
		if(x == 0 && y == 0){
			return 0;
		}

		double angle; // in degrees
		if(angleDegrees == null){
			angle = toDegrees(atan(y / x));
		} else {
			angle = angleDegrees % 90;
		}
		// angle is between -90 and 90
		if(angle < 0){
			angle = -angle;
		}
		// angle is between 0 and 90
		if(angle > 45){
			angle = 90 - angle;
		}
		return cos(toRadians(angle));
	}

	public enum JoystickType{
		NORMAL(true, false, true, true),
		POV(false, false, true, true),
		MOUSE(true, true, false, false);
		private final boolean analog;
		private final boolean rangeOver;
		private final boolean shouldScale;
		private final boolean shouldUseDelta;

		JoystickType(boolean analog, boolean rangeOver, boolean shouldScale, boolean shouldUseDelta){
			this.analog = analog;
			this.rangeOver = rangeOver;
			this.shouldScale = shouldScale;
			this.shouldUseDelta = shouldUseDelta;
		}

		public boolean isAnalog(){
			return analog;
		}

		/** @return returns true if either the abs of the x or the y can go over 1 */
		public boolean isRangeOver(){
			return rangeOver;
		}

		/**
		 * NOTE: For things like MOUSE, this will return false since it doesn't make sense to scale it.
		 * @return true if the magnitude of x and y can go over 1.
		 */
		public boolean shouldScale(){
			return shouldScale;
		}

		/**
		 *
		 * @return true if you should apply delta time to the value
		 */
		public boolean shouldUseDelta(){
			return shouldUseDelta;
		}


	}
}
