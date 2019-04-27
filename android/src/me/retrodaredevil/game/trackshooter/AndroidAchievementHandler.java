package me.retrodaredevil.game.trackshooter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.games.AchievementsClient;
import com.google.android.gms.games.EventsClient;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.LeaderboardsClient;
import com.google.android.gms.games.achievement.AchievementBuffer;
import com.google.android.gms.tasks.Task;
import me.retrodaredevil.game.trackshooter.achievement.*;
import me.retrodaredevil.game.trackshooter.achievement.implementations.DefaultEventAchievementHandler;
import me.retrodaredevil.game.trackshooter.util.PreferencesGetter;
import me.retrodaredevil.game.trackshooter.util.Util;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;

import static java.util.Objects.requireNonNull;

class AndroidAchievementHandler implements AchievementHandler {
	private static final int REQUEST_SIGN_IN = 9001;
	private static final int REQUEST_IGNORE = 5001;

	private final Map<? extends GameEvent, String> eventIdMap;
	private final Map<? extends EventAchievement, String> eventAchievementIdMap;
	private final Map<String, ? extends EventAchievement> idEventAchievementMap;
	private final Map<? extends ManualAchievement, String> manualAchievementIdMap;
	private final String highScoreKey;
	private final Context context;
	private final AndroidApplication activity;
	private final GoogleSignInClient signInClient;


	private AchievementsClient achievementsClient = null;
	private EventsClient eventsClient = null;
	private LeaderboardsClient leaderboardsClient = null;

	AndroidAchievementHandler(
			Map<? extends GameEvent, String> eventIdMap, Map<? extends EventAchievement, String> eventAchievementIdMap,
			Map<? extends ManualAchievement, String> manualAchievementIdMap, String highScoreKey,
			AndroidApplication activity, GoogleSignInClient signInClient){
		this.eventIdMap = eventIdMap;
		this.eventAchievementIdMap = eventAchievementIdMap;
		this.idEventAchievementMap = Util.reverseMap(eventAchievementIdMap, new HashMap<String, EventAchievement>());
		this.manualAchievementIdMap = manualAchievementIdMap;
		this.highScoreKey = highScoreKey;
		this.activity = activity;
		this.signInClient = signInClient;

		this.context = activity.getContext();

		activity.addAndroidEventListener(this::onActivityResult);
	}
	// region Private Getters
	private GoogleSignInAccount getLastAccount(){
		return GoogleSignIn.getLastSignedInAccount(context);
	}

	/**
	 * @param account The account
	 * @return The EventClient or null if we aren't signed in
	 */
	private synchronized EventsClient getEventsClient(GoogleSignInAccount account){
		if(account == null){
			eventsClient = null;
			return null;
		}
		EventsClient currentEventsClient = eventsClient;
		if(currentEventsClient == null){
			EventsClient client = Games.getEventsClient(context, account);
			eventsClient = client;
			return client;
		}
		return currentEventsClient;
	}
	private synchronized AchievementsClient getAchievementsClient(GoogleSignInAccount account){
		if(account == null){
			eventsClient = null;
			return null;
		}
		AchievementsClient currentClient = achievementsClient;
		if(currentClient == null){
			AchievementsClient client = Games.getAchievementsClient(context, account);
			achievementsClient = client;

			return client;
		}
		return currentClient;
	}
	private synchronized LeaderboardsClient getLeaderboardsClient(GoogleSignInAccount account){
		if(account == null){
			leaderboardsClient = null;
			return null;
		}
		LeaderboardsClient currentClient = leaderboardsClient;
		if(currentClient == null){
			LeaderboardsClient client = Games.getLeaderboardsClient(context, account);
			leaderboardsClient = client;

			return client;
		}
		return currentClient;
	}
	// endregion

