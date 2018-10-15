package me.retrodaredevil.game.trackshooter.input;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;

import java.util.Arrays;
import java.util.Collections;
import java.util.Objects;

import me.retrodaredevil.controller.input.DummyInputPart;
import me.retrodaredevil.controller.input.HighestPositionInputPart;
import me.retrodaredevil.controller.input.InputPart;
import me.retrodaredevil.controller.input.JoystickPart;
import me.retrodaredevil.controller.input.References;
import me.retrodaredevil.controller.input.TwoAxisJoystickPart;
import me.retrodaredevil.controller.options.ConfigurableObject;
import me.retrodaredevil.controller.options.ControlOption;
import me.retrodaredevil.controller.options.OptionTracker;
import me.retrodaredevil.controller.options.OptionValue;
import me.retrodaredevil.controller.options.OptionValues;
import me.retrodaredevil.controller.output.ControllerRumble;
import me.retrodaredevil.controller.output.DisconnectedRumble;
import me.retrodaredevil.game.trackshooter.input.implementations.DigitalPatternInputPart;
import me.retrodaredevil.game.trackshooter.input.implementations.GdxMouseAxis;
import me.retrodaredevil.game.trackshooter.input.implementations.GdxRumble;
import me.retrodaredevil.game.trackshooter.input.implementations.GdxScreenTouchButton;
import me.retrodaredevil.game.trackshooter.input.implementations.GdxShakeButton;
import me.retrodaredevil.game.trackshooter.input.implementations.GdxTiltJoystick;
import me.retrodaredevil.game.trackshooter.input.implementations.GdxTouchpadJoystick;
import me.retrodaredevil.game.trackshooter.input.implementations.KeyInputPart;
import me.retrodaredevil.game.trackshooter.input.implementations.helper.DigitalChildPositionInputPart;
import me.retrodaredevil.game.trackshooter.input.implementations.ScreenAreaGetter;
import me.retrodaredevil.game.trackshooter.render.RenderParts;
import me.retrodaredevil.game.trackshooter.render.parts.TouchpadRenderer;

public final class GameInputs {
	private GameInputs(){}

	private static ControlOption createMouseMultiplier(OptionTracker options, boolean mobile){
		final double min = .2;
		final double max = 2;
		final double def = Math.min(max, Math.max(min, getDefaultMouseMultiplier(mobile)));
		final OptionValue mouseMultiplier = OptionValues.createAnalogRangedOptionValue(min, max, def);
		ControlOption r = new ControlOption("Rotation Sensitivity", "How sensitive should rotation be",
				"controls.all.mouse", mouseMultiplier);
		options.add(r);
		return r;
	}
	private static double getDefaultMouseMultiplier(boolean mobile){
		if(mobile) {
			double def = 2 / (Gdx.graphics.getDensity() + .3);
			return Math.round(def * 10.0) / 10.0;
		}
		return 1;
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
		options.add(r);
		return r;
	}
	private static InputPart createAxisChooser(final InputPart xAxis, final InputPart yAxis, OptionTracker options, boolean useYByDefault, String controlType, String label){
		final OptionValue useY = OptionValues.createBooleanOptionValue(useYByDefault);
		options.add(new ControlOption(label, "Should the Y Axis be used", "controls.all." + controlType + ".axis_choice", useY));

		InputPart r = References.create(() -> useY.getBooleanOptionValue() ? yAxis : xAxis);
		r.addChild(xAxis);
		r.addChild(yAxis);
		return r;
	}
	private static InputPart createMouseAxis(OptionTracker options){
		OptionValue mouseMultiplier = createMouseMultiplier(options, false).getOptionValue();
		OptionValue mouseInverted = createMouseInvert(options).getOptionValue();
		return createAxisChooser(
				new GdxMouseAxis(false, () -> 1.0f * (float) mouseMultiplier.getOptionValue() * (mouseInverted.getBooleanOptionValue() ? -1 : 1)),
				new GdxMouseAxis(true, () -> -1.0f * (float) mouseMultiplier.getOptionValue() * (mouseInverted.getBooleanOptionValue() ? -1 : 1)),
				options, false, "mouse", "Use Y Axis for Rotation"
		);
	}
	private static InputPart createPhoneAxis(OptionTracker options, ScreenAreaGetter proportionalScreenAreaGetter, OptionValue isLeftHanded){

		OptionValue mouseMultiplier = createMouseMultiplier(options, true).getOptionValue();
		OptionValue mouseInverted = createMouseInvert(options).getOptionValue();
		return createAxisChooser(
				new GdxMouseAxis(false, () -> 7.0f * (float) mouseMultiplier.getOptionValue() * (mouseInverted.getBooleanOptionValue() ? -1 : 1), proportionalScreenAreaGetter),
				new GdxMouseAxis(true,
						() -> -7.0f * (float) mouseMultiplier.getOptionValue() * (mouseInverted.getBooleanOptionValue() ? -1 : 1) * (isLeftHanded.getBooleanOptionValue() ? -1 : 1),
						proportionalScreenAreaGetter),
				options, true, "phone", "Use Y Axis for Rotation"
		);
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
				pauseButton, backButton, selectorJoystick, enterButton, new DisconnectedRumble(), options, Collections.emptyList());

