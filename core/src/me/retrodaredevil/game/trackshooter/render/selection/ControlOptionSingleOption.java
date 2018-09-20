package me.retrodaredevil.game.trackshooter.render.selection;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Pools;

import me.retrodaredevil.controller.options.ControlOption;
import me.retrodaredevil.controller.options.OptionValue;
import me.retrodaredevil.game.trackshooter.save.OptionSaver;

public abstract class ControlOptionSingleOption implements SingleOption{

	protected final ControlOption controlOption;
	protected final OptionSaver optionSaver;
	private final Table container = new Table();

	private boolean initialized = false;
	private boolean shouldSave = false;

	protected ControlOptionSingleOption(ControlOption controlOption, OptionSaver optionSaver){
		this.controlOption = controlOption;
		this.optionSaver = optionSaver;
	}


	protected abstract boolean canSave();
	protected abstract double getSetValue();
	protected abstract void onInit(Table container);
	protected abstract void onUpdate(Table container);

	@Override
	public void renderUpdate(Table table) {
		if(!initialized){
			optionSaver.loadControlOption(controlOption);
			onInit(container);
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
			optionSaver.saveControlOption(controlOption);
			shouldSave = false;
		}

		onUpdate(container);
	}

	@Override
	public void reset() {
		controlOption.getOptionValue().setToDefaultOptionValue();
	}

	public static boolean fireInputEvents(Actor actor, InputEvent.Type... types){
		boolean eitherHandled = false;
		for(InputEvent.Type type : types){
			InputEvent event = Pools.obtain(InputEvent.class);
			event.setType(type);
			event.setButton(Input.Buttons.LEFT);

			actor.fire(event);
			if(!eitherHandled){
				eitherHandled = event.isHandled();
			}
			Pools.free(event);
		}
		return eitherHandled;
	}

	@Override
	public void remove() {
		container.remove();
	}
}
