package me.retrodaredevil.game.trackshooter.sound;

import me.retrodaredevil.controller.options.OptionValue;

public class OptionValueVolumeControl implements VolumeControl {

	private final OptionValue optionValue;

	public OptionValueVolumeControl(OptionValue optionValue) {
		this.optionValue = optionValue;
	}

	@Override
	public boolean isMuted() {
		return optionValue.getOptionValue() == 0;
	}

	@Override
	public float getVolume() {
		return (float) optionValue.getOptionValue();
	}
}
