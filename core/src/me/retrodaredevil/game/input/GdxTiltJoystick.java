package me.retrodaredevil.game.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

import me.retrodaredevil.controller.input.InputPart;
import me.retrodaredevil.controller.input.JoystickAxisFollowerPart;
import me.retrodaredevil.controller.input.JoystickType;
import me.retrodaredevil.controller.input.SimpleJoystickPart;
import me.retrodaredevil.game.trackshooter.util.MathUtil;

public class GdxTiltJoystick extends SimpleJoystickPart {
	private final float maxDegrees;
	private double x, y;

	private final InputPart xAxis = new JoystickAxisFollowerPart(this, false);
	private final InputPart yAxis = new JoystickAxisFollowerPart(this, true);

	/**
	 *
	 * @param maxDegrees The amount of the degrees you have to tilt for either axis to reach max (1 or -1) corresponding to (20 or -20)
	 */
	public GdxTiltJoystick(float maxDegrees) {
		super(new JoystickType(true, true, true, true), false, false);
		this.maxDegrees = maxDegrees;
	}


	@Override
	public void onUpdate() {
		super.onUpdate();

		float gyroX = -Gdx.input.getPitch(); // up and down
		float gyroY = Gdx.input.getRoll(); // side to side
//		float gyroZ = Gdx.input.getAzimuth();

		x = degreesToFullAnalog(gyroX, maxDegrees);
		y = degreesToFullAnalog(gyroY, maxDegrees);
//		System.out.println();
//		System.out.println("compass: " + Gdx.input.getAzimuth());
	}
	private static double degreesToFullAnalog(float degrees, float maxDegrees){
		float r = MathUtil.minChange(degrees, 0, 360);
		if(r > 180){
			r -= 360;
		}
		return r / maxDegrees;
	}

	@Override
	public double getX() {
		return x;
	}

	@Override
	public double getY() {
		return y;
	}

	@Override
	public InputPart getXAxis() {
		return xAxis;
	}

	@Override
	public InputPart getYAxis() {
		return yAxis;
	}

	@Override
	public boolean isXDeadzone() {
		return Math.abs(x) <= config.getFullAnalogDeadzone();
	}

	@Override
	public boolean isYDeadzone() {
		return Math.abs(y) <= config.getFullAnalogDeadzone();
	}

	@Override
	public boolean isConnected() {
		return Gdx.input.isPeripheralAvailable(Input.Peripheral.Gyroscope);
	}
}
