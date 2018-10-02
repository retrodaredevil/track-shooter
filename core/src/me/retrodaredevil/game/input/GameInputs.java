package me.retrodaredevil.game.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;

import java.util.Arrays;
import java.util.Collections;

import me.retrodaredevil.controller.input.DummyInputPart;
import me.retrodaredevil.controller.input.HighestPositionInputPart;
import me.retrodaredevil.controller.input.InputPart;
import me.retrodaredevil.controller.input.JoystickPart;
import me.retrodaredevil.controller.input.References;
import me.retrodaredevil.controller.input.TwoAxisJoystickPart;
import me.retrodaredevil.controller.options.ConfigurableControllerPart;
import me.retrodaredevil.controller.options.ControlOption;
import me.retrodaredevil.controller.options.OptionTracker;
import me.retrodaredevil.controller.options.OptionValue;
import me.retrodaredevil.controller.options.OptionValues;
import me.retrodaredevil.controller.output.ControllerRumble;
import me.retrodaredevil.game.input.implementations.GdxMouseAxis;
import me.retrodaredevil.game.input.implementations.GdxRumble;
import me.retrodaredevil.game.input.implementations.GdxScreenTouchButton;
import me.retrodaredevil.game.input.implementations.GdxShakeButton;
import me.retrodaredevil.game.input.implementations.GdxTiltJoystick;
import me.retrodaredevil.game.input.implementations.GdxTouchpadJoystick;
import me.retrodaredevil.game.input.implementations.KeyInputPart;
import me.retrodaredevil.game.input.implementations.ReleaseButtonPress;
import me.retrodaredevil.game.trackshooter.render.RenderParts;

public final class GameInputs {
	private GameInputs(){}

	private static ControlOption createMouseMultiplier(OptionTracker options){
		final OptionValue mouseMultiplier = OptionValues.createAnalogRangedOptionValue(.5, 2, 1);
		ControlOption r = new ControlOption("Rotation Sensitivity", "How sensitive should rotation be",
				"controls.all.mouse", mouseMultiplier);
		options.addControlOption(r);
		return r;
	}

	/**
	 * Creates, returns, and adds a ControlOption to the passed {@link OptionTracker}
	 * @param options The OptionTracker
	 * @return The created ControlOption
	 */
	private static ControlOption createMouseInvert(OptionTracker options){
		final OptionValue mouseInvert = OptionValues.createBooleanOptionValue(false);
		ControlOption r = new ControlOption("Invert Rotation", "Should the rotation be inverted",
				"controls.all.mouse", mouseInvert);
		options.addControlOption(r);
		return r;
	}
	private static InputPart createAxisChooser(final InputPart xAxis, final InputPart yAxis, OptionTracker options, boolean useYByDefault, String controlType){
		final OptionValue useY = OptionValues.createBooleanOptionValue(useYByDefault);
		options.addControlOption(new ControlOption("Use Y Axis", "Should the Y Axis be used", "controls.all." + controlType + ".axis_choice", useY));

		InputPart r = References.create(() -> useY.getBooleanOptionValue() ? yAxis : xAxis);
		r.addChild(xAxis);
		r.addChild(yAxis);
		return r;
	}
	private static InputPart createMouseAxis(OptionTracker options){
		OptionValue mouseMultiplier = createMouseMultiplier(options).getOptionValue();
		OptionValue mouseInverted = createMouseInvert(options).getOptionValue();
		return createAxisChooser(
				new GdxMouseAxis(false, 1.0f, mouseMultiplier, mouseInverted),
				new GdxMouseAxis(true, -1.0f, mouseMultiplier, mouseInverted),
				options, false, "mouse");
	}
	private static InputPart createPhoneAxis(OptionTracker options, Rectangle proportionalScreenArea){

		OptionValue mouseMultiplier = createMouseMultiplier(options).getOptionValue();
		OptionValue mouseInverted = createMouseInvert(options).getOptionValue();
		return createAxisChooser(
				new GdxMouseAxis(false, 5.0f, mouseMultiplier, mouseInverted, proportionalScreenArea),
				new GdxMouseAxis(true, -5.0f, mouseMultiplier, mouseInverted, proportionalScreenArea),
				options, true, "phone");
	}

