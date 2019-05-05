package me.retrodaredevil.game.trackshooter.sound;

public interface VolumeControl {
	boolean isMuted();
	float getVolume();

	class Defaults {
		public static final VolumeControl MUTED = new VolumeControl() {
			@Override
			public boolean isMuted() {
				return true;
			}

			@Override
			public float getVolume() {
				return 0;
			}
		};
	}
}
