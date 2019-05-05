package me.retrodaredevil.game.trackshooter.util;

import me.retrodaredevil.controller.options.ConfigurableObject;
import me.retrodaredevil.controller.options.ControlOption;

import java.util.*;

public class ImmutableConfigurableObject implements ConfigurableObject {

	private final List<ControlOption> controlOptions;

	public ImmutableConfigurableObject(Collection<? extends ControlOption> controlOptions) {
		this.controlOptions = Collections.unmodifiableList(new ArrayList<>(controlOptions));
	}
	public ImmutableConfigurableObject(ControlOption... controlOptions){
		this.controlOptions = Arrays.asList(controlOptions);
	}

	@Override
	public Collection<? extends ControlOption> getControlOptions() {
		return controlOptions;
	}
}
