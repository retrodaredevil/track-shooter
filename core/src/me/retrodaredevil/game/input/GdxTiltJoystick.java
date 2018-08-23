package me.retrodaredevil.game.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

import java.util.Collection;
import java.util.Collections;

import me.retrodaredevil.controller.input.InputPart;
import me.retrodaredevil.controller.input.JoystickAxisFollowerPart;
import me.retrodaredevil.controller.input.JoystickType;
import me.retrodaredevil.controller.input.SimpleJoystickPart;
import me.retrodaredevil.controller.options.ConfigurableControllerPart;
import me.retrodaredevil.controller.options.ControlOption;
import me.retrodaredevil.controller.options.OptionControllerPart;
import me.retrodaredevil.game.trackshooter.util.MathUtil;

public class GdxTiltJoystick extends SimpleJoystickPart implements OptionControllerPart, ConfigurableControllerPart {
	private int maxDegrees;
	private double x, y;

	private final InputPart xAxis = new JoystickAxisFollowerPart(this, false);
	private final InputPart yAxis = new JoystickAxisFollowerPart(this, true);

	private final Collection<ControlOption> controlOptions = Collections.singletonList(new ControlOption("Tilt Controller Angle",
				"How many degrees you have to tilt the controller until the magnitude of an axis is 1.",
				this));

	/**
	 *
	 * @param maxDegrees The amount of the degrees you have to tilt for either axis to reach max (1 or -1) corresponding to (20 or -20)
	 */
	public GdxTiltJoystick(Double maxDegrees) {
		super(new JoystickType(true, true, true, true), false, false);
		if(maxDegrees != null) {
			setOptionValue(maxDegrees);
		} else {
			setToDefaultOptionValue();
		}
	}
	public GdxTiltJoystick(double maxDegrees){
		this((Double) maxDegrees);
	}
	public GdxTiltJoystick(){
		this(null);
	}

	@Override
	public Collection<ControlOption> getControlOptions() {
		return controlOptions;
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
	private static double degreesToFullAnalog(float degrees, int maxDegrees){
		return MathUtil.minChange(degrees, 0, 360) / maxDegrees;
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

	@Override
	public double getMaxOptionValue() {
		return 70;
	}

	@Override
	public double getMinOptionValue() {
		return 5;
	}

	@Override
	public boolean isOptionAnalog() {
		return false;
	}

	@Override
	public double getOptionValue() {
		return maxDegrees;
	}

	@Override
	public void setOptionValue(double v) {
		this.maxDegrees = (int) v;
	}

	@Override
	public double getDefaultOptionValue() {
		return 15;
	}

	@Override
	public void setToDefaultOptionValue() {
		this.maxDegrees = 15;
	}
}
