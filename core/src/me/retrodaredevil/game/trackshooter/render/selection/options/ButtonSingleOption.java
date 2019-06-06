package me.retrodaredevil.game.trackshooter.render.selection.options;

import com.badlogic.gdx.scenes.scene2d.ui.Button;

import java.util.Collection;
import java.util.Set;

import me.retrodaredevil.controller.input.InputPart;
import me.retrodaredevil.controller.input.JoystickPart;
import me.retrodaredevil.game.trackshooter.render.selection.SelectAction;
import me.retrodaredevil.game.trackshooter.util.Size;

public class ButtonSingleOption extends PlainActorSingleOption {
	private final Button button;
	private final OnButtonPress onButtonPress;
	private boolean wasDown = false;

	public ButtonSingleOption(Button button, Size size, OnButtonPress onButtonPress) {
		super(button, size);
		this.button = button;
		this.onButtonPress = onButtonPress;
	}

	@Override
	protected void onRequestActions(Collection<? super SelectAction> requestedActions) {
		super.onRequestActions(requestedActions);
		boolean down = button.isPressed();
		if(!wasDown && down){
			onButtonPress.onButtonPress(requestedActions);
		}
		wasDown = down;
	}

	@Override
	public void selectUpdate(float delta, JoystickPart selector, InputPart select, InputPart back, Set<? super SelectAction> requestedActions) {
		super.selectUpdate(delta, selector, select, back, requestedActions);
		if(select.isJustPressed()){
			onButtonPress.onButtonPress(requestedActions);
		}
	}

	public interface OnButtonPress {
		void onButtonPress(Collection<? super SelectAction> requestingActions);
	}
}
