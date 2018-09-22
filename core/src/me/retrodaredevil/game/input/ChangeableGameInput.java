package me.retrodaredevil.game.input;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import me.retrodaredevil.controller.SimpleControllerInput;
import me.retrodaredevil.controller.input.InputPart;
import me.retrodaredevil.controller.input.JoystickPart;
import me.retrodaredevil.controller.options.ControlOption;
import me.retrodaredevil.controller.options.OptionValues;
import me.retrodaredevil.controller.options.RadioOptionOptionValue;
import me.retrodaredevil.controller.output.ControllerRumble;

/**
 * Represents a GameInput that can have alternate settings based on one physical device.
 * <p>
 * This should not have options for a keyboard and a controller because those are separate, this
 * is for having multiple different settings for a single device
 */
public class ChangeableGameInput extends SimpleControllerInput implements GameInput {

	private final ControlOption radioControlOption;
	private final RadioOptionOptionValue<UsableGameInput> gameInputOptionValue;

	/**
	 *
	 * @param gameInputs A list where each element in it is used as an option.
	 *                      Each element should be updated as well since this class will not update them
	 */
	public ChangeableGameInput(List<? extends UsableGameInput> gameInputs){
		this.gameInputOptionValue = OptionValues.createRadioOptionValue(gameInputs, 0);
		radioControlOption = new ControlOption("Game Input Options",
				"What type of control scheme do you want?", "controls.main.options", gameInputOptionValue);
	}


	private GameInput getCurrentGameInput(){
		return gameInputOptionValue.getRadioOptions().get((int) gameInputOptionValue.getOptionValue());
	}

	@Override
	public JoystickPart getMainJoystick() { return getCurrentGameInput().getMainJoystick(); }
	@Override
	public InputPart getRotateAxis() { return getCurrentGameInput().getRotateAxis(); }
	@Override
	public InputPart getFireButton() { return getCurrentGameInput().getFireButton(); }
	@Override
	public InputPart getSlowButton() { return getCurrentGameInput().getSlowButton(); }
	@Override
	public InputPart getActivatePowerup() { return getCurrentGameInput().getActivatePowerup(); }
	@Override
	public InputPart getStartButton() { return getCurrentGameInput().getStartButton(); }
	@Override
	public InputPart getPauseButton() { return getCurrentGameInput().getPauseButton(); }
	@Override
	public InputPart getBackButton() { return getCurrentGameInput().getBackButton(); }

	@Override
	public JoystickPart getSelectorJoystick() { return getCurrentGameInput().getSelectorJoystick(); }
	@Override
	public InputPart getEnterButton() { return getCurrentGameInput().getEnterButton(); }

	@Override
	public Collection<? extends ControlOption> getControlOptions() {
		List<ControlOption> r = new ArrayList<>();
		r.add(radioControlOption);
		Collection<? extends ControlOption> options = getCurrentGameInput().getControlOptions();
		r.addAll(options);

		return r;
	}


	@Override
	public boolean isConnected() { return getCurrentGameInput().isConnected(); }

	@Override
	public ControllerRumble getRumble() { return getCurrentGameInput().getRumble(); }

}
