package me.retrodaredevil.controller.types.subtypes;

import me.retrodaredevil.controller.ControllerInput;
import me.retrodaredevil.controller.input.InputPart;

/**
 * A ControllerInput with a slider
 */
public interface SliderControllerInput extends ControllerInput {
	/**
	 * The recommended way to do this is that the returned InputPart doesn't have full range but it can
	 * go either way.
	 * <p>
	 * Even though the AxisType shouldn't be full, it is possible that it is. If it is, you will have
	 * to either use it or scale it to make the range [0, 1].
	 * <p>
	 * If the AxisType isn't full and you need a range of [-1, 1], you will have to scale it yourself as well.
	 * @return The slider on the controller which should be analog and shouldn't have a full range
	 */
	InputPart getSlider();
}
