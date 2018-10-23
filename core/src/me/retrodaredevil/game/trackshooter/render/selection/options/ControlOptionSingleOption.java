package me.retrodaredevil.game.trackshooter.render.selection.options;

import com.badlogic.gdx.scenes.scene2d.ui.Table;

import me.retrodaredevil.controller.options.ControlOption;
import me.retrodaredevil.controller.options.OptionValue;
import me.retrodaredevil.game.trackshooter.render.selection.ContainerSingleOption;
import me.retrodaredevil.game.trackshooter.save.OptionSaver;
import me.retrodaredevil.game.trackshooter.util.Size;

public abstract class ControlOptionSingleOption extends ContainerSingleOption {

	private final int playerIndex;
	protected final ControlOption controlOption;
	protected final OptionSaver optionSaver;
	private boolean shouldSave = false;

	protected ControlOptionSingleOption(Size size, int playerIndex, ControlOption controlOption, OptionSaver optionSaver){
		super(size);
		this.playerIndex = playerIndex;
		this.controlOption = controlOption;
		this.optionSaver = optionSaver;
	}


	protected abstract boolean canSave();
	protected abstract double getSetValue();

	@Override
	protected void onInit(Table container) {
		super.onInit(container);
		optionSaver.loadControlOption(playerIndex, controlOption);
	}

	@Override
	protected void onUpdate(Table container) {
		super.onUpdate(container);

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
	public void reset() {
		super.reset();
		controlOption.getOptionValue().setToDefaultOptionValue();
	}

}
