package me.retrodaredevil.game.trackshooter.render.selection.options;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.utils.Align;

import java.util.Collection;

import me.retrodaredevil.controller.input.InputPart;
import me.retrodaredevil.controller.input.JoystickPart;
import me.retrodaredevil.controller.options.ControlOption;
import me.retrodaredevil.game.trackshooter.render.RenderObject;
import me.retrodaredevil.game.trackshooter.save.OptionSaver;
import me.retrodaredevil.game.trackshooter.util.ActorUtil;
import me.retrodaredevil.game.trackshooter.util.Size;

public class CheckBoxSingleOption extends ControlOptionSingleOption {

	private final CheckBox checkBox;

	public CheckBoxSingleOption(Size size, int playerIndex, ControlOption controlOption, OptionSaver optionSaver, RenderObject renderObject){
		super(size, playerIndex, controlOption, optionSaver);

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
	protected void onInit() {
		super.onInit();
		container.add(checkBox);
		checkBox.setChecked(controlOption.getOptionValue().getBooleanOptionValue());
	}

	@Override
	protected void onUpdate() {
		super.onUpdate();
		checkBox.getLabel().setAlignment(Align.right);
		if(getSize().hasWidth()) {
			checkBox.getLabelCell().width(getSize().ofWidth(1) - checkBox.getImage().getWidth());
		}
	}

	@Override
	public void selectUpdate(float delta, JoystickPart selector, InputPart select, InputPart back, Collection<? super SelectAction> requestedActions) {
		ActorUtil.fireInputEvents(checkBox, InputEvent.Type.enter);
		if(select.isPressed()){
			checkBox.toggle();
		}
	}

	@Override
	public void deselect() {
		ActorUtil.fireInputEvents(checkBox, InputEvent.Type.exit);
	}
}
