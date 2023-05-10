package me.retrodaredevil.game.trackshooter.input;

public interface RumbleAnalogControl {
	/**
	 * @return true if analog control is supported
	 */
	boolean isSupported();
	/**
	 * @param intensity The intensity of the rumble in range [0..1]
	 * @param time The time to last in millis. This should be {@code Long.MAX_VALUE} to last forever
	 */
	void setControl(double intensity, long time);
	class Defaults {
		public static final RumbleAnalogControl UNSUPPORTED_ANALOG = new RumbleAnalogControl() {
			@Override
			public boolean isSupported() {
				return false;
			}

			@Override
			public void setControl(double intensity, long time) {
				throw new UnsupportedOperationException();
			}
		};
	}
}
