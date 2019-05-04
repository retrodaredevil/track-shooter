package me.retrodaredevil.game.trackshooter.input;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.ui.PointerTouchpad;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Objects;

import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import me.retrodaredevil.controller.ControllerPart;
import me.retrodaredevil.controller.ControllerPartNotUpdatedException;
import me.retrodaredevil.controller.input.*;
import me.retrodaredevil.controller.options.*;
import me.retrodaredevil.controller.output.ControllerRumble;
import me.retrodaredevil.controller.output.DisconnectedRumble;
import me.retrodaredevil.game.trackshooter.input.implementations.*;
import me.retrodaredevil.game.trackshooter.render.RenderParts;
import me.retrodaredevil.game.trackshooter.render.parts.ArrowRenderer;
import me.retrodaredevil.game.trackshooter.render.parts.TouchpadRenderer;

import static java.util.Objects.requireNonNull;

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
			double def = 2.75 / (Gdx.graphics.getDensity() + .3);
			return Math.round(def * 10.0) / 10.0; // round to nearest 10%
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

	/**
	 *
	 * @param xAxis The x axis
	 * @param yAxis The y axis
	 * @param options The options that useY will be added to
	 * @param useY This will be automatically added to options and determines whether to use the x or y axis
	 * @param baseControlScheme The simple name of the controls scheme to determine the category
	 * @return The created InputPart
	 */
	private static InputPart createRotationAxisChooser(final InputPart xAxis, final InputPart yAxis, OptionTracker options, OptionValue useY, String baseControlScheme){
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
		final OptionValue useY = OptionValues.createBooleanOptionValue(false);
		return createRotationAxisChooser(
				new GdxMouseAxis(false, () -> 1.0f * (float) mouseMultiplier.getOptionValue() * (mouseInverted.getBooleanOptionValue() ? -1 : 1)),
				new GdxMouseAxis(true, () -> -1.0f * (float) mouseMultiplier.getOptionValue() * (mouseInverted.getBooleanOptionValue() ? -1 : 1)),
				options, useY, MOUSE
		);
	}
	private static InputPart createTouchAxis(OptionValue useY, OptionTracker options, ScreenArea screenArea, OptionValue isLeftHanded){
		OptionValue multiplier = createRotationMultiplier(options, true, TOUCH).getOptionValue();
		OptionValue inverted = createRotationInvert(options, TOUCH).getOptionValue();
		return createRotationAxisChooser(
				new GdxMouseAxis(false, () -> 7.0f * (float) multiplier.getOptionValue() * (inverted.getBooleanOptionValue() ? -1 : 1), screenArea),
				new GdxMouseAxis(true,
						() -> -7.0f * (float) multiplier.getOptionValue() * (inverted.getBooleanOptionValue() ? -1 : 1) * (isLeftHanded.getBooleanOptionValue() ? -1 : 1),
						screenArea),
				options, useY, TOUCH
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
				mainJoystick, rotateAxis, null, fireButton, slow, activatePowerup, startButton, pauseButton, backButton, selectorJoystick, enterButton, new DisconnectedRumble(), options, Collections.emptyList()
		);

		r.addChildren(false, false, mainJoystick, rotateAxis, fireButton, slow, activatePowerup,
				startButton, pauseButton, backButton, selectorJoystick, enterButton);
		return r;
	}

	/**
	 *
	 * @param renderParts if not null, we should create a touchpad joystick
	 * @return
	 */
	private static UsableGameInput createTouchInput(boolean isTouchpad, RenderParts renderParts, RumbleAnalogControl rumbleAnalogControl) {
		if(isTouchpad && !Gdx.input.isPeripheralAvailable(Input.Peripheral.Gyroscope)){
			Gdx.app.error("no gyro scope available", "creating gyro control scheme anyway");
		}
		if(renderParts == null){
			throw new NullPointerException("renderParts is null!");
		}
		if(rumbleAnalogControl == null){
			throw new NullPointerException("rumbleAnalogControl is null! At least use GdxRumble.UNSUPPORTED_ANALOG!");
		}

		final JoystickPart mainJoystick;
		final JoystickPart dummySelector;
		final JoystickPart rotationPointInput;
		final InputPart rotateAxis, fireButton, startButton, slow, activatePowerup, pauseBackButton, dummyEnter;
		final ControllerRumble rumble;
		final OptionTracker options = new OptionTracker();
		final ActiveDetector activeDetector = new ActiveDetector();

		final OptionValue isLeftHanded = OptionValues.createBooleanOptionValue(false);
		options.add(new ControlOption("Left Handed", "Should the controls be reversed for left handed.",
				"controls.main." + TOUCH + ".left_handed", isLeftHanded));
		final OptionValue isPointRotation = OptionValues.createBooleanOptionValue(false);
		options.add(new ControlOption("Point to Rotate", "Should point to rotate be enabled",
				"controls.rotation." + TOUCH + ".point_rotation", isPointRotation));
		final ScreenArea fireAreaGetter;
		final ScreenArea rotateAreaGetter;
		{
			final ScreenArea rightHandedFireArea;
			final ScreenArea leftHandedFireArea;
			final ScreenArea rightHandedRotateArea;
			final ScreenArea leftHandedRotateArea;

			final ScreenArea leftSide = ScreenAreas.leftOfX(.5f);
			final ScreenArea rightSide = ScreenAreas.rightOfX(.5f);
			rightHandedFireArea = leftSide;
			rightHandedRotateArea = rightSide;

			leftHandedRotateArea = leftSide;
			leftHandedFireArea = rightSide;

			System.out.println("Creating fireAreaGetter. isPointRotation.hashCode(): " + isPointRotation.hashCode());
			fireAreaGetter = (x, y) -> isLeftHanded.getBooleanOptionValue()
				? leftHandedFireArea.containsPoint(x, y)
				: rightHandedFireArea.containsPoint(x, y);
			rotateAreaGetter = (x, y) -> isLeftHanded.getBooleanOptionValue()
					? leftHandedRotateArea.containsPoint(x, y) :
					rightHandedRotateArea.containsPoint(x, y);
		}

		final ShouldIgnorePointer shouldIgnorePointer;

		if(isTouchpad){
//			visibilityChanger = new TouchpadRenderer.UsableGameInputTouchpadVisibilityChanger();
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

			PointerTouchpad touchpad = renderParts.getTouchpadRenderer().createTouchpad(activeDetector::isActive, new TouchpadRenderer.ProportionalPositionGetter() {
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
			shouldIgnorePointer = (pointer) -> pointer == touchpad.getPointer();


			mainJoystick = new GdxTouchpadJoystick(touchpad);

			final ScreenArea rotateFireArea = (x, y) -> {
				if(isPointRotation.getBooleanOptionValue()){
					return true;
				}
				return rotateAreaGetter.containsPoint(x, y);
			};

			fireButton = new HighestPositionInputPart(Arrays.asList(
					new GdxScreenTouchButton((x, y) -> {
						if(isPointRotation.getBooleanOptionValue()){
							return false;
						}
						return fireAreaGetter.containsPoint(x, y);
					}, shouldIgnorePointer, true),
					new DigitalChildPositionInputPart(new GdxScreenTouchButton(rotateFireArea, shouldIgnorePointer, true),
							inputPart -> {
								if(constantShoot.getBooleanOptionValue()){
									return false;
								}
								if(isPointRotation.getBooleanOptionValue()){
									return inputPart.isPressed();
								}
								return inputPart.isReleased();
							}), // will fire if released when constant shoot not enabled
					new LowestPositionInputPart( // this is for the constant shoot
							new DigitalPatternInputPart(160, 80),
							new DigitalChildPositionInputPart(new GdxScreenTouchButton(rotateFireArea, shouldIgnorePointer, true),
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
			shouldIgnorePointer = (pointer) -> false;
			mainJoystick = new GdxTiltJoystick("controls.movement." + TOUCH + ".gyro.max_tilt");
			options.add((ConfigurableObject) mainJoystick);

			fireButton = new GdxScreenTouchButton((x, y) -> {
				if(isPointRotation.getBooleanOptionValue()){
					return true;
				}
				return fireAreaGetter.containsPoint(x, y);
			});
		}
		final OptionValue useY = OptionValues.createBooleanOptionValue(true);
		rotateAxis = createTouchAxis(useY, options, rotateAreaGetter, isLeftHanded);
		renderParts.getArrowRenderer().createArrow(new ArrowRenderer.ShouldShowArrow() {
			long lastPress = 0;
			@Override
			public boolean shouldShow() {
				boolean pressing = false;
				try {
					pressing = !rotateAxis.isDeadzone();
				} catch(ControllerPartNotUpdatedException ex){
				}
				if(pressing){
					lastPress = System.currentTimeMillis();
					return false;
				}
				if(lastPress + 10000 > System.currentTimeMillis()){ // hasn't been pressed for 10 seconds
					return false;
				}
				return activeDetector.isActive();
			}

			@Override
			public boolean isHorizontal() {
				return !useY.getBooleanOptionValue();
			}

			@Override
			public boolean isLeft() {
				return isLeftHanded.getBooleanOptionValue();
			}
		});

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
			final GdxRumble gdxRumble = new GdxRumble(rumbleAnalogControl);
			rumble = gdxRumble;
			options.add(gdxRumble);
		} else {
			rumble = new DisconnectedRumble();
		}
		rotationPointInput = new ScreenPositionJoystick(shouldIgnorePointer){
			@Override
			public boolean isConnected() {
				return isPointRotation.getBooleanOptionValue();
			}
		};

		DefaultUsableGameInput r = new DefaultUsableGameInput(isTouchpad ? "Phone Virtual Joystick Controls" : "Phone Gyro Controls",
				mainJoystick, rotateAxis, rotationPointInput, fireButton, slow, activatePowerup, startButton, pauseBackButton, pauseBackButton, dummySelector, dummyEnter, rumble, options, Collections.emptyList()
		);

		r.addChildren(false, false, mainJoystick, rotateAxis, rotationPointInput, fireButton, slow, activatePowerup,
				startButton, pauseBackButton, dummySelector, dummyEnter, rumble);
		activeDetector.setGameInput(r);
		return r;
	}
	public static UsableGameInput createTouchGyroInput(RenderParts renderParts, RumbleAnalogControl rumbleAnalogControl){
		return createTouchInput(false, renderParts, rumbleAnalogControl);
	}

	public static UsableGameInput createVirtualJoystickInput(RenderParts renderParts, RumbleAnalogControl rumbleAnalogControl){
		return createTouchInput(true, requireNonNull(renderParts), rumbleAnalogControl);
	}

	private static ControlOption createRumbleOnSingleShotControlOption(){
		OptionValue optionValue = OptionValues.createBooleanOptionValue(false);
		return new ControlOption("Rumble on Single Shot",
				"Should there be rumble on a single shot",
				"config.rumble.on_single_shot",
				optionValue);
	}

	/**
	 * Creates the "rumble on single shot" input part and mutates the passed controlOptions
	 * @param controlOptions The {@link OptionTracker} to add the {@link ControlOption} to
	 * @return The {@link InputPart} representing whether or not the created {@link ControlOption} boolean is checked
	 */
	static InputPart createRumbleOnSingleShotInputPart(ControllerPart parent, OptionTracker controlOptions, ControllerRumble rumble){
		final ControlOption option = GameInputs.createRumbleOnSingleShotControlOption();
		BooleanConfigInputPart inputPart = new BooleanConfigInputPart(option, rumble::isConnected);
//		inputPart.setParent(parent);
		parent.addChild(inputPart);
		controlOptions.add(inputPart);
		return inputPart;
	}
	private static class ActiveDetector {
		private UsableGameInput gameInput = null;

		public boolean isActive() {
			return gameInput != null && gameInput.isActiveInput();
		}
		public void setGameInput(UsableGameInput gameInput){
			this.gameInput = gameInput;
		}
		public UsableGameInput getGameInput(){ return gameInput; }
	}
}
