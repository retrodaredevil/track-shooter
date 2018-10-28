package me.retrodaredevil.game.trackshooter.render.selection.options;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Align;

import java.util.Collection;

import me.retrodaredevil.controller.input.InputPart;
import me.retrodaredevil.controller.input.JoystickPart;
import me.retrodaredevil.controller.options.ControlOption;
import me.retrodaredevil.controller.options.OptionValue;
import me.retrodaredevil.game.trackshooter.render.RenderObject;
import me.retrodaredevil.game.trackshooter.render.selection.SelectAction;
import me.retrodaredevil.game.trackshooter.save.OptionSaver;
import me.retrodaredevil.game.trackshooter.util.ActorUtil;
import me.retrodaredevil.game.trackshooter.util.MathUtil;
import me.retrodaredevil.game.trackshooter.util.Size;

public class SliderSingleOption extends ControlOptionSingleOption {
	private static final float SLIDER_PERCENT_MULTIPLIER = .35f;
	private static final String STYLE_NAME = "small";
	private static final float KNOB_RADIUS = 25;
	private final RenderObject renderObject;

	private final Slider slider;
	private Label valueLabel = null;
	private Float sliderPercent = null;

	public SliderSingleOption(Size size, int playerIndex, ControlOption controlOption, OptionSaver optionSaver, RenderObject renderObject){
		super(size, playerIndex, controlOption, optionSaver);
		this.renderObject = renderObject;

		OptionValue value = controlOption.getOptionValue();

		slider = new Slider((float) value.getMinOptionValue(), (float) value.getMaxOptionValue(),
				value.isOptionAnalog() ? .05f : 1, false, renderObject.getUISkin());
		Slider.SliderStyle style = slider.getStyle();
		for(Drawable d : new Drawable[]{style.knob, style.knobDown, style.knobOver, style.disabledKnob}){
			if(d != null){
				d.setMinWidth(KNOB_RADIUS);
				d.setMinHeight(KNOB_RADIUS);
			}
		}
		// credit to https://badlogicgames.com/forum/viewtopic.php?t=12612
		slider.addListener(new InputListener() {
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				event.stop();
				return false;
			}
		});
//		for(Drawable d : new Drawable[]{style.knobBefore, style.knobAfter, style.disabledKnobBefore, style.disabledKnobAfter}){
//			if(d != null){
//				d.setMinHeight(10);
//			}
//		}
	}

	@Override
	protected void onInit(){
		super.onInit();
		OptionValue value = controlOption.getOptionValue();

		slider.setValue((float) value.getOptionValue());
		valueLabel = new Label("", renderObject.getUISkin(), STYLE_NAME);
		Size size = getSize();
		size.requireWidth();

		Table firstRow = new Table();
		container.add(firstRow);
		firstRow.add(valueLabel).width(size.ofWidth(.15f));
		firstRow.add().width(size.ofWidth(.05f));
		final Label label = new Label("" + controlOption.getLabel(), renderObject.getUISkin(), STYLE_NAME);
		label.setAlignment(Align.right);
		firstRow.add(label).width(size.ofWidth(.80f));

		container.row();

		Table secondRow = new Table();
		container.add(secondRow);
		secondRow.add(slider).width(size.ofWidth(1));

		container.row();
		container.add().height(20); // put some space
	}

	@Override
	public void onUpdate(){
		super.onUpdate();
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
	public void selectUpdate(float delta, JoystickPart selector, InputPart select, InputPart back, Collection<? super SelectAction> requestedActions) {
		ActorUtil.fireInputEvents(slider, InputEvent.Type.enter);
		if(selector.getXAxis().isDown()) {
			if (sliderPercent == null) {
				sliderPercent = slider.getPercent();
			}
			final double x = MathUtil.preservePow(selector.getX(), 3);
			sliderPercent += (float) x * delta * SLIDER_PERCENT_MULTIPLIER;
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
		ActorUtil.fireInputEvents(slider, InputEvent.Type.exit);
		sliderPercent = null;
	}
}
