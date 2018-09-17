package me.retrodaredevil.game.trackshooter.render.parts.options;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import java.util.Collection;

import me.retrodaredevil.controller.input.InputPart;
import me.retrodaredevil.controller.input.JoystickPart;
import me.retrodaredevil.controller.options.ControlOption;
import me.retrodaredevil.game.trackshooter.RenderObject;

public class CheckBoxSingleOption extends SimpleSingleOption {

	private final CheckBox checkBox;

	public CheckBoxSingleOption(ControlOption controlOption, RenderObject renderObject){
		super(controlOption);

		checkBox = new CheckBox(controlOption.getLabel(), renderObject.getUISkin());
	}

	@Override
	protected boolean canSave() {
		return !checkBox.isPressed();
	}

	@Override
	protected double getSetValue() {
		return checkBox.isChecked() ? 1 : 0;
	}

	@Override
	protected void onInit(Table container, OptionMenu optionMenu) {
		container.add(checkBox);
		checkBox.setChecked(controlOption.getOptionValue().getBooleanOptionValue());
	}

	@Override
	protected void onUpdate(Table container, OptionMenu optionMenu) {
	}

	@Override
	public void selectUpdate(float delta, JoystickPart selector, InputPart select, InputPart back, Collection<SelectAction> requestedActions) {
		fireInputEvents(checkBox, InputEvent.Type.enter);
		if(select.isPressed()){
			checkBox.toggle();
		}
	}

	@Override
	public void deselect() {
		fireInputEvents(checkBox, InputEvent.Type.exit);
	}
}
