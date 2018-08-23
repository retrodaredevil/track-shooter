package me.retrodaredevil.game.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

import java.util.Collection;
import java.util.Collections;

import me.retrodaredevil.controller.SimpleControllerPart;
import me.retrodaredevil.controller.options.ConfigurableControllerPart;
import me.retrodaredevil.controller.options.ControlOption;
import me.retrodaredevil.controller.options.OptionControllerPart;
import me.retrodaredevil.controller.output.ControllerRumble;

public class GdxRumble extends SimpleControllerPart implements ControllerRumble, OptionControllerPart, ConfigurableControllerPart {

	private final Collection<ControlOption> controlOptions = Collections.singletonList(
			new ControlOption("Simulate Analog Rumble",
					"Should the rumble/vibrator vibrate on and off quickly to simulate analog rumble", this));

	private boolean simulateAnalog;

	private long vibrateUntil = 0;
	private VibratePattern vibratePattern;

	public GdxRumble(){
		setToDefaultOptionValue();
	}
	public GdxRumble(boolean simulateAnalog){
		this.simulateAnalog = simulateAnalog;
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
		if(vibrateUntil <= System.currentTimeMillis()){
			cancel();
		}
	}
	private void requestPattern(VibratePattern pattern){
		if(pattern != vibratePattern){
			Gdx.input.cancelVibrate();
			if(pattern == VibratePattern.FULL){
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

	@Override
	public boolean isTimingNativelyImplemented() {
		return true;
	}

	@Override
	public boolean isAnalogRumbleSupported() {
		return simulateAnalog;
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
		return Gdx.input.isPeripheralAvailable(Input.Peripheral.Vibrator);
	}

	@Override
	public Collection<ControlOption> getControlOptions() {
		return controlOptions;
	}

	@Override
	public boolean isOptionAnalog() {
		return false;
	}

	@Override
	public double getMinOptionValue() {
		return 0;
	}

	@Override
	public double getMaxOptionValue() {
		return 1;
	}

	/**
	 * @param b 1 for true, 0 for false. If true, sets simulateAnalog to true
	 */
	@Override
	public void setOptionValue(double b) {
		this.simulateAnalog = b >= .5;
	}


	/**
	 * @return true if simulateAnalog==true, false otherwise
	 */
	@Override
	public double getOptionValue() { return simulateAnalog ? 1 : 0; }
	/** @return true because the default value of simulateAnalog is true*/
	@Override
	public double getDefaultOptionValue() { return 1; }

	/**
	 * Sets simulateAnalog to true
	 */
	@Override
	public void setToDefaultOptionValue() { simulateAnalog = true; }

	private enum VibratePattern{
		OFF(), FULL(), P90(new long[]{4, 30}), P70(new long[]{12, 25}), P50(new long[]{10, 18}), P35(new long[]{15, 15}), P10(new long[]{22, 15});

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
