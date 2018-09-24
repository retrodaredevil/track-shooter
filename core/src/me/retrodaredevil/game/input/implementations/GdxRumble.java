package me.retrodaredevil.game.input.implementations;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import me.retrodaredevil.controller.SimpleControllerPart;
import me.retrodaredevil.controller.options.ConfigurableControllerPart;
import me.retrodaredevil.controller.options.ControlOption;
import me.retrodaredevil.controller.options.OptionValue;
import me.retrodaredevil.controller.options.OptionValues;
import me.retrodaredevil.controller.output.ControllerRumble;

public class GdxRumble extends SimpleControllerPart implements ControllerRumble, ConfigurableControllerPart {
	private final OptionValue simulateAnalogOption = OptionValues.createBooleanOptionValue(true);
	private final OptionValue enableRumbleOption = OptionValues.createBooleanOptionValue(true);
	private final ControlOption simulateAnalogControlOption = new ControlOption("Simulate Analog Rumble",
					"Should the rumble/vibrator vibrate on and off quickly to simulate analog rumble", "output.rumble.simulateAnalog", simulateAnalogOption);
	private final ControlOption enableRumbleControlOption = new ControlOption("Enable Rumble", "Should the rumble/vibrator be enabled", "output.rumble.enable", enableRumbleOption);


	private long vibrateUntil = 0;
	private VibratePattern vibratePattern;

	public GdxRumble(){
	}
	public GdxRumble(boolean simulateAnalog){
		simulateAnalogOption.setOptionValue(simulateAnalog ? 1 : 0);
	}

	private void cancel(){
		vibrateUntil = 0;
		if(vibratePattern != VibratePattern.OFF) {
			vibratePattern = VibratePattern.OFF;
			Gdx.input.cancelVibrate();
		}
	}

	@Override
	protected void onUpdate() {
		super.onUpdate();
		if(vibrateUntil <= System.currentTimeMillis() || !enableRumbleOption.getBooleanOptionValue()){
			cancel();
		}
	}
	private void requestPattern(VibratePattern pattern){
		if(pattern == VibratePattern.OFF){
			throw new IllegalArgumentException("This method cannot handle the OFF value. use cancel() instead");
		}
		if(pattern != vibratePattern){
			Gdx.input.cancelVibrate();
			if(pattern == VibratePattern.FULL || !simulateAnalogOption.getBooleanOptionValue()){
				Gdx.input.vibrate(Integer.MAX_VALUE);
			} else {
				Gdx.input.vibrate(pattern.pattern, 0);
			}
			vibratePattern = pattern;
			System.out.println("changed to pattern: " + pattern);
		}
	}

	@Override
	public void rumble(double amount) {
		checkRange(amount);
		checkEnabled();
		if(amount == 0){
			cancel();
		} else {
			requestPattern(VibratePattern.getPattern(amount));
			vibrateUntil = Long.MAX_VALUE;
		}
	}

	@Override
	public void rumble(double left, double right) {
		checkRange(left);
		checkRange(right);
		this.rumble((left + right) / 2.0);
	}

	@Override
	public void rumble(long millis, double amount) {
		checkRange(amount);
		checkEnabled();
		if(amount == 0) {
			cancel();
		} else {
			requestPattern(VibratePattern.getPattern(amount));
			vibrateUntil = System.currentTimeMillis() + millis;
		}
	}
	@Override
	public void rumble(long millis, double left, double right) {
		rumble(millis, (left + right) / 2.0);
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
	public boolean isTimingNativelyImplemented() {
		return true;
	}

	@Override
	public boolean isAnalogRumbleSupported() {
		return simulateAnalogOption.getBooleanOptionValue() && enableRumbleOption.getBooleanOptionValue();
	}
	@Override
	public boolean isAnalogRumbleNativelySupported() {
		return false;
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
		if(enableRumbleOption.getBooleanOptionValue()){
			return Arrays.asList(enableRumbleControlOption, simulateAnalogControlOption);
		}
		return Collections.singletonList(enableRumbleControlOption);
	}

	private enum VibratePattern{
		OFF(), FULL(), P90(new long[]{4, 30}), P70(new long[]{15, 20}), P50(new long[]{15, 15}), P35(new long[]{20, 8}), P10(new long[]{30, 3});

		private final long[] pattern;

		VibratePattern(long[] pattern){
			this.pattern = pattern;
		}
		VibratePattern(){
			this.pattern = null; // special pattern
		}
		public static VibratePattern getPattern(double intensity){
			checkRange(intensity);
			if(intensity == 1){
				return FULL;
			} else if(intensity >= .85){
				return P90;
			} else if(intensity >= .675){
				return P70;
			} else if(intensity >= .45){
				return P50;
			} else if(intensity >= .23){
				return P35;
			} else if(intensity > 0){
				return P10;
			}
			return OFF;
		}
	}
}