	public static UsableGameInput createKeyboardInput(){
		final JoystickPart mainJoystick = FourKeyJoystick.newWASDJoystick();
		final JoystickPart selectorJoystick = FourKeyJoystick.newArrowKeyJoystick();
		final InputPart rotateAxis, fireButton, startButton, slow, activatePowerup, pauseButton, backButton, enterButton;
		final OptionTracker options = new OptionTracker();

		rotateAxis = createMouseAxis(options);
		fireButton = new HighestPositionInputPart(new KeyInputPart(Input.Keys.SPACE), new KeyInputPart(Input.Buttons.LEFT, true));
		startButton = new KeyInputPart(Input.Keys.ENTER);
		slow = new KeyInputPart(Input.Keys.SHIFT_LEFT);
		activatePowerup = new KeyInputPart(Input.Keys.F);
		pauseButton = new HighestPositionInputPart(new KeyInputPart(Input.Keys.ESCAPE),
				new KeyInputPart(Input.Keys.ENTER));
		backButton = new HighestPositionInputPart(new KeyInputPart(Input.Keys.ESCAPE), new KeyInputPart(Input.Keys.BACKSPACE));
		enterButton = new HighestPositionInputPart(new KeyInputPart(Input.Keys.ENTER), new KeyInputPart(Input.Keys.SPACE));

		DefaultUsableGameInput r = new DefaultUsableGameInput("Keyboard Controls",
				mainJoystick, rotateAxis, fireButton, slow, activatePowerup, startButton,
				pauseButton, backButton, selectorJoystick, enterButton, null, options, Collections.emptyList());

		r.addChildren(false, false, mainJoystick, rotateAxis, fireButton, slow, activatePowerup,
				startButton, pauseButton, backButton, selectorJoystick, enterButton);
		return r;
	}

	/**
	 *
	 * @param renderParts if not null, we should create a touchpad joystick
	 * @param leftHanded
	 * @return
	 */
	private static UsableGameInput createPhoneInput(RenderParts renderParts, boolean leftHanded) {
//		if(!Gdx.input.isPeripheralAvailable(Input.Peripheral.Gyroscope)){
//			throw new UnsupportedOperationException("Cannot create gyro input without gyroscope");
//		}
		final Rectangle fireArea;
		final Rectangle rotateArea;
		{
			final Rectangle leftSide = new Rectangle(0, 0, .5f, 1);
			final Rectangle rightSide = new Rectangle(.5f, 0, .5f, 1);
			if(leftHanded){
				rotateArea = leftSide;
				fireArea = rightSide;
			} else {
				fireArea = leftSide;
				rotateArea = rightSide;
			}
		}

		final JoystickPart mainJoystick;
		final JoystickPart dummySelector;
		final InputPart rotateAxis, fireButton, startButton, slow, activatePowerup, pauseBackButton, dummyEnter;
		final ControllerRumble rumble;
		final OptionTracker options = new OptionTracker();
		if(renderParts != null){
			// TODO figure out a better way to tell if this is active. Using () -> true right now (always active)
			Touchpad touchpad = renderParts.getTouchpadRenderer().createTouchpad(() -> true, new Vector2(.13f, .5f), .35f);
			mainJoystick = new GdxTouchpadJoystick(touchpad);
//			OptionValue mainJoystickDiameter = ((GdxHiddenTouchJoystick) mainJoystick).getMinimumProportionalDiameterOptionValue();
//			options.addControlOption(new ControlOption("Hidden Joystick Proportional Diameter", "How large should the joystick be",
//					"controls.all.joystick.diameter", mainJoystickDiameter));

			fireButton = new HighestPositionInputPart(
					Arrays.asList(
							new GdxScreenTouchButton(fireArea),
							new ReleaseButtonPress(new GdxScreenTouchButton(rotateArea))
					),
					true);
		} else {
			mainJoystick = new GdxTiltJoystick();
			options.addController((ConfigurableControllerPart) mainJoystick);

			fireButton = new GdxScreenTouchButton(fireArea);
		}
//		rotateAxis = new GdxMouseAxis(true, -5.0f, createMouseMultiplier(options).getOptionValue(),
//				createMouseInvert(options).getOptionValue(), rotateArea);
		rotateAxis = createPhoneAxis(options, rotateArea);

		startButton = new DummyInputPart(0, false);
		slow = new DummyInputPart(0, false);

		OptionValue shakeThresholdValue = OptionValues.createDigitalRangedOptionValue(3, 16, 8);
		GdxShakeButton button = new GdxShakeButton(shakeThresholdValue);
		options.addControlOption(new ControlOption("Powerup Activate Shake Sensitivity",
				"How much you have to shake the device to activate the powerup in m/s^2",
				"controls.all.shake", shakeThresholdValue));
		activatePowerup = button;
		Gdx.input.setCatchBackKey(true);
		pauseBackButton = new KeyInputPart(Input.Keys.BACK);

		dummySelector = new TwoAxisJoystickPart(new DummyInputPart(0, true), new DummyInputPart(0, true));
		dummyEnter = new DummyInputPart(0, false);

		GdxRumble gdxRumble = new GdxRumble();
		options.addController(gdxRumble);
		rumble = gdxRumble;

		return new DefaultUsableGameInput(renderParts != null ? "Phone Virtual Joystick Controls" : "Phone Gyro Controls",
				mainJoystick, rotateAxis, fireButton, slow, activatePowerup, startButton,
				pauseBackButton, pauseBackButton, dummySelector, dummyEnter, rumble, options, Collections.emptyList())
		{{

			addChildren(false, false, mainJoystick, rotateAxis, fireButton, slow, activatePowerup,
					startButton, pauseBackButton, dummySelector, dummyEnter, rumble);

		}};
	}
	public static UsableGameInput createTouchGyroInput(){
		return createPhoneInput(null, false);
	}

	public static UsableGameInput createVirtualJoystickInput(RenderParts renderParts){
		return createPhoneInput(renderParts, false);
	}
}
