package me.retrodaredevil.game.trackshooter.render.selection.options;

import me.retrodaredevil.controller.options.ControlOption;
import me.retrodaredevil.controller.options.OptionValue;
import me.retrodaredevil.game.trackshooter.render.selection.ContainerSingleOption;
import me.retrodaredevil.game.trackshooter.save.OptionSaver;
import me.retrodaredevil.game.trackshooter.util.Size;

abstract class SimpleControlOptionSingleOption extends ContainerSingleOption implements ControlOptionSingleOption {

	private final Integer playerIndex;
	protected final ControlOption controlOption;
	protected final OptionSaver optionSaver;
	private boolean shouldSave = false;

	SimpleControlOptionSingleOption(Size size, Integer playerIndex, ControlOption controlOption, OptionSaver optionSaver){
		super(size);
		this.playerIndex = playerIndex;
		this.controlOption = controlOption;
		this.optionSaver = optionSaver;
	}

	@Override
	public ControlOption getControlOption() {
		return controlOption;
	}

	/**
	 * @return true if it should save. This should NOT be true if {@link #getSetValue()} is actively changing
	 */
	protected abstract boolean canSave();

	/**
	 * @return Should return the value that represents the position/state of the actor (checkbox, slider etc)
	 */
	protected abstract double getSetValue();

	protected abstract void setValueTo(double value);

	@Override
	protected void onInit() {
		super.onInit();
		optionSaver.loadControlOption(playerIndex, controlOption);
	}

	@Override
	protected void onUpdate() {
		super.onUpdate();

		OptionValue value = controlOption.getOptionValue();
		final double originalValue = value.getOptionValue();
		final double newValue = getSetValue();
		value.setOptionValue(newValue);

		if(originalValue != newValue){
			shouldSave = true;
		}
		if(shouldSave && canSave()){
			optionSaver.saveControlOption(playerIndex, controlOption);
			shouldSave = false;
		}
	}


	@Override
	public void resetOption() {
		super.resetOption();
		OptionValue option = controlOption.getOptionValue();
		option.setToDefaultOptionValue();
		setValueTo(option.getOptionValue());
        optionSaver.saveControlOption(playerIndex, controlOption);
	}

}
