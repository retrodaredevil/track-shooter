package me.retrodaredevil.game.trackshooter.render.parts.options;

import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import me.retrodaredevil.controller.options.ControlOption;
import me.retrodaredevil.controller.options.OptionValueObject;
import me.retrodaredevil.game.trackshooter.RenderObject;
import me.retrodaredevil.game.trackshooter.util.StringUtil;

public class OptionHandle {
	private final ControlOption controlOption;
	private final RenderObject renderObject;

	private final CheckBox checkBox;
	private final Slider slider;
	private Label valueLabel = null;
	private Actor container;

	private boolean shouldSave = false;

	OptionHandle(ControlOption controlOption, RenderObject renderObject){
		this.controlOption = controlOption;
		this.renderObject = renderObject;

		OptionValueObject value = controlOption.getOptionValueObject();
		if(value.isOptionValueBoolean()){
			checkBox = new CheckBox(controlOption.getLabel(), renderObject.getUISkin());
			slider = null;
		} else {
			checkBox = null;
			slider = new Slider((float) value.getMinOptionValue(), (float) value.getMaxOptionValue(),
					value.isOptionAnalog() ? .05f : 1, false, renderObject.getUISkin());
		}
	}

	/**
	 * Creates a new row after adding needed actors
	 * @param table The table
	 */
	public void init(Table table, Preferences preferences){
		OptionValueObject value = controlOption.getOptionValueObject();
		float savedValue = preferences.getFloat(getKey(), (float) value.getDefaultOptionValue());
//		System.out.println("saved value: " + savedValue);
		value.setOptionValue(savedValue);

		if(checkBox != null){
			checkBox.setChecked(value.getBooleanOptionValue());
			table.add(checkBox);
			this.container = checkBox;
		} else {
			slider.setValue(savedValue);
			Table container = new Table();
			this.container = container;
			valueLabel = new Label("", renderObject.getUISkin());

			container.add(new Label(getNumberText(value.getMinOptionValue()), renderObject.getUISkin())).width(50);
			container.add(slider).width(160);
			container.add(new Label(getNumberText(value.getMaxOptionValue()), renderObject.getUISkin())).width(50);
			container.add(valueLabel).width(70);
			container.add(new Label("" + controlOption.getLabel(), renderObject.getUISkin())).width(260);

			table.add(container).center();
		}
		table.row();
	}

	public void update(Preferences preferences){
		if(valueLabel != null) {
			valueLabel.setText("(" + getNumberText(controlOption.getOptionValueObject().getOptionValue()) + ")");
		}
		OptionValueObject value = controlOption.getOptionValueObject();
		double originalValue = value.getOptionValue();
		double newValue = (checkBox != null ? (checkBox.isChecked()?1:0) : slider.getValue());
		value.setOptionValue(newValue);
		if(originalValue != newValue){
			shouldSave = true;
		}
		if(shouldSave && !isOver()){
			preferences.putFloat(getKey(),(float) originalValue);
			preferences.flush();
			shouldSave = false;
		}
	}
	private String getNumberText(double number){
		OptionValueObject value = controlOption.getOptionValueObject();
		if(value.isOptionAnalog()){
			return "" + ((int) Math.round(number * 100)) + "%";
		}
		return "" + ((int) Math.round(number));
	}
	private String getKey(){
		return controlOption.getCategory() + "." + controlOption.getLabel();
	}
	private boolean isOver(){
		if(checkBox != null){
			return checkBox.isOver();
		}
		return slider.isDragging();
	}
	public void reset(){
		OptionValueObject value = controlOption.getOptionValueObject();
		value.setToDefaultOptionValue();
	}


	public void remove() {
		if(container == null){
			throw new NullPointerException("Cannot remove because container was not initialized.");
		}
		container.remove(); // TODO I don't think this will work. Might have to clear children
		System.out.println("removed OptionHandle");
	}
}
