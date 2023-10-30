package me.retrodaredevil.game.trackshooter;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.KeyEvent;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;

import me.retrodaredevil.game.trackshooter.input.InputConfig;
import me.retrodaredevil.game.trackshooter.input.InputQuirk;
import me.retrodaredevil.game.trackshooter.input.RumbleAnalogControl;
import me.retrodaredevil.game.trackshooter.input.VolumeButtons;
import me.retrodaredevil.game.trackshooter.util.PreferencesGetter;


public abstract class BaseAndroidLauncher extends AndroidApplication {

	private boolean volumeCaptureEnabled = false;
	private long volumeUpCount = 0;
	private long volumeDownCount = 0;


	protected abstract AccountObject createAccountObject();
	protected abstract void postInitialize();

	protected InputConfig getInputConfig() {
		return new InputConfig(InputQuirk.NORMAL);
	}

	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		config.useGyroscope = true;
		config.useAccelerometer = true;
		config.useCompass = true;
		config.useRotationVectorSensor = true; // may not work on all devices
		final RumbleAnalogControl rumbleAnalogControl;
		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
			Vibrator vibrator = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);
			rumbleAnalogControl = new AndroidAnalogRumble(vibrator);
		} else {
			rumbleAnalogControl = RumbleAnalogControl.Defaults.UNSUPPORTED_ANALOG;
		}
		PreferencesGetter scorePreferencesGetter = GameMain.SCORE_PREFERENCSE_GETTER;

		initialize(new GameMain(scorePreferencesGetter, rumbleAnalogControl, volumeButtons, createAccountObject(), getInputConfig()), config);
		postInitialize();
	}


	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		/*
		https://stackoverflow.com/questions/27488119/pressing-volume-down-calls-the-onkeydown-late-while-volume-up-calls-it-in-time
		NOTE: The volume down key will be handled "late" on most devices, while the volume up key will sometimes be handled "late".
		This is because depending on the phone's configuration, a volume key in combination with the power button may be used for something different.

		The only solution to this is to disable the given shortcut on your phone. That will help with the input lag.
		In reality, the volume keys aren't the best things to use as buttons in the game, but this was fun to implement.
		 */
		if (volumeCaptureEnabled) {
			if (keyCode == KeyEvent.KEYCODE_VOLUME_UP) {
				if (event.getRepeatCount() == 0) {
					volumeUpCount++;
				}
				return true;
			} else if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {
				if (event.getRepeatCount() == 0) {
					volumeDownCount++;
				}
				return true;
			}
		}
		return super.onKeyDown(keyCode, event);
	}

	private final VolumeButtons volumeButtons = new VolumeButtons() {
		@Override
		public boolean isSupported() {
			return true;
		}

		@Override
		public void setActive(boolean active) {
			volumeCaptureEnabled = active;
		}

		@Override
		public boolean isActive() {
			return volumeCaptureEnabled;
		}

		@Override
		public long getVolumeUpCount() {
			return volumeUpCount;
		}

		@Override
		public long getVolumeDownCount() {
			return volumeDownCount;
		}
	};
}
