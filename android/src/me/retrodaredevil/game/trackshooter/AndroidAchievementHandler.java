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
import com.google.android.gms.tasks.Task;
import me.retrodaredevil.game.trackshooter.achievement.*;
import me.retrodaredevil.game.trackshooter.achievement.implementations.DefaultEventAchievementHandler;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

import static java.util.Objects.requireNonNull;

class AndroidAchievementHandler implements AchievementHandler {
	private static final int REQUEST_SIGN_IN = 9001;
	private static final int REQUEST_IGNORE = 5001;

	private final Map<? extends GameEvent, String> eventIdMap;
	private final Map<? extends EventAchievement, String> eventAchievementIdMap;
	private final Map<? extends ManualAchievement, String> manualAchievementIdMap;
	private final Context context;
	private final AndroidApplication activity;
	private final GoogleSignInClient signInClient;

	private final AchievementHandler eventAchievementHandler;

	private AchievementsClient achievementsClient = null;
	private EventsClient eventsClient = null;
//	private LeaderboardsClient leaderboardsClient = null;

	private final Map<GameEvent, Integer> toIncrementMap = new HashMap<>();

	AndroidAchievementHandler(
			Map<? extends GameEvent, String> eventIdMap, Map<? extends EventAchievement, String> eventAchievementIdMap,
			Map<? extends ManualAchievement, String> manualAchievementIdMap,
			Context context, AndroidApplication activity, GoogleSignInClient signInClient){
		this.eventIdMap = eventIdMap;
		this.eventAchievementIdMap = eventAchievementIdMap;
		this.manualAchievementIdMap = manualAchievementIdMap;
		this.context = context;
		this.activity = activity;
		this.signInClient = signInClient;

		eventAchievementHandler = new DefaultEventAchievementHandler(
				eventAchievementIdMap.keySet(),
				() -> Gdx.app.getPreferences("track_shooter.event_tracker.xml"),
				new OnAchievement()
		);
		activity.addAndroidEventListener(this::onActivityResult);
	}
	// region Private Getters
	private GoogleSignInAccount getLastAccount(){
		return GoogleSignIn.getLastSignedInAccount(context);
	}

	/**
	 * If the account isn't null and we don't have a cached {@link EventsClient}, this will increment and clear the cache from {@link #toIncrementMap}
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
			synchronized (toIncrementMap) {
				if (!toIncrementMap.isEmpty()) {
					for (Map.Entry<GameEvent, Integer> entry : toIncrementMap.entrySet()) {
						client.increment(requireNonNull(eventIdMap.get(entry.getKey())), entry.getValue());
						System.out.println("Successfully incremented " + entry.getKey() + " by " + entry.getValue());
					}
					toIncrementMap.clear();
				}
			}
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
		eventAchievementHandler.increment(event, amount);
		final EventsClient eventsClient = getEventsClient(getLastAccount());
		if(eventsClient == null){
			synchronized (toIncrementMap) {
				Integer current = toIncrementMap.get(event);
				if (current == null) {
					current = 0;
				}
				current += amount;
				toIncrementMap.put(event, current);
			}
			System.out.println("[cache] Incrementing " + event + " by " + amount);
		} else {
			eventsClient.increment(value, amount);
			System.out.println("Incrementing " + event + " by " + amount);
		}
	}

	@Override
	public void achieve(ManualAchievement achievement) {
		throw new UnsupportedOperationException();
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
		AchievementsClient client = getAchievementsClient(getLastAccount());
		if(client != null) {
			Task<Intent> task = requireNonNull(client.getAchievementsIntent());
			task.addOnSuccessListener(result -> {
				activity.startActivityForResult(result, REQUEST_IGNORE);
			});
		}
	}

	@Override
	public boolean canShowAchievements() {
		return true;
	}

	interface StartSignIn {
		void startSignIn();
	}
	private class OnAchievement implements DefaultEventAchievementHandler.OnEventBasedAchievement {

		@Override
		public void onEventBasedAchievement(EventAchievement achievement) {
			requireNonNull(achievement);
			String key = requireNonNull(eventAchievementIdMap.get(achievement));
			System.out.println("Should have unlocked: " + achievement);
			if(!achievement.isProgressShown()) {
				AchievementsClient client = getAchievementsClient(getLastAccount());
				if (client != null) {
					System.out.println("This achievement's process was not shown. Incrementing now");
					client.increment(key, 1);
				} else {
					System.out.println("This achievement's process was not shown. NOT Incrementing now");
				}
			} else {
				System.out.println("This achievement's process was shown. Doing nothing");
			}
		}

		@Override
		public void onEventBasedUnlock(EventAchievement achievement) {
			requireNonNull(achievement);
			String key = requireNonNull(eventAchievementIdMap.get(achievement));
			AchievementsClient client = getAchievementsClient(getLastAccount());
			if(client != null){
				System.out.println("Unlocking " + achievement);
				client.unlock(key);
			} else {
				System.out.println("NOT Unlocking " + achievement);
			}

		}

		@Override
		public void onEventIncrement(EventAchievement achievement, int amount) {
			requireNonNull(achievement);
			String key = requireNonNull(eventAchievementIdMap.get(achievement));
			AchievementsClient client = getAchievementsClient(getLastAccount());
			if(client != null){
				System.out.println("[EventAchievement] Incrementing " + achievement + " by " + amount);
				client.increment(key, amount);
			} else {
				System.out.println("NOT Incrementing " + achievement);

			}
		}
	}
}