	// region sign in
	@Override
	public void signIn() {
		System.out.println("Going to sign in now isSignedIn: " + isSignedIn());
		activity.startActivityForResult(signInClient.getSignInIntent(), REQUEST_SIGN_IN);
	}
	private void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(requestCode == REQUEST_SIGN_IN){
			Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
			try {
				GoogleSignInAccount account = task.getResult(ApiException.class);
				doAccountSignIn(account);
			} catch (ApiException apiException) {
				String message = apiException.getMessage();
				if (message == null || message.trim().isEmpty()) {
					message = "Unable to connect";
				}

				onAccountLogout();

				new AlertDialog.Builder(activity)
						.setMessage(message)
						.setNeutralButton(android.R.string.ok, null)
						.show();
			}
		}
	}

	@Override
	public void logout() {
		if(isSignedIn()){
			signInClient.signOut()
					.addOnSuccessListener(result -> onAccountLogout())
					.addOnFailureListener(result -> {throw new IllegalStateException("Signing out should never fail!"); });
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
		getEventsClient(account);
	}
	void onAccountLogout(){
		System.out.println("Logging out");
		synchronized (this) {
			achievementsClient = null;
			eventsClient = null;
//			leaderboardsClient = null;
		}
	}
	// endregion

	@Override
	public synchronized void increment(GameEvent event, int amount) {
		final String value = eventIdMap.get(event);
		if(value == null){
			throw new NoSuchElementException("No value for event: " + event);
		}
		if(amount <= 0){
			throw new IllegalArgumentException("Amount must be > 0. amount: " + amount);
		}
		GoogleSignInAccount account = getLastAccount();
		if(account != null){
			final EventsClient eventsClient = requireNonNull(getEventsClient(account));
			eventsClient.increment(value, amount);
			System.out.println("Incrementing " + event + " by " + amount);
			final AchievementsClient achievementsClient = requireNonNull(getAchievementsClient(account));

			/*
			Go through each possible achievement. If that achievement is an EventAchievement and its GameEvent is event,
			increment it
			 */
//			requireNonNull(getAchievementsClient(account)).load(true).addOnSuccessListener(result -> {
//				AchievementBuffer buffer = requireNonNull(result.get());
//				for (Iterator<com.google.android.gms.games.achievement.Achievement> it = buffer.singleRefIterator(); it.hasNext(); ) {
//					com.google.android.gms.games.achievement.Achievement a = it.next();
//					String key = a.getAchievementId();
//					EventAchievement achievement = idEventAchievementMap.get(key);
//					if(achievement != null && achievement.getGameEvent() == event) {
//						final int steps = a.getCurrentSteps();
//						achievementsClient.incrementImmediate(key, amount).addOnSuccessListener(didConnect -> {
//							if(didConnect) {
//								final int newSteps = a.getCurrentSteps();
//								if(newSteps != steps + amount){
//									throw new IllegalStateException("newSteps should be: " + (steps + amount) + " but it's " + newSteps);
//								} else {
//									System.out.println("Good! newSteps is " + newSteps + " which is " + amount + " more than last time!");
//								}
//							} else {
//								System.out.println("Couldn't connect");
//							}
//						});
//						Integer stepsToReveal = achievement.getIncrementsForReveal();
//						if(stepsToReveal != null && steps >= stepsToReveal){
//							achievementsClient.reveal(key);
//						}
//					}
//				}
//			});
		}
	}

	@Override
	public void manualAchieve(ManualAchievement achievement) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void manualIncrement(ManualAchievement achievement) {

	}

	@Override
	public boolean isSupported(GameEvent event) {
		return eventIdMap.containsKey(event);
	}

	@Override
	public boolean isSupported(ManualAchievement achievement) {
		return false;
	}

	@Override
	public void showAchievements() {
		GoogleSignInAccount account = getLastAccount();
		if(account == null){
			throw new IllegalStateException("account is null! We cannot show achievements now! You should check isCurrentlyAbleToShowAchievements()!");
		}
		AchievementsClient client = requireNonNull(getAchievementsClient(account));

		Task<Intent> task = requireNonNull(client.getAchievementsIntent());
		task.addOnSuccessListener(result -> {
			activity.startActivityForResult(result, REQUEST_IGNORE);
		});
	}
	@Override public boolean isEverAbleToShowAchievements() { return true; }
	@Override public boolean isCurrentlyAbleToShowAchievements() { return isSignedIn(); }

	@Override
	public void showLeaderboards() {
		GoogleSignInAccount account = getLastAccount();
		if(account == null){
			throw new IllegalStateException("account is null! We cannot show leaderboard now! You should check isCurrentlyAbleToShowLeaderboards()!");
		}
		LeaderboardsClient client = requireNonNull(getLeaderboardsClient(account));

		Task<Intent> task = requireNonNull(client.getAllLeaderboardsIntent()); // TODO Do we want to just show the high score leaderboard?

		task.addOnSuccessListener(result -> activity.startActivityForResult(result, REQUEST_IGNORE));
	}
	@Override public boolean isEverAbleToShowLeaderboards() { return true; }
	@Override public boolean isCurrentlyAbleToShowLeaderboards() { return isSignedIn(); }

	@Override
	public void submitScore(int score) {
		LeaderboardsClient client = getLeaderboardsClient(getLastAccount());
		if(client != null) {
			client.submitScore(highScoreKey, score);
			System.out.println("Submit score: " + score);
		} else {
			System.out.println("Not submitting score: " + score + "!");
		}
	}

}
