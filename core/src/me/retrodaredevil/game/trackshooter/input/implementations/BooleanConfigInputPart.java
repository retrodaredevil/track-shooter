package me.retrodaredevil.game.trackshooter.input.implementations;


import me.retrodaredevil.controller.input.AutoCachingInputPart;
import me.retrodaredevil.controller.input.AxisType;
import me.retrodaredevil.controller.options.ConfigurableControllerPart;
import me.retrodaredevil.controller.options.ControlOption;
import me.retrodaredevil.controller.output.ControllerRumble;

import java.util.Collection;
import java.util.Collections;

public class BooleanConfigInputPart extends AutoCachingInputPart implements ConfigurableControllerPart {
	private final ControlOption controlOption;
	private final ControllerRumble rumble;

	public BooleanConfigInputPart(ControlOption controlOption, ControllerRumble controllerRumble) {
		super(AxisType.DIGITAL);
		this.controlOption = controlOption;
		this.rumble = controllerRumble;
	}

	@Override
	protected double calculatePosition() {
		return controlOption.getOptionValue().getBooleanOptionValue() ? 1 : 0;
	}

	@Override
	public boolean isConnected() {
		return true;
	}

	@Override
	public Collection<? extends ControlOption> getControlOptions() {
		if(!rumble.isConnected()){
			return Collections.emptyList();
		}
		return Collections.singleton(controlOption);
	}
}

