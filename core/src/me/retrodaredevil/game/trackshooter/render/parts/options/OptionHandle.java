package me.retrodaredevil.game.trackshooter.render.parts.options;

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
			slider = new Slider((float) value.getMinOptionValue(), (float) value.getMaxOptionValue(),
					value.isOptionAnalog() ? .1f : 1, false, renderObject.getUISkin());
			slider.setValue((float) value.getOptionValue());
		}
	}

	/**
	 * Creates a new row after adding needed actors
	 * @param table The table
	 */
	public void init(Table table){
		if(checkBox != null){
			table.add(checkBox);
			this.container = checkBox;
		} else {
			OptionValueObject value = controlOption.getOptionValueObject();
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

	public void update(){
		if(valueLabel != null) {
			valueLabel.setText("(" + getNumberText(controlOption.getOptionValueObject().getOptionValue()) + ")");
		}
		OptionValueObject value = controlOption.getOptionValueObject();
		if(checkBox != null){
			value.setOptionValue(checkBox.isChecked() ? 1 : 0);
		} else {
			value.setOptionValue(slider.getValue());
		}
	}
	private String getNumberText(double number){
		OptionValueObject value = controlOption.getOptionValueObject();
		if(value.isOptionAnalog()){
			return "" + ((int) Math.round(number * 100)) + "%";
		}
		return "" + ((int) Math.round(number));
	}
	public void reset(){
		OptionValueObject value = controlOption.getOptionValueObject();
		value.setToDefaultOptionValue();
	}


	public void remove() {
		if(container == null){
			throw new NullPointerException("Cannot remove because container was not initialized.");
		}
		container.remove();
		System.out.println("removed OptionHandle");
	}
}
