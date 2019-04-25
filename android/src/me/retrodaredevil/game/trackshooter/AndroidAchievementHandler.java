package me.retrodaredevil.game.trackshooter;

import android.content.Context;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.games.AchievementsClient;
import com.google.android.gms.games.EventsClient;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.LeaderboardsClient;
import me.retrodaredevil.game.trackshooter.achievement.Achievement;
import me.retrodaredevil.game.trackshooter.achievement.AchievementHandler;
import me.retrodaredevil.game.trackshooter.achievement.GameEvent;

import java.util.Map;
import java.util.NoSuchElementException;

import static java.util.Objects.requireNonNull;

public class AndroidAchievementHandler implements AchievementHandler {

	private final Map<? extends GameEvent, String> eventIdMap;
	private final Context context;
	private final StartSignIn startSignIn;
	private final GoogleSignInClient signInClient;
	private boolean signedIn = false;

	private AchievementsClient achievementsClient;
	private EventsClient eventsClient;
	private LeaderboardsClient leaderboardsClient;

	public AndroidAchievementHandler(Map<? extends GameEvent, String> eventIdMap, Context context, StartSignIn startSignIn, GoogleSignInClient signInClient){
		this.eventIdMap = eventIdMap;
		this.context = context;
		this.startSignIn = startSignIn;
		this.signInClient = signInClient;

	}

	@Override
	public void signIn() {
		System.out.println("Going to sign in now isSignedIn: " + isSignedIn());
		startSignIn.startSignIn();

	}

	@Override
	public void logout() {
		if(isSignedIn()){
			signInClient.signOut();
		}
	}

	void onResume(){
		System.out.println("Going to silently sign in again");

		if(isSignedIn()){
			System.out.println("We're already signed in!");
		} else {
			signInClient.silentSignIn().addOnCompleteListener(accountTask -> {
				if (accountTask.isSuccessful()) {
					final GoogleSignInAccount account = requireNonNull(accountTask.getResult());
					doAccountSignIn(account);
					System.out.println("Successfully connected to google");
				} else {
					Exception exception = requireNonNull(accountTask.getException());
					if (exception instanceof ApiException) {
						System.err.println("Tried to connect to google. My best guess on this exception is that the app isn't signed correctly. Status code: " + ((ApiException) exception).getStatusCode());
					}
					System.err.println("Failed to sign in. Message: " + exception.getMessage());
					exception.printStackTrace();
				}
			});
		}
	}
	@Override
	public boolean isSignedIn(){
		return GoogleSignIn.getLastSignedInAccount(context) != null;
	}

	@Override
	public boolean isNeedsSignIn() {
		return true;
	}

	void doAccountSignIn(GoogleSignInAccount account){
		requireNonNull(account);
		System.out.println("Successfully signed in");
		achievementsClient = Games.getAchievementsClient(context, account);
		eventsClient = Games.getEventsClient(context, account);
		leaderboardsClient = Games.getLeaderboardsClient(context, account);
	}
	void onAccountLogout(){
		System.out.println("Logging out");
		achievementsClient = null;
		eventsClient = null;
		leaderboardsClient = null;
	}

	@Override
	public void increment(GameEvent event, int amount) {
		final String value = eventIdMap.get(event);
		if(value == null){
			throw new NoSuchElementException("No value for event: " + event);
		}
		eventsClient.increment(value, amount);
	}

	@Override
	public void achieve(Achievement achievement) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean isSupported(GameEvent event) {
		return eventIdMap.containsKey(event);
	}

	@Override
	public boolean isSupported(Achievement achievement) {
		return false;
	}

	interface StartSignIn {
		void startSignIn();
	}
}
