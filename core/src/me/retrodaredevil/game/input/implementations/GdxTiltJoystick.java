package me.retrodaredevil.game.input.implementations;

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
import me.retrodaredevil.controller.options.OptionValue;
import me.retrodaredevil.controller.options.OptionValues;
import me.retrodaredevil.game.trackshooter.util.MathUtil;

public class GdxTiltJoystick extends SimpleJoystickPart implements ConfigurableControllerPart {
	private final OptionValue maxDegreesOption = OptionValues.createDigitalRangedOptionValue(5, 20, 15);
	private double x, y;

	private final InputPart xAxis = new JoystickAxisFollowerPart(this, false);
	private final InputPart yAxis = new JoystickAxisFollowerPart(this, true);

	private final Collection<ControlOption> controlOptions = Collections.singletonList(new ControlOption("Tilt Controller Angle",
				"How many degrees you have to tilt the controller until the magnitude of an axis is 1.", "controls.all.tilt",
				maxDegreesOption));

	/**
	 *
	 * @param maxDegrees The amount of the degrees you have to tilt for either axis to reach a magnitude of 1 in
	 */
	public GdxTiltJoystick(Integer maxDegrees) {
		super(new JoystickType(true, true, true, true), false, false);
		if(maxDegrees != null) {
			maxDegreesOption.setOptionValue(maxDegrees);
		}
	}
	public GdxTiltJoystick(int maxDegrees){
		this((Integer) maxDegrees);
	}
	public GdxTiltJoystick(){
		this(null);
	}

	@Override
	public Collection<? extends ControlOption> getControlOptions() {
		return controlOptions;
	}

	@Override
	public void onUpdate() {
		super.onUpdate();

		// works in landscape mode
		float gyroX = -Gdx.input.getPitch(); // up and down
		float gyroY = Gdx.input.getRoll(); // side to side

//		System.out.println(Gdx.input.getRotation());
		final float originalX = gyroX;
		final float originalY = gyroY;
		switch(Gdx.input.getRotation()){
			case 90:
				break; // landscape, good
			case 0: // portrait
				gyroX = originalY;
				gyroY = -originalX;

				break;
			case 270: // landscape flipped
				gyroX = -originalX;
				gyroY = - originalY;

				break;
			case 180: // portrait flipped
				gyroX = -originalY;
				gyroY = originalX;

				break;
			default:
				System.out.println("unknown rotation: " + Gdx.input.getRotation());
				break;
		}

		x = degreesToFullAnalog(gyroX, maxDegreesOption.getOptionValue());
		y = degreesToFullAnalog(gyroY, maxDegreesOption.getOptionValue());
//		System.out.println();
//		System.out.println("compass: " + Gdx.input.getAzimuth());
	}
	private static double degreesToFullAnalog(float degrees, double maxDegrees){
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

}
