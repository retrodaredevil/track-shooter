package me.retrodaredevil.game.trackshooter.render.selection.options;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Pools;

import me.retrodaredevil.controller.options.ControlOption;
import me.retrodaredevil.controller.options.OptionValue;
import me.retrodaredevil.game.trackshooter.render.selection.ContainerSingleOption;
import me.retrodaredevil.game.trackshooter.save.OptionSaver;

public abstract class ControlOptionSingleOption extends ContainerSingleOption {

	protected final ControlOption controlOption;
	protected final OptionSaver optionSaver;
	private boolean shouldSave = false;

	protected ControlOptionSingleOption(ControlOption controlOption, OptionSaver optionSaver){
		this.controlOption = controlOption;
		this.optionSaver = optionSaver;
	}


	protected abstract boolean canSave();
	protected abstract double getSetValue();

	@Override
	protected void onInit(Table container) {
		super.onInit(container);
		optionSaver.loadControlOption(controlOption);
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
			optionSaver.saveControlOption(controlOption);
			shouldSave = false;
		}
	}


	@Override
	public void reset() {
		super.reset();
		controlOption.getOptionValue().setToDefaultOptionValue();
	}

}