		r.addChildren(false, false, mainJoystick, rotateAxis, fireButton, slow, activatePowerup,
				startButton, pauseButton, backButton, selectorJoystick, enterButton);
		return r;
	}

	/**
	 *
	 * @param renderParts if not null, we should create a touchpad joystick
	 * @return
	 */
	private static UsableGameInput createPhoneInput(RenderParts renderParts) {
		if(renderParts == null && !Gdx.input.isPeripheralAvailable(Input.Peripheral.Gyroscope)){
			Gdx.app.error("no gyro scope available", "creating gyro control scheme anyway");
		}

		final JoystickPart mainJoystick;
		final JoystickPart dummySelector;
		final InputPart rotateAxis, fireButton, startButton, slow, activatePowerup, pauseBackButton, dummyEnter;
		final ControllerRumble rumble;
		final OptionTracker options = new OptionTracker();
		final TouchpadRenderer.UsableGameInputTouchpadVisibilityChanger visibilityChanger; // may be null

		final OptionValue isLeftHanded = OptionValues.createBooleanOptionValue(false);
		options.add(new ControlOption("Left Handed", "Should the controls be reversed for left handed.",
				"controls.all.phone.hand", isLeftHanded));
		final ScreenAreaGetter fireAreaGetter;
		final ScreenAreaGetter rotateAreaGetter;
		{
			final Rectangle rightHandedFireArea;
			final Rectangle leftHandedFireArea;
			final Rectangle rightHandedRotateArea;
			final Rectangle leftHandedRotateArea;

			final Rectangle leftSide = new Rectangle(0, 0, .5f, 1);
			final Rectangle rightSide = new Rectangle(.5f, 0, .5f, 1);
			rightHandedFireArea = leftSide;
			rightHandedRotateArea = rightSide;

			leftHandedRotateArea = leftSide;
			leftHandedFireArea = rightSide;

			fireAreaGetter = () -> isLeftHanded.getBooleanOptionValue() ? leftHandedFireArea : rightHandedFireArea;
			rotateAreaGetter = () -> isLeftHanded.getBooleanOptionValue() ? leftHandedRotateArea : rightHandedRotateArea;
		}

		if(renderParts != null){
			visibilityChanger = new TouchpadRenderer.UsableGameInputTouchpadVisibilityChanger();
			OptionValue distanceAwayX = OptionValues.createAnalogRangedOptionValue(.05, .5, .15);
			OptionValue heightOption = OptionValues.createAnalogRangedOptionValue(.25, .75, .5);
			OptionValue diameterOption = OptionValues.createAnalogRangedOptionValue(.2, .5, .35);
			options.add(new ControlOption("Joystick X Position",
					"The x position of the joystick.", "controls.joystick.position.x", distanceAwayX));
			options.add(new ControlOption("Joystick Y Position",
					"The y position of the joystick.", "controls.joystick.position.y", heightOption));
			options.add(new ControlOption("Joystick size",
					"The size of the joystick relative to the height", "controls.joystick.size", diameterOption));
			Touchpad touchpad = renderParts.getTouchpadRenderer().createTouchpad(visibilityChanger, new TouchpadRenderer.ProportionalPositionGetter() {
				@Override
				public float getX() {
					double x = distanceAwayX.getOptionValue();
					if(isLeftHanded.getBooleanOptionValue()){
						return 1f - (float) x;
					}
					return (float) x;
				}

				@Override
				public float getY() {
					return (float) heightOption.getOptionValue();
				}
			}, () -> (float) diameterOption.getOptionValue());
			mainJoystick = new GdxTouchpadJoystick(touchpad);

			fireButton = new HighestPositionInputPart(
					new DigitalChildPositionInputPart(new HighestPositionInputPart(
							Arrays.asList(
									new GdxScreenTouchButton(fireAreaGetter),
									new DigitalChildPositionInputPart(new GdxScreenTouchButton(rotateAreaGetter), InputPart::isReleased) // will fire if released
							), true),
							InputPart::isPressed // will only be down if it's being pressed
					),
					new DigitalPatternInputPart(160, 80) // for 80ms, the above InputPart won't register - this is on purpose to avoid lots of shots at once
			);
		} else {
			visibilityChanger = null;
			mainJoystick = new GdxTiltJoystick();
			options.add((ConfigurableObject) mainJoystick);

			fireButton = new GdxScreenTouchButton(fireAreaGetter);
		}
		rotateAxis = createPhoneAxis(options, rotateAreaGetter, isLeftHanded);

		startButton = new DummyInputPart(0, false);
		slow = new DummyInputPart(0, false);

		OptionValue shakeThresholdValue = OptionValues.createDigitalRangedOptionValue(3, 16, 8);
		GdxShakeButton button = new GdxShakeButton(shakeThresholdValue);
		options.add(new ControlOption("Powerup Activate Shake Sensitivity",
				"How much you have to shake the device to activate the powerup in m/s^2",
				"controls.all.shake", shakeThresholdValue));
		activatePowerup = button;
		if(Gdx.app.getType() == Application.ApplicationType.Android) {
			pauseBackButton = new KeyInputPart(Input.Keys.BACK);
			Gdx.input.setCatchBackKey(true);
		} else {
			// TODO Provide replacement button for non-android devices
			pauseBackButton = new DummyInputPart(0, false);
		}

		dummySelector = new TwoAxisJoystickPart(new DummyInputPart(0, true), new DummyInputPart(0, true));
		dummyEnter = new DummyInputPart(0, false);

		if(Gdx.input.isPeripheralAvailable(Input.Peripheral.Vibrator)){
			final GdxRumble gdxRumble = new GdxRumble();
			rumble = gdxRumble;
			options.add(gdxRumble);
		} else {
			rumble = new DisconnectedRumble();
		}

		DefaultUsableGameInput r = new DefaultUsableGameInput(renderParts != null ? "Phone Virtual Joystick Controls" : "Phone Gyro Controls",
				mainJoystick, rotateAxis, fireButton, slow, activatePowerup, startButton,
				pauseBackButton, pauseBackButton, dummySelector, dummyEnter, rumble, options, Collections.emptyList());

		r.addChildren(false, false, mainJoystick, rotateAxis, fireButton, slow, activatePowerup,
				startButton, pauseBackButton, dummySelector, dummyEnter, rumble);
		if(visibilityChanger != null){
			visibilityChanger.setGameInput(r);
		}
		return r;
	}
	public static UsableGameInput createTouchGyroInput(){
		return createPhoneInput(null);
	}

	public static UsableGameInput createVirtualJoystickInput(RenderParts renderParts){
		Objects.requireNonNull(renderParts);
		return createPhoneInput(renderParts);
	}
}
