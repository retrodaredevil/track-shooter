package me.retrodaredevil.game.trackshooter.input;

import com.badlogic.gdx.controllers.Controller;

import me.retrodaredevil.controller.SimpleControllerInput;
import me.retrodaredevil.controller.input.AxisType;
import me.retrodaredevil.controller.input.InputPart;
import me.retrodaredevil.controller.input.JoystickPart;
import me.retrodaredevil.controller.input.TwoAxisJoystickPart;
import me.retrodaredevil.controller.output.ControllerRumble;
import me.retrodaredevil.controller.types.LogitechAttack3JoystickControllerInput;
import me.retrodaredevil.controller.types.RumbleCapableController;
import me.retrodaredevil.game.trackshooter.input.implementations.ControllerInputPart;
import me.retrodaredevil.game.trackshooter.input.implementations.GdxControllerRumble;
import me.retrodaredevil.game.trackshooter.util.Util;

@Deprecated
public class StandardAttackJoystickControllerInput extends SimpleControllerInput implements LogitechAttack3JoystickControllerInput, RumbleCapableController {

	private final Controller controller;

	private final InputPart trigger;
	private final InputPart leftUpper, leftLower, centerLeft, centerRight, rightUpper, rightLower;
	private final InputPart thumbUpper, thumbLower, thumbLeft, thumbRight;
	private final InputPart slider;
	private final JoystickPart joystick;
	private final ControllerRumble rumble;

	public StandardAttackJoystickControllerInput(Controller controller){
		this.controller = controller;

		trigger = new ControllerInputPart(controller, AxisType.DIGITAL, 0);
		thumbLower = new ControllerInputPart(controller, AxisType.DIGITAL, 1);
		thumbUpper = new ControllerInputPart(controller, AxisType.DIGITAL, 2);
		thumbLeft = new ControllerInputPart(controller, AxisType.DIGITAL, 3);
		thumbRight = new ControllerInputPart(controller, AxisType.DIGITAL, 4);

		leftUpper = new ControllerInputPart(controller, AxisType.DIGITAL, 5);
		leftLower = new ControllerInputPart(controller, AxisType.DIGITAL, 6);

		centerLeft = new ControllerInputPart(controller, AxisType.DIGITAL, 7);
		centerRight = new ControllerInputPart(controller, AxisType.DIGITAL, 8);

		rightLower = new ControllerInputPart(controller, AxisType.DIGITAL, 9);
		rightUpper = new ControllerInputPart(controller, AxisType.DIGITAL, 10);

		joystick = new TwoAxisJoystickPart(
				new ControllerInputPart(controller, AxisType.FULL_ANALOG, 0),
				new ControllerInputPart(controller, AxisType.FULL_ANALOG, 1, true)
		);
		slider = new ControllerInputPart(controller, AxisType.FULL_ANALOG, 2, true);
		rumble = new GdxControllerRumble(controller);

		addChildren(false, false, trigger, thumbLower, thumbUpper, thumbLeft, thumbRight,
				leftUpper, leftLower, centerLeft, centerRight, rightLower, rightUpper, joystick, slider, rumble);
	}

	@Override
	public ControllerRumble getRumble() {
		return rumble;
	}

	@Override
	public boolean isConnected() {
        return Util.isControllerConnected(controller);
	}

	@Override
	public InputPart getLeftUpper() {
        return leftUpper;
	}

	@Override
	public InputPart getLeftLower() {
        return leftLower;
	}

	@Override
	public InputPart getCenterLeft() {
        return centerLeft;
	}

	@Override
	public InputPart getCenterRight() {
        return centerRight;
	}

	@Override
	public InputPart getRightUpper() {
        return rightUpper;
	}

	@Override
	public InputPart getRightLower() {
        return rightLower;
	}

	@Override
	public InputPart getThumbUpper() {
        return thumbUpper;
	}

	@Override
	public InputPart getThumbLower() {
        return thumbLower;
	}

	@Override
	public InputPart getThumbLeft() {
        return thumbLeft;
	}

	@Override
	public InputPart getThumbRight() {
        return thumbRight;
	}

	@Override
	public JoystickPart getMainJoystick() {
        return joystick;
	}

	@Override
	public InputPart getTrigger() {
        return trigger;
	}

	@Override
	public InputPart getSlider() {
        return slider;
	}
}
