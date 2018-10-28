package me.retrodaredevil.game.trackshooter.render.selection.options;

import com.badlogic.gdx.scenes.scene2d.ui.Button;

import java.util.Collection;

import me.retrodaredevil.controller.input.InputPart;
import me.retrodaredevil.controller.input.JoystickPart;
import me.retrodaredevil.game.trackshooter.render.selection.SelectAction;
import me.retrodaredevil.game.trackshooter.util.Size;

public class ButtonExitMenuSingleOption extends PlainActorSingleOption {
	private final Button button;
	public ButtonExitMenuSingleOption(Button button, Size size) {
		super(button, size);
		this.button = button;
	}

	@Override
	protected void onRequestActions(Collection<? super SelectAction> requestedActions) {
		super.onRequestActions(requestedActions);
		if(button.isPressed()){
			requestedActions.add(SelectAction.EXIT_MENU);
		}
	}

	@Override
	public void selectUpdate(float delta, JoystickPart selector, InputPart select, InputPart back, Collection<? super SelectAction> requestedActions) {
		super.selectUpdate(delta, selector, select, back, requestedActions);
		if(select.isPressed()){
			requestedActions.add(SelectAction.EXIT_MENU);
		}
	}
}
