package me.retrodaredevil.controller.output;

import me.retrodaredevil.controller.ControllerPart;

public interface ControllerRumble extends ControllerPart {
	/**
	 * Rumbles forever until stopped by: calling this again with amount = 0 or by calling
	 * {@link #rumble(long, double)} and it will turn off after millis milliseconds or if amount = 0, will turn off
	 * <p>
	 * It is recommended to use {@link #rumble(long, double)} or {@link #rumble(long, double, double)} because
	 * they provide the same features except if they stopped getting called continuously they will reset the rumble
	 * @param amount The rumble intensity. If 0 stops rumble
	 */
	void rumble(double amount);
	/**
	 * Rumbles forever until stopped by: calling {@link #rumble(double)} with amount = 0 or by calling
	 * {@link #rumble(long, double)} and it will turn off after millis milliseconds or if amount = 0, will turn off
	 * <p>
	 * It is recommended to use {@link #rumble(long, double)} or {@link #rumble(long, double, double)} because
	 * they provide the same features except if they stopped getting called continuously they will reset the rumble
	 * @param left The left rumble intensity range [0, 1]
	 * @param right The left rumble intensity range [0, 1]
	 */
	void rumble(double left, double right);

	/**
	 * @param millis Amount of time for rumble to last (milliseconds)
	 * @param left Left rumble intensity range: [0, 1]
	 * @param right Right rumble intensity range: [0, 1]
	 */
	void rumble(long millis, double left, double right);

	/**
	 * @param millis Amount of time for rumble to last (milliseconds)
	 * @param amount Rumble intensity range: [0, 1]
	 */
	void rumble(long millis, double amount);

	/** @return true if underlying API this is uses has support for how long the rumble should stay on, false otherwise. */
	boolean isTimingNativelyImplemented();
	/** @return true if analog rumble is supported at all, false if the intensity will only be interpreted as on or off*/
	boolean isAnalogRumbleSupported();
	/** This will be different than {@link #isAnalogRumbleSupported()} when isAnalogRumbleSupported() == true and
	 * this is false. When that is the case, it means that this still supports analog calls, but may implement
	 * it using something like PWM (Pulse Width Modulation) or something else
	 * @return true if analog rumble is supported, false if the rumble/vibrator can only be on or off */
	boolean isAnalogRumbleNativelySupported();
	/** @return true if the controller allows you to control the left and right individually, false
	 * 			if it doesn't allow that or if there is only one rumble. */
	boolean isLeftAndRightSupported();
}
