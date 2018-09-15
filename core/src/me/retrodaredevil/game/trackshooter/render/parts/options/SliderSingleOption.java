package me.retrodaredevil.game.trackshooter.render.parts.options;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import me.retrodaredevil.controller.options.ControlOption;
import me.retrodaredevil.controller.options.OptionValue;
import me.retrodaredevil.game.trackshooter.RenderObject;

public class SliderSingleOption extends SimpleSingleOption{
	private final RenderObject renderObject;

	private final Slider slider;
	private Label valueLabel = null;

	SliderSingleOption(ControlOption controlOption, RenderObject renderObject){
		super(controlOption);
		this.renderObject = renderObject;

		OptionValue value = controlOption.getOptionValue();

		slider = new Slider((float) value.getMinOptionValue(), (float) value.getMaxOptionValue(),
				value.isOptionAnalog() ? .05f : 1, false, renderObject.getUISkin());
	}

	@Override
	protected void onInit(Table container, OptionMenu optionMenu){
		OptionValue value = controlOption.getOptionValue();

		slider.setValue((float) value.getOptionValue());
		valueLabel = new Label("", renderObject.getUISkin());

		container.add(valueLabel).width(80);
		container.add(slider).width(120);
		container.add(new Label("" + controlOption.getLabel(), renderObject.getUISkin())).width(200);
	}

	@Override
	public void onUpdate(Table table, OptionMenu optionMenu){
		valueLabel.setText(getNumberText(controlOption.getOptionValue().getOptionValue()));
	}

	@Override
	protected double getSetValue() {
		return slider.getValue();
	}

	private String getNumberText(double number){
		OptionValue value = controlOption.getOptionValue();
		if(value.isOptionAnalog()){
			return "" + ((int) Math.round(number * 100)) + "%";
		}
		return "" + ((int) Math.round(number));
	}
	@Override
	protected boolean canSave(){
		return !slider.isDragging();
	}

}
