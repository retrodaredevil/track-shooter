package me.retrodaredevil.game.trackshooter.render.parts.options;

import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Array;

import java.util.Collection;

import me.retrodaredevil.controller.options.ControlOption;
import me.retrodaredevil.controller.options.OptionValue;
import me.retrodaredevil.controller.options.RadioOption;
import me.retrodaredevil.game.trackshooter.RenderObject;

public class DropDownSingleOption extends SimpleSingleOption {

	private final SelectBox<String> selectBox;
	private final Array<String> itemsArray;

	public DropDownSingleOption(ControlOption controlOption, RenderObject renderObject){
		super(controlOption);
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
	protected void onInit(Table container, OptionMenu optionMenu) {
		updateItems();
		container.add(selectBox);
		selectBox.setSelectedIndex((int) controlOption.getOptionValue().getOptionValue());
	}

	@Override
	protected void onUpdate(Table container, OptionMenu optionMenu) {
		updateItems();
	}
}
