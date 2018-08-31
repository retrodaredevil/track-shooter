package me.retrodaredevil.game.trackshooter.render.parts.options;

import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import me.retrodaredevil.controller.options.ControlOption;
import me.retrodaredevil.controller.options.OptionValueObject;
import me.retrodaredevil.game.trackshooter.RenderObject;

public class OptionHandle {
	private final ControlOption controlOption;
	private final RenderObject renderObject;

	private final CheckBox checkBox;
	private final Slider slider;

	OptionHandle(ControlOption controlOption, RenderObject renderObject){
		this.controlOption = controlOption;
		this.renderObject = renderObject;

		OptionValueObject value = controlOption.getOptionValueObject();
		if(value.isOptionValueBoolean()){
			checkBox = new CheckBox(controlOption.getLabel(), renderObject.getUISkin());
			slider = null;
			checkBox.setChecked(value.getBooleanOptionValue());
		} else {
			checkBox = null;
			slider = new Slider((float) value.getMinOptionValue(), (float) value.getMaxOptionValue(), value.isOptionAnalog() ? .1f : 1, false, renderObject.getUISkin());
			slider.setValue((float) value.getDefaultOptionValue());
		}
	}

	/**
	 * Creates a new row after adding needed actors
	 * @param table The table
	 */
	public void init(Table table){
		if(checkBox != null){
			table.add(checkBox);
		} else {
			OptionValueObject value = controlOption.getOptionValueObject();
			table.add(new Label("" + value.getMinOptionValue(), renderObject.getUISkin()));
			table.add(slider);
			table.add(new Label("" + value.getMaxOptionValue(), renderObject.getUISkin()));
			table.add(new Label(controlOption.getLabel(), renderObject.getUISkin()));
		}
		table.row();
	}

	public void update(){
		OptionValueObject value = controlOption.getOptionValueObject();
		if(checkBox != null){
			value.setOptionValue(checkBox.isChecked() ? 1 : 0);
		} else {
			value.setOptionValue(slider.getValue());
		}
	}
	public void reset(){
		OptionValueObject value = controlOption.getOptionValueObject();
		value.setToDefaultOptionValue();
	}


}
