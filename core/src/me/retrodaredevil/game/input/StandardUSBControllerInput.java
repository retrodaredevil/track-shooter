package me.retrodaredevil.game.input;

import com.badlogic.gdx.controllers.Controller;
import me.retrodaredevil.input.*;

import java.util.Arrays;
import java.util.Collection;

public class StandardUSBControllerInput extends StandardControllerInput {

	private final InputPart start, select,
			faceUp, faceDown, faceLeft, faceRight,
			leftBumper, rightBumper,
			leftTrigger, rightTrigger,
			leftStick, rightStick;

	private final JoystickPart dPad, leftJoy, rightJoy;
	private final Joysticks joysticks;

	private final Collection<ControllerPart> parts;

	public StandardUSBControllerInput(Controller controller){
		InputPart leftXAxis = new ControllerInputPart(controller, InputPart.AxisType.FULL_ANALOG, 0);
		InputPart leftYAxis = new ControllerInputPart(controller, InputPart.AxisType.FULL_ANALOG, 1, true);

		InputPart rightXAxis = new ControllerInputPart(controller, InputPart.AxisType.FULL_ANALOG, 2);
		InputPart rightYAxis = new ControllerInputPart(controller, InputPart.AxisType.FULL_ANALOG, 3, true);

		InputPart dXAxis = new ControllerInputPart(controller, InputPart.AxisType.FULL_ANALOG, 4);
		InputPart dYAxis = new ControllerInputPart(controller, InputPart.AxisType.FULL_ANALOG, 5, true);

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

		leftStick = new ControllerInputPart(controller, InputPart.AxisType.DIGITAL, 10);
		rightStick = new ControllerInputPart(controller, InputPart.AxisType.DIGITAL, 11);

		dPad = new TwoAxisJoystickPart(dXAxis, dYAxis);
		leftJoy = new TwoAxisJoystickPart(leftXAxis, leftYAxis);
		rightJoy = new TwoAxisJoystickPart(rightXAxis, rightYAxis);

		joysticks = new Joysticks(dPad, leftJoy, rightJoy);

		parts = Arrays.asList(leftXAxis, leftYAxis, rightXAxis, rightYAxis,
				start, select, faceUp, faceDown, faceLeft, faceRight,
				leftBumper, rightBumper, leftTrigger, rightTrigger,
				leftStick, rightStick,
				dPad, leftJoy, rightJoy);

		// the axises already have parents and we don't want to change them
		setParentsToThis(parts, false, true);

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
	public InputPart leftStick() {
		return leftStick;
	}

	@Override
	public InputPart rightStick() {
		return rightStick;
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
