package me.retrodaredevil.game.trackshooter.input;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;

import java.util.Arrays;
import java.util.Collections;
import java.util.Objects;

import me.retrodaredevil.controller.input.DigitalChildPositionInputPart;
import me.retrodaredevil.controller.input.DummyInputPart;
import me.retrodaredevil.controller.input.HighestPositionInputPart;
import me.retrodaredevil.controller.input.InputPart;
import me.retrodaredevil.controller.input.JoystickPart;
import me.retrodaredevil.controller.input.LowestPositionInputPart;
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
import me.retrodaredevil.game.trackshooter.input.implementations.ScreenAreaGetter;
import me.retrodaredevil.game.trackshooter.render.RenderParts;
import me.retrodaredevil.game.trackshooter.render.parts.TouchpadRenderer;

public final class GameInputs {
	/** The amount of time to keep "constant" shooting after the rotate area has been released (in milliseoncds)*/
	private static final long CONSTANT_SHOOT_TIME_AFTER_RELEASE = 500;
	private static final String TOUCH = "touch";
	private static final String MOUSE = "mouse";

	private GameInputs(){}

	/**@param options The OptionTracker to append to
	 * @param touchScreen true if this is for a touch screen
	 * @param baseControlScheme Usually "mouse" or "touch"
	 * @return The ControlOption */
	private static ControlOption createRotationMultiplier(OptionTracker options, boolean touchScreen, String baseControlScheme){
		final double min = .2;
		final double max = 2;
		final double def = Math.min(max, Math.max(min, getDefaultMouseMultiplier(touchScreen)));
		final OptionValue multiplier = OptionValues.createAnalogRangedOptionValue(min, max, def);
		ControlOption r = new ControlOption("Rotation Sensitivity", "How sensitive should rotation be",
				"controls.rotation." + baseControlScheme + ".sensitivity", multiplier);
		options.add(r);
		return r;
	}
	private static double getDefaultMouseMultiplier(boolean mobile){
		if(mobile) {
			double def = 1.5 / (Gdx.graphics.getDensity() + .3);
			return Math.round(def * 10.0) / 10.0;
		}
		return 1;
	}

	/**
	 * Creates, returns, and adds a ControlOption to the passed {@link OptionTracker}
	 * @param options The OptionTracker
	 * @return The created ControlOption
	 */
	private static ControlOption createRotationInvert(OptionTracker options, String baseControlScheme){
		final OptionValue invert = OptionValues.createBooleanOptionValue(false);
		ControlOption r = new ControlOption("Invert Rotation", "Should the rotation be inverted",
				"controls.rotation." + baseControlScheme + ".invert", invert);
		options.add(r);
		return r;
	}
	private static InputPart createRotationAxisChooser(final InputPart xAxis, final InputPart yAxis, OptionTracker options, boolean useYByDefault, String baseControlScheme){
		final OptionValue useY = OptionValues.createBooleanOptionValue(useYByDefault);
		options.add(new ControlOption("Use Y Axis for Rotation", "Should the Y Axis be used for rotation",
				"controls.rotation." + baseControlScheme + ".use_y_axis", useY));

		InputPart r = References.create(() -> useY.getBooleanOptionValue() ? yAxis : xAxis);
		r.addChild(xAxis);
		r.addChild(yAxis);
		return r;
	}
	private static InputPart createMouseAxis(OptionTracker options){
		OptionValue mouseMultiplier = createRotationMultiplier(options, false, MOUSE).getOptionValue();
		OptionValue mouseInverted = createRotationInvert(options, MOUSE).getOptionValue();
		return createRotationAxisChooser(
				new GdxMouseAxis(false, () -> 1.0f * (float) mouseMultiplier.getOptionValue() * (mouseInverted.getBooleanOptionValue() ? -1 : 1)),
				new GdxMouseAxis(true, () -> -1.0f * (float) mouseMultiplier.getOptionValue() * (mouseInverted.getBooleanOptionValue() ? -1 : 1)),
				options, false, MOUSE
		);
	}
	private static InputPart createTouchAxis(OptionTracker options, ScreenAreaGetter proportionalScreenAreaGetter, OptionValue isLeftHanded){
		OptionValue multiplier = createRotationMultiplier(options, true, TOUCH).getOptionValue();
		OptionValue inverted = createRotationInvert(options, TOUCH).getOptionValue();
		return createRotationAxisChooser(
				new GdxMouseAxis(false, () -> 7.0f * (float) multiplier.getOptionValue() * (inverted.getBooleanOptionValue() ? -1 : 1), proportionalScreenAreaGetter),
				new GdxMouseAxis(true,
						() -> -7.0f * (float) multiplier.getOptionValue() * (inverted.getBooleanOptionValue() ? -1 : 1) * (isLeftHanded.getBooleanOptionValue() ? -1 : 1),
						proportionalScreenAreaGetter),
				options, true, TOUCH
		);
	}

