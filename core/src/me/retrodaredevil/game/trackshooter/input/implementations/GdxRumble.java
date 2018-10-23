package me.retrodaredevil.game.trackshooter.input.implementations;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.LongStream;

import me.retrodaredevil.controller.SimpleControllerPart;
import me.retrodaredevil.controller.options.ConfigurableControllerPart;
import me.retrodaredevil.controller.options.ControlOption;
import me.retrodaredevil.controller.options.OptionValue;
import me.retrodaredevil.controller.options.OptionValues;
import me.retrodaredevil.controller.output.ControllerRumble;
import me.retrodaredevil.game.trackshooter.util.MathUtil;

public class GdxRumble extends SimpleControllerPart implements ControllerRumble, ConfigurableControllerPart {
	private final OptionValue simulateAnalogOption = OptionValues.createBooleanOptionValue(true);
	private final OptionValue enableRumbleOption = OptionValues.createBooleanOptionValue(true);
	private final ControlOption simulateAnalogControlOption = new ControlOption("Simulate Analog Rumble",
			"Should the rumble/vibrator vibrate on and off quickly to simulate analog rumble",
			"controls.misc.rumble.simulate_analog", simulateAnalogOption);
	private final ControlOption enableRumbleControlOption = new ControlOption("Enable Rumble",
			"Should the rumble/vibrator be enabled",
			"controls.misc.rumble.enable", enableRumbleOption);

	private final Map<VibratePattern, Map<Integer, long[]>> patternRepeatCache = new EnumMap<>(VibratePattern.class);


	private long vibrateUntil = 0;
	private VibratePattern vibratePattern;

	public GdxRumble(){
	}
	public GdxRumble(boolean simulateAnalog){
		simulateAnalogOption.setBooleanOptionValue(simulateAnalog);
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
		if(pattern == VibratePattern.MANUAL){
			throw new IllegalArgumentException("This method cannot handle the MANUAL value. You should deal with that yourself.");
		}
		if(pattern != vibratePattern){
			Gdx.input.cancelVibrate();
			if(pattern == VibratePattern.FULL || !simulateAnalogOption.getBooleanOptionValue()){
				Gdx.input.vibrate(Integer.MAX_VALUE);
			} else {
				Gdx.input.vibrate(pattern.pattern, 0);
			}
			vibratePattern = pattern;
//			System.out.println("changed to pattern: " + pattern);
		}
	}

	@Override
	public void rumbleForever(double intensity) {
		checkRange(intensity);
		checkEnabled();
		if(intensity == 0){
			cancel();
		} else {
			requestPattern(VibratePattern.getPattern(intensity));
			vibrateUntil = Long.MAX_VALUE;
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
			requestPattern(VibratePattern.getPattern(intensity));
			vibrateUntil = System.currentTimeMillis() + millis;
		}
	}
	@Override
	public void rumbleTimeout(long millis, double left, double right) {
		rumbleTimeout(millis, (left + right) / 2.0);
	}

	@Override
	public void rumbleTime(long millis, double intensity) {
		VibratePattern pattern = VibratePattern.getPattern(intensity);
		if(pattern == VibratePattern.OFF){
			cancel();
			return;
		}
		vibrateUntil = Long.MAX_VALUE; // we don't want to to cancel it
		vibratePattern = VibratePattern.MANUAL;
		if(pattern == VibratePattern.FULL || !simulateAnalogOption.getBooleanOptionValue()){
			Gdx.input.vibrate((int) millis);
			return;
		}
		// Basically, instead of turning the rumble off in an open loop, we use the exact
		// pattern that we want and create a new array that repeats it a certain number of times.
		// This can actually be a very resource intensive on some devices (so we use a cache) but it can vibrate
		// for the exact amount of time that we want it to.
		final long period = MathUtil.sum(pattern.pattern);
		final int repeat = (int) (millis / period);

		Gdx.input.vibrate(getAndCacheVibratePatternRepeat(pattern, repeat), -1);
	}
	private long[] getAndCacheVibratePatternRepeat(VibratePattern pattern, int repeat){
		Map<Integer, long[]> repeatMap = patternRepeatCache.get(pattern);
		if(repeatMap == null){
			repeatMap = new HashMap<>();
			patternRepeatCache.put(pattern, repeatMap);
		}
		final long[] cachedPattern = repeatMap.get(repeat);
		if(cachedPattern != null){
			return cachedPattern;
		}
		final long[] shortPattern = pattern.pattern;
		final long[] r = MathUtil.repeatNew(shortPattern, repeat * shortPattern.length);
		repeatMap.put(repeat, r);
		return r;
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
		OFF(), MANUAL(), FULL(), P90(new long[]{4, 30}), P70(new long[]{15, 20}), P50(new long[]{15, 15}), P35(new long[]{20, 8}), P10(new long[]{30, 3});

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
