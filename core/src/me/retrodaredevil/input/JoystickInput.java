package me.retrodaredevil.input;

import java.awt.geom.Point2D;

import static java.lang.Math.*;

public abstract class JoystickInput extends ControllerPart{
	private JoystickType type;

	protected double angleDegrees;
	protected double magnitude;

	public JoystickInput(JoystickType type){
		this.type = type;
	}

	public JoystickType getJoystickType(){
		return type;
	}

	@Override
	public void update(ControlConfig config) {
		super.update(config);
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
	 * @return The Y value of the joystick -1 to 1
	 */
	public abstract double getY();

	public static Point2D getScaled(double x, double y, Double angleRadians){
		final double radians45 = toRadians(45);
		final double radians90 = toRadians(90);
		final double radians180 = toRadians(180);
		final double radians360 = toRadians(360);

		double angle;
		if(angleRadians == null){
			angle = atan(y / x);
		} else {
			angle = angleRadians % radians360;
			angle = angle < 0 ? angle + radians360 : angle;

			if(angle > radians90){
				angle -= radians180;
			} else if(angle < -radians90){
				angle += radians180;
			}
		}
		// angle is between -90 and 90
		if(angle > radians45){
			angle = radians45 - (angle - radians45); // = 90 - angle
		} else if (angle < -radians45){
			angle = -radians90 - angle;
		}
//		angle = ((angle - 45) % 45) - 45;
		double scale = cos(angle);
		return new Point2D.Double(x * scale, y * scale);
	}

	public enum JoystickType{
		NORMAL, POV
	}
}
