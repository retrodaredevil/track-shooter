package me.retrodaredevil.game.trackshooter.input.implementations;

import com.badlogic.gdx.controllers.Controller;

import me.retrodaredevil.controller.implementations.ControllerPartCreator;
import me.retrodaredevil.controller.input.AxisType;
import me.retrodaredevil.controller.input.HighestPositionInputPart;
import me.retrodaredevil.controller.input.InputPart;
import me.retrodaredevil.controller.input.JoystickPart;
import me.retrodaredevil.controller.input.TwoAxisJoystickPart;
import me.retrodaredevil.controller.output.ControllerRumble;
import me.retrodaredevil.game.trackshooter.util.Util;

public class GdxControllerPartCreator implements ControllerPartCreator {
	private final Controller controller;

	public GdxControllerPartCreator(Controller controller){
		this.controller = controller;
	}

	@Override
	public InputPart createDigital(int code) {
        return new ControllerInputPart(controller, AxisType.DIGITAL, code);
	}

	@Override
	public JoystickPart createPOV(int povNumber, int xAxis, int yAxis) {
        return createPOV(povNumber);
	}

	@Override
	public JoystickPart createPOV(int povNumber) {
        return new ControllerPovJoystick(controller, povNumber);
	}

	@Override
	public JoystickPart createPOV(int xAxis, int yAxis) {
        return createJoystick(xAxis, yAxis);
	}

	@Override
	public JoystickPart createJoystick(int xAxis, int yAxis) {
        return new TwoAxisJoystickPart(
        		new ControllerInputPart(controller, AxisType.FULL_ANALOG, xAxis),
				new ControllerInputPart(controller, AxisType.FULL_ANALOG, yAxis, true)
		);
	}

	@Override
	public InputPart createFullAnalog(int axisCode) {
		return new ControllerInputPart(controller, AxisType.FULL_ANALOG, axisCode);
	}

	@Override
	public InputPart createAnalog(int axisCode) {
		return new ControllerInputPart(controller, AxisType.ANALOG, axisCode);
	}

	@Override
	public InputPart createFullAnalog(int axisCode, boolean isVertical) {
        return new ControllerInputPart(controller, AxisType.FULL_ANALOG, axisCode, isVertical);
	}

	@Override
	public InputPart createAnalog(int axisCode, boolean isVertical) {
		return new ControllerInputPart(controller, AxisType.ANALOG, axisCode, isVertical);
	}

	@Override
	public InputPart createAnalogTrigger(int axisCode) {
		return new ControllerInputPart(controller, AxisType.ANALOG, axisCode);
	}

	@Override
	public InputPart createTrigger(int digitalCode, int analogCode) {
        return new HighestPositionInputPart(
        		new ControllerInputPart(controller, AxisType.DIGITAL, digitalCode),
				new ControllerInputPart(controller, AxisType.ANALOG, analogCode)
		);
	}

	@Override
	public ControllerRumble createRumble() {
        return new GdxControllerRumble(controller);
	}

	@Override
	public boolean isConnected() {
        return Util.isControllerConnected(controller);
	}

	@Override
	public String getName() {
        return controller.getName();
	}
}
