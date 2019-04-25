package me.retrodaredevil.game.trackshooter;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.games.Games;
import me.retrodaredevil.game.trackshooter.achievement.EventAchievement;
import me.retrodaredevil.game.trackshooter.achievement.implementations.DefaultEventAchievement;
import me.retrodaredevil.game.trackshooter.achievement.implementations.DefaultGameEvent;
import me.retrodaredevil.game.trackshooter.input.RumbleAnalogControl;

import java.util.Collections;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;


public class AndroidLauncher extends AndroidApplication {

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
		final Map<DefaultGameEvent, String> eventMap = new EnumMap<>(DefaultGameEvent.class);
		eventMap.put(DefaultGameEvent.SHARKS_KILLED, getString(R.string.event_sharks_killed));
		eventMap.put(DefaultGameEvent.SNAKES_KILLED, getString(R.string.event_snakes_killed));
		eventMap.put(DefaultGameEvent.CARGO_SHIPS_PROTECTED, getString(R.string.event_mr_spaceship_protected));
		eventMap.put(DefaultGameEvent.POWER_UPS_COLLECTED, getString(R.string.event_powerups_collected));
		eventMap.put(DefaultGameEvent.FRUIT_CONSUMED, getString(R.string.event_fruit_consumed));
		eventMap.put(DefaultGameEvent.GAMES_COMPLETED, getString(R.string.event_games_completed));
		eventMap.put(DefaultGameEvent.SHOTS_FIRED, getString(R.string.event_shots_fired));

		final Map<EventAchievement, String> achievementMap = new HashMap<>();
		achievementMap.put(DefaultEventAchievement.FIRST_GAME, getString(R.string.achievement_first_game_completed));
		achievementMap.put(DefaultEventAchievement.COMPLETE_20_GAMES, getString(R.string.achievement_played_20_games));
		achievementMap.put(DefaultEventAchievement.COMPLETE_100_GAMES, getString(R.string.achievement_played_100_games));
		achievementMap.put(DefaultEventAchievement.SHARKS_KILLED_5, getString(R.string.achievement_5_sharks_killed));

		GoogleSignInClient client = GoogleSignIn.getClient(this,
				new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_GAMES_SIGN_IN)
						.requestScopes(Games.SCOPE_GAMES_LITE)
						.requestEmail()
						.build()
		);
		achievementHandler = new AndroidAchievementHandler(
				Collections.unmodifiableMap(eventMap), Collections.unmodifiableMap(achievementMap), Collections.emptyMap(),
				getContext(),
				this,
				client);
		initialize(new GameMain(rumbleAnalogControl, achievementHandler), config);
	}


	@Override
	protected void onResume() {
		super.onResume();
		achievementHandler.onResume();
	}
}
