package me.retrodaredevil.game.trackshooter.input.implementations;

import static java.util.Objects.requireNonNull;

import me.retrodaredevil.controller.input.AxisType;
import me.retrodaredevil.controller.input.implementations.AutoCachingInputPart;
import me.retrodaredevil.controller.options.OptionValue;
import me.retrodaredevil.game.trackshooter.input.VolumeButtons;

public class VolumeButtonsInputPart extends AutoCachingInputPart {
	private static final int TIME_FOR_VOLUME_ACTIVE_TIMEOUT = 500; // ms

	private final VolumeButtons volumeButtons;
	private final PressCountGetter pressCountGetter;
	private final OptionValue enable;

	private Long lastCount = null;
	private long lastPositionRequest = 0;

	public VolumeButtonsInputPart(VolumeButtons volumeButtons, PressCountGetter pressCountGetter, OptionValue enable) {
		super(AxisType.DIGITAL);
		this.volumeButtons = requireNonNull(volumeButtons);
		this.pressCountGetter = requireNonNull(pressCountGetter);
		this.enable = requireNonNull(enable);
	}

	@Override
	protected void onUpdate() {
		super.onUpdate();
		if(!enable.getBooleanOptionValue() || lastPositionRequest + TIME_FOR_VOLUME_ACTIVE_TIMEOUT <= System.currentTimeMillis()){
			volumeButtons.setActive(false);
		}
	}

	@Override
	protected double calculatePosition() {
		long currentCount = pressCountGetter.getPressCount(volumeButtons);
		Long lastCount = this.lastCount;
		this.lastCount = currentCount;
		if (lastCount != null && lastCount < currentCount) {
			return 1.0;
		}
		return 0.0;
	}

	@Override
	public double getPosition() {
		lastPositionRequest = System.currentTimeMillis();
		volumeButtons.setActive(enable.getBooleanOptionValue()); // enable only if the option is enabled, otherwise disable
		return super.getPosition();
	}

	@Override
	public boolean isConnected() {
		return volumeButtons.isSupported() && volumeButtons.isActive();
	}


	public interface PressCountGetter {
		long getPressCount(VolumeButtons volumeButtons);
	}
}
