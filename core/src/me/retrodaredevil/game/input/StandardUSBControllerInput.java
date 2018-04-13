package me.retrodaredevil.game.input;

import com.badlogic.gdx.controllers.Controller;
import me.retrodaredevil.input.*;

import java.util.Arrays;
import java.util.Collection;

public class StandardUSBControllerInput extends StandardControllerInput {
	private final SingleInput leftXAxis, leftYAxis,
			rightXAxis, rightYAxis,
			dXAxis, dYAxis;

	private final SingleInput start, select,
			faceUp, faceDown, faceLeft, faceRight,
			leftBumper, rightBumper,
			leftTrigger, rightTrigger;

	private final JoystickInput dPad, leftJoy, rightJoy;
	private final Joysticks joysticks;

	private final Collection<ControllerPart> parts;

	public StandardUSBControllerInput(Controller controller){
		leftXAxis = new ControllerSingleInput(controller, SingleInput.AxisType.FULL_ANALOG, 0);
		leftYAxis = new ControllerSingleInput(controller, SingleInput.AxisType.FULL_ANALOG, 1, true);

		rightXAxis = new ControllerSingleInput(controller, SingleInput.AxisType.FULL_ANALOG, 2);
		rightYAxis = new ControllerSingleInput(controller, SingleInput.AxisType.FULL_ANALOG, 3, true);

		dXAxis = new ControllerSingleInput(controller, SingleInput.AxisType.FULL_ANALOG, 4);
		dYAxis = new ControllerSingleInput(controller, SingleInput.AxisType.FULL_ANALOG, 5, true);

		start = new ControllerSingleInput(controller, SingleInput.AxisType.DIGITAL, 9);
		select = new ControllerSingleInput(controller, SingleInput.AxisType.DIGITAL, 8);

		faceUp = new ControllerSingleInput(controller, SingleInput.AxisType.DIGITAL, 0);
		faceDown = new ControllerSingleInput(controller, SingleInput.AxisType.DIGITAL, 2);
		faceLeft = new ControllerSingleInput(controller, SingleInput.AxisType.DIGITAL, 3);
		faceRight = new ControllerSingleInput(controller, SingleInput.AxisType.DIGITAL, 1);

		leftBumper = new ControllerSingleInput(controller, SingleInput.AxisType.DIGITAL, 4);
		rightBumper = new ControllerSingleInput(controller, SingleInput.AxisType.DIGITAL, 5);

		leftTrigger = new ControllerSingleInput(controller, SingleInput.AxisType.DIGITAL, 6);
		rightTrigger = new ControllerSingleInput(controller, SingleInput.AxisType.DIGITAL, 7);

		dPad = new TwoAxisJoystickInput(dXAxis, dYAxis);
		leftJoy = new TwoAxisJoystickInput(leftXAxis, leftYAxis);
		rightJoy = new TwoAxisJoystickInput(rightXAxis, rightYAxis);

		joysticks = new Joysticks(dPad, leftJoy, rightJoy);

		parts = Arrays.asList(leftXAxis, leftYAxis, rightXAxis, rightYAxis,
				start, select, faceUp, faceDown, faceLeft, faceRight,
				leftBumper, rightBumper, leftTrigger, rightTrigger,
				dPad, leftJoy, rightJoy);
	}

	@Override
	public JoystickInput dPad() {
		return dPad;
	}

	@Override
	public JoystickInput leftJoy() {
		return leftJoy;
	}

	@Override
	public JoystickInput rightJoy() {
		return rightJoy;
	}

	@Override
	public SingleInput start() {
		return start;
	}

	@Override
	public SingleInput select() {
		return select;
	}

	@Override
	public SingleInput faceUp() {
		return faceUp;
	}

	@Override
	public SingleInput faceDown() {
		return faceDown;
	}

	@Override
	public SingleInput faceLeft() {
		return faceLeft;
	}

	@Override
	public SingleInput faceRight() {
		return faceRight;
	}

	@Override
	public SingleInput leftBumper() {
		return leftBumper;
	}

	@Override
	public SingleInput rightBumper() {
		return rightBumper;
	}

	@Override
	public SingleInput leftTrigger() {
		return leftTrigger;
	}

	@Override
	public SingleInput rightTrigger() {
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
