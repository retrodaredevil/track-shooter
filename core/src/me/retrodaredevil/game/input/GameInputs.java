package me.retrodaredevil.game.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Rectangle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import me.retrodaredevil.controller.input.HighestPositionInputPart;
import me.retrodaredevil.controller.input.InputPart;
import me.retrodaredevil.controller.input.JoystickPart;
import me.retrodaredevil.controller.options.ControlOption;
import me.retrodaredevil.controller.options.OptionValue;
import me.retrodaredevil.controller.options.OptionValues;
import me.retrodaredevil.controller.output.ControllerRumble;

public final class GameInputs {
	private GameInputs(){}

	private static ControlOption createMouseMultiplier(Collection<? super ControlOption> optionsToBeAddedTo){
		final OptionValue mouseMultiplier = OptionValues.createAnalogRangedOptionValue(.5, 2, 1);
		return new ControlOption("Rotation Sensitivity", "How sensitive should rotation be",
				"controls.all.mouse", mouseMultiplier)
		{{
			optionsToBeAddedTo.add(this);
		}};
	}
	private static ControlOption createMouseInvert(Collection<? super ControlOption> optionsToBeAddedTo){
		final OptionValue mouseInvert = OptionValues.createBooleanOptionValue(false);
		return new ControlOption("Invert Rotation", "Should the rotation be inverted",
				"controls.all.mouse", mouseInvert)
		{{
			optionsToBeAddedTo.add(this);
		}};
	}

	public static UsableGameInput createKeyboardInput(){
		final JoystickPart mainJoystick = FourKeyJoystick.newWASDJoystick();
		final InputPart rotateAxis, fireButton, startButton, slow, activatePowerup, pauseButton, backButton;
		final List<ControlOption> options = new ArrayList<>();

		rotateAxis = new GdxMouseAxis(false, 1.0f, createMouseMultiplier(options).getOptionValue(), createMouseInvert(options).getOptionValue());
		fireButton = new HighestPositionInputPart(new KeyInputPart(Input.Keys.SPACE), new KeyInputPart(Input.Buttons.LEFT, true));
		startButton = new KeyInputPart(Input.Keys.ENTER);
		slow = new KeyInputPart(Input.Keys.SHIFT_LEFT);
		activatePowerup = new KeyInputPart(Input.Keys.F);
		pauseButton = new HighestPositionInputPart(new KeyInputPart(Input.Keys.ESCAPE),
				new KeyInputPart(Input.Keys.ENTER));
		backButton = new KeyInputPart(Input.Keys.ESCAPE);

		return new DefaultUsableGameInput("Keyboard Controls",
				mainJoystick, rotateAxis, fireButton, slow, activatePowerup, startButton,
				pauseButton, backButton, null, options, Collections.emptyList())
		{{

			addChildren(Arrays.asList(mainJoystick, rotateAxis, fireButton, slow, activatePowerup, startButton, pauseButton, backButton),
					false, false);

		}};
	}

	public static UsableGameInput createTouchGyroInput() {
//		if(!Gdx.input.isPeripheralAvailable(Input.Peripheral.Gyroscope)){
//			throw new UnsupportedOperationException("Cannot create gyro input without gyroscope");
//		}

		final GdxTiltJoystick mainJoystick = new GdxTiltJoystick();
		final InputPart rotateAxis, fireButton, startButton, slow, activatePowerup, pauseBackButton;
		final ControllerRumble rumble;
		final List<ControlOption> options = new ArrayList<>(mainJoystick.getControlOptions());

		rotateAxis = new GdxMouseAxis(true, -5.0f, createMouseMultiplier(options).getOptionValue(),
				createMouseInvert(options).getOptionValue(), new Rectangle(.5f, 0, .5f, 1));
		fireButton = new GdxScreenTouchButton(new Rectangle(0, 0, .5f, 1));


		startButton = new KeyInputPart(Input.Keys.ENTER);
		slow = new KeyInputPart(Input.Keys.SHIFT_LEFT);

		OptionValue shakeThresholdValue = OptionValues.createDigitalRangedOptionValue(3, 16, 8);
		GdxShakeButton button = new GdxShakeButton(shakeThresholdValue);
		options.add(new ControlOption("Powerup Activate Shake Sensitivity",
				"How much you have to shake the device to activate the powerup in m/s^2",
				"controls.all.shake", shakeThresholdValue));
		activatePowerup = button;
		Gdx.input.setCatchBackKey(true);
		pauseBackButton = new KeyInputPart(Input.Keys.BACK);

		GdxRumble gdxRumble = new GdxRumble();
		options.addAll(gdxRumble.getControlOptions());
		rumble = gdxRumble;

		return new DefaultUsableGameInput("Keyboard Controls",
				mainJoystick, rotateAxis, fireButton, slow, activatePowerup, startButton,
				pauseBackButton, pauseBackButton, rumble, options, Collections.emptyList())
		{{

			addChildren(Arrays.asList(mainJoystick, rotateAxis, fireButton, slow, activatePowerup, startButton, pauseBackButton, rumble),
					false, false);

		}};
	}
}