	public static UsableGameInput createMouseAndKeyboardInput(){
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
	private static UsableGameInput createTouchInput(RenderParts renderParts) {
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
				"controls.main." + TOUCH + ".left_handed", isLeftHanded));
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
			OptionValue constantShoot = OptionValues.createBooleanOptionValue(true);
			OptionValue distanceAwayX = OptionValues.createAnalogRangedOptionValue(.05, .5, .15);
			OptionValue heightOption = OptionValues.createAnalogRangedOptionValue(.25, .75, .5);
			OptionValue diameterOption = OptionValues.createAnalogRangedOptionValue(.2, .5, .35);

			options.add(new ControlOption("Constant Shoot",
					"Should you be constantly shooting when holding down on the rotation area",
					"controls.shooting." + TOUCH + ".constant_shoot", constantShoot));

			options.add(new ControlOption("Joystick X Position",
					"The x position of the joystick.", "controls.movement." + TOUCH + ".joystick.position.x", distanceAwayX));
			options.add(new ControlOption("Joystick Y Position",
					"The y position of the joystick.", "controls.movement." + TOUCH + ".joystick.position.y", heightOption));
			options.add(new ControlOption("Joystick size",
					"The size of the joystick relative to the height", "controls.movement." + TOUCH + ".joystick.size", diameterOption));

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

			fireButton = new HighestPositionInputPart(Arrays.asList(
					new GdxScreenTouchButton(fireAreaGetter),
					new DigitalChildPositionInputPart(new GdxScreenTouchButton(rotateAreaGetter),
							inputPart -> !constantShoot.getBooleanOptionValue() && inputPart.isReleased()), // will fire if released when constant shoot not enabled
					new LowestPositionInputPart(
							new DigitalPatternInputPart(160, 80),
							new DigitalChildPositionInputPart(new GdxScreenTouchButton(rotateAreaGetter),
									new DigitalChildPositionInputPart.DigitalGetter() {
										Long lastDown = null;
										@Override
										public boolean isDown(InputPart childInputPart) {
											if(!constantShoot.getBooleanOptionValue()) return false;

											final boolean down = childInputPart.isDown();
											final long currentTime = System.currentTimeMillis();
											if(down){
												lastDown = currentTime;
											}
											return down || (lastDown != null && lastDown + CONSTANT_SHOOT_TIME_AFTER_RELEASE > currentTime);
									}
							})
					)
			), true);
		} else {
			visibilityChanger = null;
			mainJoystick = new GdxTiltJoystick("controls.movement." + TOUCH + ".gyro.max_tilt");
			options.add((ConfigurableObject) mainJoystick);

			fireButton = new GdxScreenTouchButton(fireAreaGetter);
		}
		rotateAxis = createTouchAxis(options, rotateAreaGetter, isLeftHanded);

		startButton = new DummyInputPart(0, false);
		slow = new DummyInputPart(0, false);

		OptionValue shakeThresholdValue = OptionValues.createDigitalRangedOptionValue(3, 16, 9);
		GdxShakeButton button = new GdxShakeButton(shakeThresholdValue);
		options.add(new ControlOption("Power-up Activate Shake Threshold",
				"How much you have to shake the device to activate the power-up in m/s^2",
				"controls.misc." + TOUCH + ".powerup.shake_threshold", shakeThresholdValue));
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
		return createTouchInput(null);
	}

	public static UsableGameInput createVirtualJoystickInput(RenderParts renderParts){
		return createTouchInput(Objects.requireNonNull(renderParts));
	}
}
