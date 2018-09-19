package me.retrodaredevil.game.trackshooter.render.selection;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import java.util.Collection;

import me.retrodaredevil.controller.input.InputPart;
import me.retrodaredevil.controller.input.JoystickPart;
import me.retrodaredevil.controller.options.ControlOption;
import me.retrodaredevil.controller.options.OptionValue;
import me.retrodaredevil.game.trackshooter.RenderObject;
import me.retrodaredevil.game.trackshooter.render.parts.options.OptionMenu;

public class SliderSingleOption extends SimpleSingleOption{
	private static final float SLIDER_PERCENT_MULTIPLIER = .5f;
	private final RenderObject renderObject;

	private final Slider slider;
	private Label valueLabel = null;
	private Float sliderPercent = null;

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

	@Override
	public void selectUpdate(float delta, JoystickPart selector, InputPart select, InputPart back, Collection<SelectAction> requestedActions) {
		fireInputEvents(slider, InputEvent.Type.enter);
		if(selector.getXAxis().isDown()) {
			if (sliderPercent == null) {
				sliderPercent = slider.getPercent();
			}
			sliderPercent += (float) selector.getX() * delta * SLIDER_PERCENT_MULTIPLIER;
			if (sliderPercent < 0) {
				sliderPercent = 0f;
			}
			if (sliderPercent > 1) {
				sliderPercent = 1f;
			}

			final float distance = slider.getMaxValue() - slider.getMinValue();
			slider.setValue(slider.getMinValue() + distance * sliderPercent);
		}
	}

	@Override
	public void deselect() {
		fireInputEvents(slider, InputEvent.Type.exit);
		sliderPercent = null;
	}
}
