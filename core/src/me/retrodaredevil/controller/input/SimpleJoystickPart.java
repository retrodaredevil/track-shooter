package me.retrodaredevil.controller.input;

import me.retrodaredevil.controller.SimpleControllerPart;

import static java.lang.Math.abs;
import static java.lang.Math.atan;
import static java.lang.Math.atan2;
import static java.lang.Math.cos;
import static java.lang.Math.hypot;
import static java.lang.Math.toDegrees;
import static java.lang.Math.toRadians;

public abstract class SimpleJoystickPart extends SimpleControllerPart implements JoystickPart{
	private JoystickType type;

	// these 3 variables set to null every frame or calculated every frame
	private Double angleDegrees;
	private Double magnitude;
	private Double correctMagnitude;

	public SimpleJoystickPart(JoystickType type){
		this.type = type;
	}

	@Override
	public JoystickType getJoystickType(){
		return type;
	}

	@Override
	protected void onUpdate() {
		super.onUpdate();
		angleDegrees = null;
		magnitude = null;
		correctMagnitude = null;
	}

	@Override
	public void onSecondUpdate() {
		super.onSecondUpdate();
		if(this.config.cacheAngleAndMagnitudeInUpdate){
			double x = getX();
			double y = getY();
			angleDegrees = calculateAngle(x, y);
			magnitude = calculateMagnitude(x, y);
			correctMagnitude = calculateCorrectMagnitude(magnitude, angleDegrees);
		}
	}

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
	protected double calculateCorrectMagnitude(double magnitude, double angleDegrees){
		double r = getJoystickType().shouldScale() ? magnitude * getScaled(angleDegrees) : magnitude;
		if(r >= .999999999999999){ // if another 9 is added, will start to produce incorrect results
			r = 1;
		}
		return r;
	}

	/**
	 * The returned value will be 0 (inclusive) to 360 (exclusive)
	 * @return The angle the joystick is pointing in degrees. If getMagnitude() is 0, then this value may not be accurate
	 */
	@Override
	public double getAngle(){
		if(angleDegrees == null){
			angleDegrees = calculateAngle(getX(), getY());
		}
		return angleDegrees;
	}
	/**
	 * @return The magnitude of the joystick
	 */
	@Override
	public double getMagnitude(){
		if(magnitude == null){
			magnitude = calculateMagnitude(getX(), getY());
		}
		return magnitude;
	}

	@Override
	public double getCorrectMagnitude() {
		if(correctMagnitude == null){
			correctMagnitude = calculateCorrectMagnitude(getMagnitude(), getAngle());
		}
		return correctMagnitude;
	}

	/**
	 * @return isXDeadzone() && isYDeadzone();
	 */
	@Override
	public boolean isDeadzone(){
		return isXDeadzone() && isYDeadzone();
	}

	/**
	 * This method is a util method that assumes that x is -1 to 1 and y is -1 to 1 meaning that an (x, y) value of
	 * (1, 1). This method scales the passed x and y values accordingly so that when something like (1, 1) is passed,
	 * it is scaled down to a magnitude of 1 so it is then (1/sqrt(2), 1/sqrt(2)). When (1, 0) is passed, it is not
	 * scaled at all
	 * <p>
	 * <p>
	 * How this works: Takes the cosine of the smallest distance to the closest 45 degree angle. Ex: 45, 135, etc
	 *
	 * @param angleDegrees The angle of the joystick in degrees
	 * @return The number you should scale (multiply) x and y by (or the magnitude by)
	 */
	public static double getScaled(final double angleDegrees){
	    // another option is 1/(cos(angle)+sin(angle)) from: https://www.reddit.com/r/gamedev/comments/4urddr/how_to_deal_with_joysticks_that_map_out_squares/d5srwz4
        double angle = angleDegrees;
//        System.out.println(angle);

        angle %= 90;
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

}
