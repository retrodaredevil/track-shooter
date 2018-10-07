package me.retrodaredevil.game.trackshooter.render.selection.options;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Array;

import java.util.Collection;

import me.retrodaredevil.controller.input.InputPart;
import me.retrodaredevil.controller.input.JoystickPart;
import me.retrodaredevil.controller.options.ControlOption;
import me.retrodaredevil.controller.options.OptionValue;
import me.retrodaredevil.controller.options.RadioOption;
import me.retrodaredevil.game.trackshooter.render.RenderObject;
import me.retrodaredevil.game.trackshooter.render.selection.ContainerSingleOption;
import me.retrodaredevil.game.trackshooter.render.selection.SingleOption;
import me.retrodaredevil.game.trackshooter.save.OptionSaver;

public class DropDownSingleOption extends ControlOptionSingleOption {

	private final SelectBox<String> selectBox;
	private final Array<String> itemsArray;
	private boolean isShown = false;

	public DropDownSingleOption(ControlOption controlOption, OptionSaver optionSaver, RenderObject renderObject){
		super(controlOption, optionSaver);
		OptionValue value = controlOption.getOptionValue();
		if(!value.isOptionValueRadio()){
			throw new IllegalArgumentException("Cannot create a " + getClass().getSimpleName() + " with an OptionValue that is not 'radio'!");
		}
		Collection<? extends RadioOption> options = value.getRadioOptions();
		if(options.isEmpty()){
			throw new IllegalArgumentException("Cannot create radio option when the options are empty!");
		}
		selectBox = new SelectBox<>(renderObject.getUISkin());
		itemsArray = new Array<>(options.size());
	}

	@Override
	protected boolean canSave() {
		return true;
	}

	@Override
	protected double getSetValue() {
		return selectBox.getSelectedIndex();
	}
	private void updateItems(){
		itemsArray.clear();
		Collection<? extends RadioOption> options = controlOption.getOptionValue().getRadioOptions();
		for(RadioOption option : options){
			itemsArray.add(option.getRadioOptionName());
		}
		selectBox.setItems(itemsArray);
//		System.out.println("itemsArray size:" + itemsArray.size + " getItems() size: " + selectBox.getItems().size + " options.size(): " + options.size());
	}

	@Override
	protected void onInit(Table container) {
		super.onInit(container);
		updateItems();
		container.add(selectBox);
		selectBox.setSelectedIndex((int) controlOption.getOptionValue().getOptionValue());
	}

	@Override
	protected void onUpdate(Table container) {
		super.onUpdate(container);
		updateItems();
	}

	private void show(){
		selectBox.showList();
		isShown = true;
	}
	private void hide(){
		selectBox.hideList();
		isShown = false;
	}

	@Override
	public void selectUpdate(float delta, JoystickPart selector, InputPart select, InputPart back, Collection<SingleOption.SelectAction> requestedActions) {
		ContainerSingleOption.fireInputEvents(selectBox, InputEvent.Type.enter);
		// The reason we have the use the 'isShown' variable, is that in a SelectBox, pressing escape
		// (sometimes mapped to the back button) closes the drop down (IT'S FREAKING HARD CODED)
		// and because of that, we think it's already down on the frame isPressed() for 'back' returns true.
		// Using isShown is just an extra precaution to make sure it does what we want
		if(isShown != selectBox.getList().isTouchable()){
			if(isShown){
				show();
			} else {
				hide();
			}
		}
		if(isShown){
			requestedActions.clear();
			if(back.isPressed()){
				hide();
			} else if(select.isPressed()){
				ContainerSingleOption.fireInputEvents(selectBox, InputEvent.Type.touchDown);
				ContainerSingleOption.fireInputEvents(selectBox, InputEvent.Type.touchUp);
				hide();
			} else if(selector.getYAxis().isPressed()){
				final int y = selector.getYAxis().getDigitalPosition();
				final int currentIndex = selectBox.getSelectedIndex();
				final int size = selectBox.getItems().size;

				final int newIndex = currentIndex - y;
				if(newIndex >= 0 && newIndex < size){
					selectBox.setSelectedIndex(newIndex);
				}
			}
		} else {
			if(select.isPressed()){
				show();
			}
		}

	}

	@Override
	public void deselect() {
		hide();
		ContainerSingleOption.fireInputEvents(selectBox, InputEvent.Type.exit);
	}
}
