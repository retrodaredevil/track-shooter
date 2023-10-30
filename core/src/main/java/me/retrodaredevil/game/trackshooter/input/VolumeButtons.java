package me.retrodaredevil.game.trackshooter.input;

public interface VolumeButtons {
	boolean isSupported();

	void setActive(boolean active);
	boolean isActive();

	/** This value is guaranteed to either go up, or stay the same */
	long getVolumeUpCount();
	/** This value is guaranteed to either go up, or stay the same */
	long getVolumeDownCount();

	class Defaults {
		public static final VolumeButtons UNSUPPORTED_VOLUME_BUTTONS = new VolumeButtons() {

			@Override
			public boolean isSupported() {
				return false;
			}

			@Override
			public void setActive(boolean active) {
				throw new UnsupportedOperationException();
			}

			@Override
			public boolean isActive() {
				return false;
			}

			@Override
			public long getVolumeUpCount() {
				return 0;
			}

			@Override
			public long getVolumeDownCount() {
				return 0;
			}
		};
	}
}
