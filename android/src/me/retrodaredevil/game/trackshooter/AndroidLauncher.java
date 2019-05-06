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
import me.retrodaredevil.game.trackshooter.account.achievement.EventAchievement;
import me.retrodaredevil.game.trackshooter.account.achievement.ManualAchievement;
import me.retrodaredevil.game.trackshooter.achievement.DefaultAchievement;
import me.retrodaredevil.game.trackshooter.achievement.DefaultEventAchievement;
import me.retrodaredevil.game.trackshooter.achievement.DefaultGameEvent;
import me.retrodaredevil.game.trackshooter.input.RumbleAnalogControl;
import me.retrodaredevil.game.trackshooter.util.PreferencesGetter;

import java.util.*;


public class AndroidLauncher extends AndroidApplication {


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
		eventMap.put(DefaultGameEvent.CARGO_SHIPS_UNPROTECTED, getString(R.string.event_mr_spaceship_unprotected));
		eventMap.put(DefaultGameEvent.POWER_UPS_COLLECTED, getString(R.string.event_powerups_collected));
		eventMap.put(DefaultGameEvent.FRUIT_CONSUMED, getString(R.string.event_fruit_consumed));
		eventMap.put(DefaultGameEvent.GAMES_COMPLETED, getString(R.string.event_games_completed));
		eventMap.put(DefaultGameEvent.REDIRECT_STARFISH, getString(R.string.event_starfish_redirected));

		final Map<EventAchievement, String> achievementMap = new HashMap<>();
		achievementMap.put(DefaultEventAchievement.FIRST_GAME, getString(R.string.achievement_first_game_completed));
		achievementMap.put(DefaultEventAchievement.COMPLETE_20_GAMES, getString(R.string.achievement_played_20_games));
		achievementMap.put(DefaultEventAchievement.COMPLETE_100_GAMES, getString(R.string.achievement_played_100_games));
		achievementMap.put(DefaultEventAchievement.COMPLETE_250_GAMES, getString(R.string.achievement_played_250_games));
		achievementMap.put(DefaultEventAchievement.COMPLETE_500_GAMES, getString(R.string.achievement_played_500_games));
		achievementMap.put(DefaultEventAchievement.COMPLETE_1000_GAMES, getString(R.string.achievement_played_1000_games));
		achievementMap.put(DefaultEventAchievement.COMPLETE_10000_GAMES, getString(R.string.achievement_played_10000_games));

		achievementMap.put(DefaultEventAchievement.SHARKS_KILLED_5, getString(R.string.achievement_5_sharks_killed));
		achievementMap.put(DefaultEventAchievement.SHARKS_KILLED_20, getString(R.string.achievement_20_sharks_killed));
		achievementMap.put(DefaultEventAchievement.SHARKS_KILLED_100, getString(R.string.achievement_100_sharks_killed));
		achievementMap.put(DefaultEventAchievement.SHARKS_KILLED_250, getString(R.string.achievement_250_sharks_killed));
		achievementMap.put(DefaultEventAchievement.SHARKS_KILLED_500, getString(R.string.achievement_500_sharks_killed));
		achievementMap.put(DefaultEventAchievement.SHARKS_KILLED_1000, getString(R.string.achievement_1000_sharks_killed));
		achievementMap.put(DefaultEventAchievement.SHARKS_KILLED_10000, getString(R.string.achievement_10000_sharks_killed));

		achievementMap.put(DefaultEventAchievement.SNAKES_KILLED_50, getString(R.string.achievement_50_snakes_killed));
		achievementMap.put(DefaultEventAchievement.CONSUME_50_FRUIT, getString(R.string.achievement_consume_50_pieces_of_fruit));
		achievementMap.put(DefaultEventAchievement.COLLECT_50_POWERUP, getString(R.string.achievement_50_powerups_collected));
		achievementMap.put(DefaultEventAchievement.PROTECT_MR_SPACESHIP_1, getString(R.string.achievement_protect_mr_spaceship));
		achievementMap.put(DefaultEventAchievement.PROTECT_MR_SPACESHIP_100, getString(R.string.achievement_protect_mr_spaceship_100_times));
		achievementMap.put(DefaultEventAchievement.REDIRECT_STARFISH_1, getString(R.string.achievement_redirect_the_starfish));
		achievementMap.put(DefaultEventAchievement.REDIRECT_STARFISH_50, getString(R.string.achievement_redirect_the_starfish_50_times));

		final Map<ManualAchievement, String> manualAchievementMap = new HashMap<>();
		manualAchievementMap.put(DefaultAchievement.CLEAR_LEVEL_5, getString(R.string.achievement_clear_level_5));
		manualAchievementMap.put(DefaultAchievement.CLEAR_LEVEL_10, getString(R.string.achievement_clear_level_10));
		manualAchievementMap.put(DefaultAchievement.CLEAR_LEVEL_30, getString(R.string.achievement_clear_level_30));

		GoogleSignInClient client = GoogleSignIn.getClient(this,
				new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_GAMES_SIGN_IN)
						.requestScopes(Games.SCOPE_GAMES_LITE)
						.build()
		);
		PreferencesGetter scorePreferencesGetter = GameMain.SCORE_PREFERENCSE_GETTER;

		GoogleAccountManager accountManager = new GoogleAccountManager(this, client);

		AndroidAchievementHandler achievementHandler = new AndroidAchievementHandler(
				Collections.unmodifiableMap(eventMap), Collections.unmodifiableMap(achievementMap), Collections.unmodifiableMap(manualAchievementMap),
				getString(R.string.leaderboard_high_score),
				this,
				accountManager);
		initialize(new GameMain(scorePreferencesGetter, rumbleAnalogControl, new AccountObject(accountManager, achievementHandler)), config);
		achievementHandler.setView(graphics.getView());

	}


	@Override
	protected void onResume() {
		super.onResume();
	}
}
