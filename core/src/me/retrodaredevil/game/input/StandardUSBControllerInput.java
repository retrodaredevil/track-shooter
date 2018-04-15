package me.retrodaredevil.game.input;

import com.badlogic.gdx.controllers.Controller;
import me.retrodaredevil.input.*;

import java.util.Arrays;
import java.util.Collection;

public class StandardUSBControllerInput extends StandardControllerInput {
	private final InputPart leftXAxis, leftYAxis,
			rightXAxis, rightYAxis,
			dXAxis, dYAxis;

	private final InputPart start, select,
			faceUp, faceDown, faceLeft, faceRight,
			leftBumper, rightBumper,
			leftTrigger, rightTrigger;

	private final JoystickPart dPad, leftJoy, rightJoy;
	private final Joysticks joysticks;

	private final Collection<ControllerPart> parts;

	public StandardUSBControllerInput(Controller controller){
		leftXAxis = new ControllerInputPart(controller, InputPart.AxisType.FULL_ANALOG, 0);
		leftYAxis = new ControllerInputPart(controller, InputPart.AxisType.FULL_ANALOG, 1, true);

		rightXAxis = new ControllerInputPart(controller, InputPart.AxisType.FULL_ANALOG, 2);
		rightYAxis = new ControllerInputPart(controller, InputPart.AxisType.FULL_ANALOG, 3, true);

		dXAxis = new ControllerInputPart(controller, InputPart.AxisType.FULL_ANALOG, 4);
		dYAxis = new ControllerInputPart(controller, InputPart.AxisType.FULL_ANALOG, 5, true);

		start = new ControllerInputPart(controller, InputPart.AxisType.DIGITAL, 9);
		select = new ControllerInputPart(controller, InputPart.AxisType.DIGITAL, 8);

		faceUp = new ControllerInputPart(controller, InputPart.AxisType.DIGITAL, 0);
		faceDown = new ControllerInputPart(controller, InputPart.AxisType.DIGITAL, 2);
		faceLeft = new ControllerInputPart(controller, InputPart.AxisType.DIGITAL, 3);
		faceRight = new ControllerInputPart(controller, InputPart.AxisType.DIGITAL, 1);

		leftBumper = new ControllerInputPart(controller, InputPart.AxisType.DIGITAL, 4);
		rightBumper = new ControllerInputPart(controller, InputPart.AxisType.DIGITAL, 5);

		leftTrigger = new ControllerInputPart(controller, InputPart.AxisType.DIGITAL, 6);
		rightTrigger = new ControllerInputPart(controller, InputPart.AxisType.DIGITAL, 7);

		dPad = new TwoAxisJoystickPart(dXAxis, dYAxis);
		leftJoy = new TwoAxisJoystickPart(leftXAxis, leftYAxis);
		rightJoy = new TwoAxisJoystickPart(rightXAxis, rightYAxis);

		joysticks = new Joysticks(dPad, leftJoy, rightJoy);

		parts = Arrays.asList(leftXAxis, leftYAxis, rightXAxis, rightYAxis,
				start, select, faceUp, faceDown, faceLeft, faceRight,
				leftBumper, rightBumper, leftTrigger, rightTrigger,
				dPad, leftJoy, rightJoy);
	}

	@Override
	public JoystickPart dPad() {
		return dPad;
	}

	@Override
	public JoystickPart leftJoy() {
		return leftJoy;
	}

	@Override
	public JoystickPart rightJoy() {
		return rightJoy;
	}

	@Override
	public InputPart start() {
		return start;
	}

	@Override
	public InputPart select() {
		return select;
	}

	@Override
	public InputPart faceUp() {
		return faceUp;
	}

	@Override
	public InputPart faceDown() {
		return faceDown;
	}

	@Override
	public InputPart faceLeft() {
		return faceLeft;
	}

	@Override
	public InputPart faceRight() {
		return faceRight;
	}

	@Override
	public InputPart leftBumper() {
		return leftBumper;
	}

	@Override
	public InputPart rightBumper() {
		return rightBumper;
	}

	@Override
	public InputPart leftTrigger() {
		return leftTrigger;
	}

	@Override
	public InputPart rightTrigger() {
		return rightTrigger;
	}

	@Override
	public Joysticks getJoysticks() {
		return joysticks;
	}

	@Override
	public Collection<ControllerPart> getAllParts() {
		return parts;
	}

	@Override
	public boolean isConnected(ControllerManager manager) {
		return true; // TODO we may be able to return something else here
	}
}
