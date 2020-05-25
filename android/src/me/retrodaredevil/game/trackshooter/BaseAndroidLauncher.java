package me.retrodaredevil.game.trackshooter;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;

import me.retrodaredevil.game.trackshooter.input.RumbleAnalogControl;
import me.retrodaredevil.game.trackshooter.util.PreferencesGetter;


public abstract class BaseAndroidLauncher extends AndroidApplication {

	protected abstract AccountObject createAccountObject();
	protected abstract void postInitialize();

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

		initialize(new GameMain(scorePreferencesGetter, rumbleAnalogControl, createAccountObject()), config);

	}


}
