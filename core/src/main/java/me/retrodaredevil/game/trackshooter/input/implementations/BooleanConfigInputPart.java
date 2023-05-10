package me.retrodaredevil.game.trackshooter.input.implementations;


import me.retrodaredevil.controller.input.AxisType;
import me.retrodaredevil.controller.input.implementations.AutoCachingInputPart;
import me.retrodaredevil.controller.options.ConfigurableControllerPart;
import me.retrodaredevil.controller.options.ControlOption;

import java.util.Collection;
import java.util.Collections;

public class BooleanConfigInputPart extends AutoCachingInputPart implements ConfigurableControllerPart {
	private final ControlOption controlOption;
	private final ShouldShowOption shouldShowOption;

	public BooleanConfigInputPart(ControlOption controlOption, ShouldShowOption shouldShowOption) {
		super(AxisType.DIGITAL);
		if(!controlOption.getOptionValue().isOptionValueBoolean()){
			throw new IllegalArgumentException("Must be a boolean option value!");
		}
		this.controlOption = controlOption;
		this.shouldShowOption = shouldShowOption;
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
		if(!shouldShowOption.shouldShowOption()){
			return Collections.emptyList();
		}
		return Collections.singleton(controlOption);
	}
	public interface ShouldShowOption {
		boolean shouldShowOption();
	}
}

