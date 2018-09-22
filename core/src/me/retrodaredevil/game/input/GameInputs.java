package me.retrodaredevil.game.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import me.retrodaredevil.controller.input.DummyInputPart;
import me.retrodaredevil.controller.input.HighestPositionInputPart;
import me.retrodaredevil.controller.input.InputPart;
import me.retrodaredevil.controller.input.JoystickPart;
import me.retrodaredevil.controller.input.TwoAxisJoystickPart;
import me.retrodaredevil.controller.options.ConfigurableControllerPart;
import me.retrodaredevil.controller.options.ControlOption;
import me.retrodaredevil.controller.options.OptionTracker;
import me.retrodaredevil.controller.options.OptionValue;
import me.retrodaredevil.controller.options.OptionValues;
import me.retrodaredevil.controller.output.ControllerRumble;
import me.retrodaredevil.game.input.implementations.GdxHiddenTouchJoystick;
import me.retrodaredevil.game.input.implementations.GdxMouseAxis;
import me.retrodaredevil.game.input.implementations.GdxRumble;
import me.retrodaredevil.game.input.implementations.GdxScreenTouchButton;
import me.retrodaredevil.game.input.implementations.GdxShakeButton;
import me.retrodaredevil.game.input.implementations.GdxTiltJoystick;
import me.retrodaredevil.game.input.implementations.GdxTouchpadJoystick;
import me.retrodaredevil.game.input.implementations.KeyInputPart;
import me.retrodaredevil.game.trackshooter.render.RenderParts;

public final class GameInputs {
	private GameInputs(){}

	private static ControlOption createMouseMultiplier(OptionTracker options){
		final OptionValue mouseMultiplier = OptionValues.createAnalogRangedOptionValue(.5, 2, 1);
		return new ControlOption("Rotation Sensitivity", "How sensitive should rotation be",
				"controls.all.mouse", mouseMultiplier)
		{{
			options.addControlOption(this);
		}};
	}
	private static ControlOption createMouseInvert(OptionTracker options){
		final OptionValue mouseInvert = OptionValues.createBooleanOptionValue(false);
		return new ControlOption("Invert Rotation", "Should the rotation be inverted",
				"controls.all.mouse", mouseInvert)
		{{
			options.addControlOption(this);
		}};
	}

	public static UsableGameInput createKeyboardInput(){
		final JoystickPart mainJoystick = FourKeyJoystick.newWASDJoystick();
		final JoystickPart selectorJoystick = FourKeyJoystick.newArrowKeyJoystick();
		final InputPart rotateAxis, fireButton, startButton, slow, activatePowerup, pauseButton, backButton, enterButton;
		final OptionTracker options = new OptionTracker();

		rotateAxis = new GdxMouseAxis(false, 1.0f, createMouseMultiplier(options).getOptionValue(), createMouseInvert(options).getOptionValue());
		fireButton = new HighestPositionInputPart(new KeyInputPart(Input.Keys.SPACE), new KeyInputPart(Input.Buttons.LEFT, true));
		startButton = new KeyInputPart(Input.Keys.ENTER);
		slow = new KeyInputPart(Input.Keys.SHIFT_LEFT);
		activatePowerup = new KeyInputPart(Input.Keys.F);
		pauseButton = new HighestPositionInputPart(new KeyInputPart(Input.Keys.ESCAPE),
				new KeyInputPart(Input.Keys.ENTER));
		backButton = new HighestPositionInputPart(new KeyInputPart(Input.Keys.ESCAPE), new KeyInputPart(Input.Keys.BACKSPACE));
		enterButton = new HighestPositionInputPart(new KeyInputPart(Input.Keys.ENTER), new KeyInputPart(Input.Keys.SPACE));

		return new DefaultUsableGameInput("Keyboard Controls",
				mainJoystick, rotateAxis, fireButton, slow, activatePowerup, startButton,
				pauseButton, backButton, selectorJoystick, enterButton, null, options, Collections.emptyList())
		{{

			addChildren(false, false, mainJoystick, rotateAxis, fireButton, slow, activatePowerup,
					startButton, pauseButton, backButton, selectorJoystick, enterButton);

		}};
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
		final Rectangle joystickArea;
		{
			final Rectangle leftSide = new Rectangle(0, 0, .5f, 1);
			final Rectangle rightSide = new Rectangle(.5f, 0, .5f, 1);
			final float centerJoystickCutoff = .2f;
			if(leftHanded){
				rotateArea = leftSide;
				fireArea = rightSide;
				joystickArea = new Rectangle(.5f + centerJoystickCutoff, 0, 1 - centerJoystickCutoff, 1);
			} else {
				fireArea = leftSide;
				rotateArea = rightSide;
				joystickArea = new Rectangle(0, 0, .5f - centerJoystickCutoff, 1);
			}
		}

		final JoystickPart mainJoystick;
		final JoystickPart dummySelector;
		final InputPart rotateAxis, fireButton, startButton, slow, activatePowerup, pauseBackButton, dummyEnter;
		final ControllerRumble rumble;
		final OptionTracker options = new OptionTracker();
		if(renderParts != null){
			Touchpad touchpad = renderParts.getTouchpadRenderer().createTouchpad(() -> true, new Vector2(.13f, .5f), .35f);
			mainJoystick = new GdxTouchpadJoystick(touchpad);
//			mainJoystick = new GdxHiddenTouchJoystick(joystickArea);
//			OptionValue mainJoystickDiameter = ((GdxHiddenTouchJoystick) mainJoystick).getMinimumProportionalDiameterOptionValue();
//			options.addControlOption(new ControlOption("Hidden Joystick Proportional Diameter", "How large should the joystick be",
//					"controls.all.joystick.diameter", mainJoystickDiameter));

//			fireButton = new HighestPositionInputPart(new KeyInputPart(Input.Keys.VOLUME_UP), new KeyInputPart(Input.Keys.VOLUME_DOWN));
			fireButton = new HighestPositionInputPart(Arrays.asList(new GdxScreenTouchButton(rotateArea), new GdxScreenTouchButton(fireArea)), true);
		} else {
			mainJoystick = new GdxTiltJoystick();
			options.addController((ConfigurableControllerPart) mainJoystick);

			fireButton = new GdxScreenTouchButton(fireArea);
		}
		rotateAxis = new GdxMouseAxis(true, -5.0f, createMouseMultiplier(options).getOptionValue(),
				createMouseInvert(options).getOptionValue(), rotateArea);

//		startButton = new KeyInputPart(Input.Keys.ENTER);
//		slow = new KeyInputPart(Input.Keys.SHIFT_LEFT);
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
