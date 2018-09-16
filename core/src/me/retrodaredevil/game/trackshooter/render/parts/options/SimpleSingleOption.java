package me.retrodaredevil.game.trackshooter.render.parts.options;

import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Array;

import me.retrodaredevil.controller.options.ControlOption;
import me.retrodaredevil.controller.options.OptionValue;

public abstract class SimpleSingleOption implements SingleOption{

	protected final ControlOption controlOption;
	private final Table container = new Table();

	private boolean initialized = false;
	private boolean shouldSave = false;

	protected SimpleSingleOption(ControlOption controlOption){
		this.controlOption = controlOption;
	}


	protected abstract boolean canSave();
	protected abstract double getSetValue();
	protected abstract void onInit(Table container, OptionMenu optionMenu);
	protected abstract void onUpdate(Table container, OptionMenu optionMenu);

	@Override
	public void update(Table table, OptionMenu optionMenu) {
		if(!initialized){
			optionMenu.loadControlOption(controlOption);
			onInit(container, optionMenu);
		}
		initialized = true;
		if(container.getParent() != table){
			table.add(container);
			table.row();
		}

		OptionValue value = controlOption.getOptionValue();
		final double originalValue = value.getOptionValue();
		final double newValue = getSetValue();
		value.setOptionValue(newValue);

		if(originalValue != newValue){
			shouldSave = true;
		}
		if(shouldSave && canSave()){
			optionMenu.saveControlOption(controlOption);
			shouldSave = false;
		}

		onUpdate(container, optionMenu);
	}

	@Override
	public void reset() {
		controlOption.getOptionValue().setToDefaultOptionValue();
	}

	@Override
	public void remove() {
		container.remove();
	}


}
