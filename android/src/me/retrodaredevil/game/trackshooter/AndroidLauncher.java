package me.retrodaredevil.game.trackshooter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.games.Games;
import com.google.android.gms.tasks.Task;
import me.retrodaredevil.game.trackshooter.achievement.DefaultGameEvent;
import me.retrodaredevil.game.trackshooter.input.RumbleAnalogControl;

import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;


public class AndroidLauncher extends AndroidApplication {
	private static final int RC_SIGN_IN = 9001;

	private AndroidAchievementHandler achievementHandler;

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
		final Map<DefaultGameEvent, String> map = new EnumMap<>(DefaultGameEvent.class);
		map.put(DefaultGameEvent.SHARKS_KILLED, getString(R.string.event_sharks_killed));
		map.put(DefaultGameEvent.SNAKES_KILLED, getString(R.string.event_snakes_killed));
		map.put(DefaultGameEvent.CARGO_SHIPS_PROTECTED, getString(R.string.event_mr_spaceship_protected));
		map.put(DefaultGameEvent.POWER_UPS_COLLECTED, getString(R.string.event_powerups_collected));
		map.put(DefaultGameEvent.FRUIT_CONSUMED, getString(R.string.event_fruit_consumed));
		map.put(DefaultGameEvent.GAMES_COMPLETED, getString(R.string.event_games_completed));
		map.put(DefaultGameEvent.SHOTS_FIRED, getString(R.string.event_shots_fired));

		GoogleSignInClient client = GoogleSignIn.getClient(this,
				new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_GAMES_SIGN_IN)
						.requestScopes(Games.SCOPE_GAMES_LITE)
						.requestEmail()
						.build()
		);
		achievementHandler = new AndroidAchievementHandler(
				Collections.unmodifiableMap(map), getContext(),
				() -> startActivityForResult(client.getSignInIntent(), RC_SIGN_IN),
				client
		);
		initialize(
				new GameMain(rumbleAnalogControl, achievementHandler),
				config
		);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode == RC_SIGN_IN){
			Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
			try {
				GoogleSignInAccount account = task.getResult(ApiException.class);
				achievementHandler.doAccountSignIn(account);
			} catch (ApiException apiException) {
				String message = apiException.getMessage();
				if (message == null || message.trim().isEmpty()) {
					message = "Unable to connect";
				}

				achievementHandler.onAccountLogout();

				new AlertDialog.Builder(this)
						.setMessage(message)
						.setNeutralButton(android.R.string.ok, null)
						.show();
			}
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		achievementHandler.onResume();
	}
}
