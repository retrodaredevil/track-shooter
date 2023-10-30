package me.retrodaredevil.game.trackshooter.input.implementations;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

import java.util.Collection;
import java.util.Collections;

import me.retrodaredevil.controller.SimpleControllerPart;
import me.retrodaredevil.controller.options.ConfigurableControllerPart;
import me.retrodaredevil.controller.options.ControlOption;
import me.retrodaredevil.controller.options.OptionValue;
import me.retrodaredevil.controller.options.OptionValues;
import me.retrodaredevil.controller.output.ControllerRumble;
import me.retrodaredevil.game.trackshooter.input.RumbleAnalogControl;

// TODO This class was designed to support rumble on older Android devices,s but libGDX no longer makes that easy to support with the way
//   this class was originally designed. We should consider simplifying this class in the future
public class GdxRumble extends SimpleControllerPart implements ControllerRumble, ConfigurableControllerPart {
	private final OptionValue enableRumbleOption = OptionValues.createBooleanOptionValue(true);
	private final ControlOption enableRumbleControlOption = new ControlOption("Enable Rumble",
			"Should the rumble/vibrator be enabled",
			"controls.misc.rumble.enable", enableRumbleOption);


	/** The analog rumble. {@link RumbleAnalogControl#isSupported()} is guaranteed to be true */
	private final RumbleAnalogControl analogControl;


	private long vibrateUntil = 0;
	private boolean isRumbling = false;

	public GdxRumble(RumbleAnalogControl analogControl){
		this.analogControl = analogControl;
		if (!analogControl.isSupported()) {
			throw new IllegalArgumentException("Analog control must be supported to use this class (at this time)");
		}
	}

	private void cancel(){
		vibrateUntil = 0;
		analogControl.setControl(0.0, 0);
		isRumbling = false;
	}

	@Override
	protected void onUpdate() {
		super.onUpdate();
		if(vibrateUntil <= System.currentTimeMillis() || !enableRumbleOption.getBooleanOptionValue()){
			if (isRumbling) {
				cancel();
			}
		}
	}

	@Override
	public void rumbleForever(double intensity) {
		checkRange(intensity);
		checkEnabled();
		if(intensity == 0){
			cancel();
		} else {
			analogControl.setControl(intensity, Long.MAX_VALUE);
			vibrateUntil = Long.MAX_VALUE;
			isRumbling = true;
		}
	}

	@Override
	public void rumbleForever(double leftIntensity, double rightIntensity) {
		checkRange(leftIntensity);
		checkRange(rightIntensity);
		this.rumbleForever((leftIntensity + rightIntensity) / 2.0);
	}

	@Override
	public void rumbleTimeout(long millis, double intensity) {
		checkRange(intensity);
		checkEnabled();
		if(intensity == 0) {
			cancel();
		} else {
			final long now = System.currentTimeMillis();
			vibrateUntil = now + millis;
			analogControl.setControl(intensity, millis);
			isRumbling = true;
		}
	}
	@Override
	public void rumbleTimeout(long millis, double left, double right) {
		rumbleTimeout(millis, (left + right) / 2.0);
	}

	@Override
	public void rumbleTime(long millis, double intensity) {
		rumbleTimeout(millis, intensity);
	}

	@Override
	public void rumbleTime(long millis, double leftIntensity, double rightIntensity) {
		rumbleTime(millis, (leftIntensity + rightIntensity) / 2.0);
	}

	private static void checkRange(double check){
		if(check < 0){
			throw new IllegalArgumentException("intensity cannot be < 0");
		} else if(check > 1){
			throw new IllegalArgumentException("intensity cannot be > 1");
		}
	}
	private void checkEnabled(){
		if(!enableRumbleOption.getBooleanOptionValue()){
			throw new IllegalStateException("The rumble is not connected because it's not enabled! You can't use the gyro right now! Remember to check isConnected()");
		}
	}

	@Override
	public boolean isAnalogRumbleSupported() {
		return enableRumbleOption.getBooleanOptionValue();
	}
	@Override
	public boolean isAnalogRumbleNativelySupported() {
		return true;
	}

	@Override
	public boolean isLeftAndRightSupported() {
		return false;
	}

	@Override
	public boolean isConnected() {
		return Gdx.input.isPeripheralAvailable(Input.Peripheral.Vibrator) && enableRumbleOption.getBooleanOptionValue();
	}

	@Override
	public Collection<? extends ControlOption> getControlOptions() {
		return Collections.singletonList(enableRumbleControlOption);
	}
}
