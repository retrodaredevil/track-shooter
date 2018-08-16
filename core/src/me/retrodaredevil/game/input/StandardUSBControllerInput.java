package me.retrodaredevil.game.input;

import com.badlogic.gdx.controllers.Controller;

import java.util.Arrays;

import me.retrodaredevil.controller.SimpleControllerInput;
import me.retrodaredevil.controller.input.AxisType;
import me.retrodaredevil.controller.input.InputPart;
import me.retrodaredevil.controller.input.JoystickPart;
import me.retrodaredevil.controller.input.TwoAxisJoystickPart;
import me.retrodaredevil.controller.output.ControllerRumble;
import me.retrodaredevil.controller.types.RumbleCapableController;
import me.retrodaredevil.controller.types.StandardControllerInput;
import me.retrodaredevil.game.trackshooter.util.Util;

public class StandardUSBControllerInput extends SimpleControllerInput implements StandardControllerInput, RumbleCapableController {

	private final Controller controller;
	private final InputPart start, select,
			yButton, aButton, xButton, bButton,
			leftBumper, rightBumper,
			leftTrigger, rightTrigger,
			leftStick, rightStick;

	private final JoystickPart dPad, leftJoy, rightJoy;

	private final ControllerRumble rumble;

	public StandardUSBControllerInput(Controller controller){
		System.out.println(controller.getName());
		this.controller = controller;

		InputPart leftXAxis = new ControllerInputPart(controller, AxisType.FULL_ANALOG, 0);
		InputPart leftYAxis = new ControllerInputPart(controller, AxisType.FULL_ANALOG, 1, true);

		InputPart rightXAxis = new ControllerInputPart(controller, AxisType.FULL_ANALOG, 2);
		InputPart rightYAxis = new ControllerInputPart(controller, AxisType.FULL_ANALOG, 3, true);

		// The reason we didn't use these two axi, is because when I tested it (on linux) it didn't work at all
//		InputPart dXAxis = new ControllerInputPart(controller, AxisType.FULL_DIGITAL, 4);
//		InputPart dYAxis = new ControllerInputPart(controller, AxisType.FULL_DIGITAL, 5, true);

		start = new ControllerInputPart(controller, AxisType.DIGITAL, 9);
		select = new ControllerInputPart(controller, AxisType.DIGITAL, 8);

		yButton = new ControllerInputPart(controller, AxisType.DIGITAL, 0);
		aButton = new ControllerInputPart(controller, AxisType.DIGITAL, 2);
		xButton = new ControllerInputPart(controller, AxisType.DIGITAL, 3);
		bButton = new ControllerInputPart(controller, AxisType.DIGITAL, 1);

		leftBumper = new ControllerInputPart(controller, AxisType.DIGITAL, 4);
		rightBumper = new ControllerInputPart(controller, AxisType.DIGITAL, 5);

		leftTrigger = new ControllerInputPart(controller, AxisType.DIGITAL, 6);
		rightTrigger = new ControllerInputPart(controller, AxisType.DIGITAL, 7);

		leftStick = new ControllerInputPart(controller, AxisType.DIGITAL, 10);
		rightStick = new ControllerInputPart(controller, AxisType.DIGITAL, 11);

//		getDPad = new TwoAxisJoystickPart(dXAxis, dYAxis, true);
		dPad = new ControllerPovJoystick(controller, 0);
		leftJoy = new TwoAxisJoystickPart(leftXAxis, leftYAxis);
		rightJoy = new TwoAxisJoystickPart(rightXAxis, rightYAxis);

		this.rumble = new GdxControllerRumble(controller);


		// the axises already have parents and we don't want to change them
		addChildren(Arrays.asList(
				start, select, yButton, aButton, xButton, bButton,
				leftBumper, rightBumper, leftTrigger, rightTrigger,
				leftStick, rightStick,
				dPad, leftJoy, rightJoy, rumble), false, false);

	}

	@Override
	public JoystickPart getDPad() {
		return dPad;
	}

	@Override
	public JoystickPart getLeftJoy() {
		return leftJoy;
	}

	@Override
	public JoystickPart getRightJoy() {
		return rightJoy;
	}

	@Override
	public InputPart getLeftStick() {
		return leftStick;
	}

	@Override
	public InputPart getRightStick() {
		return rightStick;
	}

	@Override
	public InputPart getStart() {
		return start;
	}

	@Override
	public InputPart getSelect() {
		return select;
	}

	// TODO correct physical locations
	@Override
	public InputPart getFaceUp() {
		return yButton;
	}
	@Override
	public InputPart getFaceDown() {
		return aButton;
	}
	@Override
	public InputPart getFaceLeft() {
		return xButton;
	}
	@Override
	public InputPart getFaceRight() {
		return bButton;
	}

	@Override
	public InputPart getAButton() {
		return aButton;
	}
	@Override
	public InputPart getBButton() {
		return bButton;
	}
	@Override
	public InputPart getXButton() {
		return xButton;
	}
	@Override
	public InputPart getYButton() {
		return yButton;
	}

	@Override
	public InputPart getLeftBumper() {
		return leftBumper;
	}

	@Override
	public InputPart getRightBumper() {
		return rightBumper;
	}

	@Override
	public InputPart getLeftTrigger() {
		return leftTrigger;
	}

	@Override
	public InputPart getRightTrigger() {
		return rightTrigger;
	}

	@Override
	public boolean isConnected() {
		return Util.isControllerConnected(controller);
	}

	@Override
	public ControllerRumble getRumble() {
		return rumble;
	}
}
