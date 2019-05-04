package me.retrodaredevil.game.trackshooter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;
import com.badlogic.gdx.LifecycleListener;
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
import com.google.android.gms.games.event.Event;
import com.google.android.gms.games.event.EventBuffer;
import com.google.android.gms.tasks.Task;
import me.retrodaredevil.game.trackshooter.achievement.*;
import me.retrodaredevil.game.trackshooter.util.Util;

import java.util.*;

import static java.util.Objects.requireNonNull;

class AndroidAchievementHandler implements AchievementHandler {
	private static final int REQUEST_SIGN_IN = 9001;
	private static final int REQUEST_IGNORE = 5001;

	private final Map<? extends GameEvent, String> eventIdMap;
	private final Map<String, ? extends GameEvent> idEventMap;

	private final Map<? extends EventAchievement, String> eventAchievementIdMap;
	private final Map<String, ? extends EventAchievement> idEventAchievementMap;
	private final Map<? extends ManualAchievement, String> manualAchievementIdMap;
//	private final Map<String, ? extends ManualAchievement> idManualAchievementMap;

	private final String highScoreKey;
	private final Context context;
	private final AndroidApplication activity;
	private final GoogleSignInClient signInClient;

	private final Map<GameEvent, Set<EventAchievement>> eventAchievementSetMap;


	private AchievementsClient achievementsClient = null;
	private EventsClient eventsClient = null;
	private LeaderboardsClient leaderboardsClient = null;

	private boolean wantsSignIn = false;

	AndroidAchievementHandler(
			Map<? extends GameEvent, String> eventIdMap, Map<? extends EventAchievement, String> eventAchievementIdMap,
			Map<? extends ManualAchievement, String> manualAchievementIdMap,
			String highScoreKey,
			AndroidApplication activity, GoogleSignInClient signInClient){
		this.eventIdMap = eventIdMap;
		this.idEventMap = Util.reverseMap(eventIdMap, new HashMap<>());
		this.eventAchievementIdMap = eventAchievementIdMap;
		this.idEventAchievementMap = Util.reverseMap(eventAchievementIdMap, new HashMap<>());
		this.manualAchievementIdMap = manualAchievementIdMap;
//		this.idManualAchievementMap = Util.reverseMap(manualAchievementIdMap, new HashMap<>());
		this.highScoreKey = highScoreKey;
		this.activity = activity;
		this.signInClient = signInClient;

		this.context = activity.getContext();

		Map<GameEvent, Set<EventAchievement>> eventAchievementSetMap = new HashMap<>();
		for(EventAchievement eventAchievement : eventAchievementIdMap.keySet()){
			GameEvent event = eventAchievement.getGameEvent();
			Set<EventAchievement> set = eventAchievementSetMap.get(event);
			if(set == null){
				set = new HashSet<>();
				eventAchievementSetMap.put(event, set);
			}
			if(!set.add(eventAchievement)){
				throw new AssertionError("Already had " + eventAchievement + " in set!");
			}
		}
		this.eventAchievementSetMap = Collections.unmodifiableMap(eventAchievementSetMap);

		activity.addAndroidEventListener(this::onActivityResult);
		activity.addLifecycleListener(new BasicListener());

		GoogleSignInAccount account = getLastAccount();
		if(account != null){
			doAccountSignIn(account);
		}
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
		wantsSignIn = true;
		activity.startActivityForResult(signInClient.getSignInIntent(), REQUEST_SIGN_IN);
	}
	private void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(requestCode == REQUEST_SIGN_IN){
			Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
			try {
				GoogleSignInAccount account = task.getResult(ApiException.class);
				doAccountSignIn(account);
				Toast.makeText(context, "Signed into Google", Toast.LENGTH_SHORT).show();
			} catch (ApiException apiException) {
				onAccountLogout();
				switch(apiException.getStatusCode()){
					case 12501:
						System.out.println("Pressed back button while signing in.");
						break;
					case 4:
						System.out.println("Dreaded status code 4...");
						new AlertDialog.Builder(activity)
								.setMessage("Status code 4. Unable to connect. This error is probably temporary and is likely being fixed.")
								.setNeutralButton(android.R.string.ok, null)
								.show();
						break;
					default:
						String message = apiException.getMessage();
						if (message == null || message.trim().isEmpty()) {
							message = "Unable to connect";
						}


						new AlertDialog.Builder(activity)
								.setMessage(message)
								.setNeutralButton(android.R.string.ok, null)
								.show();
						break;
				}
			}
		}
	}

	@Override
	public void logout() {
		wantsSignIn = false;
		if(isSignedIn()){
			signInClient.signOut()
					.addOnSuccessListener(result -> onAccountLogout())
					.addOnFailureListener(result -> {throw new IllegalStateException("Signing out should never fail!"); });
		}
	}

	private void onResume(){
		System.out.println("Going to silently sign in again");

		if(isSignedIn()){
			System.out.println("We're already signed in!");
		} else if(wantsSignIn){
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

	private void doAccountSignIn(GoogleSignInAccount account){
		requireNonNull(account);
		System.out.println("Successfully signed in");
		wantsSignIn = true;
		checkReveals(getAchievementsClient(account), getEventsClient(account), true);
	}
	private void onAccountLogout(){
		System.out.println("Logging out");
		synchronized (this) {
			achievementsClient = null;
			eventsClient = null;
			leaderboardsClient = null;
		}
		wantsSignIn = false;
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

			boolean needsCheck = false;
			for(Map.Entry<? extends EventAchievement, String> entry : eventAchievementIdMap.entrySet()){
				if(entry.getKey().getGameEvent() == event){
					if(entry.getKey().isIncremental()) {
						achievementsClient.increment(entry.getValue(), amount);
					} else {
						achievementsClient.unlock(entry.getValue());
					}
					needsCheck = true;
				}
			}
			if(needsCheck) {
				checkReveals(achievementsClient, null, false);
			}
		}
	}

	/**
	 *
	 * @param achievementsClient The AchievementsClient. Must be non-null
	 * @param eventsClient The EventsClient if you want to check EventAchievements to make sure they're up to date. If null, will not check
	 */
	private void checkReveals(AchievementsClient achievementsClient, EventsClient eventsClient, boolean reload){
		/*
		Go through each possible achievement. If that achievement is an EventAchievement and its GameEvent is event,
		check to see if we need to reveal it
		 */
		requireNonNull(achievementsClient).load(reload).addOnSuccessListener(result -> {
			AchievementBuffer achievementBuffer = requireNonNull(result.get());
			Map<EventAchievement, AchievementCache> achievementMap = new HashMap<>();
			for (com.google.android.gms.games.achievement.Achievement a : achievementBuffer) {
				String key = a.getAchievementId();
				EventAchievement eventAchievement = idEventAchievementMap.get(key);
				if (eventAchievement != null) {
					achievementMap.put(eventAchievement, new AchievementCache(a));
					final int steps = getCurrentSteps(a);
					Integer stepsToReveal = eventAchievement.getIncrementsForReveal();
					if (stepsToReveal != null && (steps >= stepsToReveal)) {
						achievementsClient.reveal(key);
					}
				}
			}
			achievementBuffer.release();
			if(eventsClient != null) {
				eventsClient.load(reload).addOnSuccessListener(eventsResult -> {
					EventBuffer eventBuffer = requireNonNull(eventsResult.get());

					for (Event gmsEvent : eventBuffer) {
						String eventId = gmsEvent.getEventId();
						GameEvent event = idEventMap.get(eventId);
						int counter = (int) Math.min(gmsEvent.getValue(), Integer.MAX_VALUE);
						if (event != null) {
							Set<EventAchievement> set = eventAchievementSetMap.get(event);
							if(set != null) {
								for (EventAchievement eventAchievement : set) {
									AchievementCache achievementCache = requireNonNull(achievementMap.get(eventAchievement));
									int neededSteps = counter - achievementCache.steps;
									if (neededSteps > 0 && !achievementCache.unlocked) { // we need more steps and the achievement isn't unlocked
										System.out.println("Was a new achievement added? incrementing " + eventAchievement + " by " + neededSteps + " to get to counter: " + counter);
										if (achievementCache.incremental != eventAchievement.isIncremental()) {
											throw new AssertionError("Achievement: " + eventAchievement
													+ " is incremental: " + eventAchievement.isIncremental() + " should be opposite!");
										}
										if (achievementCache.incremental) {
											achievementsClient.increment(achievementCache.id, neededSteps);
											Integer stepsToReveal = eventAchievement.getIncrementsForReveal();
											if (stepsToReveal != null && (counter >= stepsToReveal)) {
												achievementsClient.reveal(achievementCache.id);
											}
										} else {
											achievementsClient.unlock(achievementCache.id);
										}
									}

								}
							}
						}
					}

					eventBuffer.release();
				});
			}
		});

	}
	private static boolean isIncremental(com.google.android.gms.games.achievement.Achievement gamesAchievement){
		return gamesAchievement.getType() == com.google.android.gms.games.achievement.Achievement.TYPE_INCREMENTAL;
	}
	private static boolean isUnlocked(com.google.android.gms.games.achievement.Achievement gamesAchievement){
		return gamesAchievement.getState() == com.google.android.gms.games.achievement.Achievement.STATE_UNLOCKED;
	}
	private static int getCurrentSteps(com.google.android.gms.games.achievement.Achievement gamesAchievement){
		if(isIncremental(gamesAchievement)){
			return gamesAchievement.getCurrentSteps();
		}
		return gamesAchievement.getState() == com.google.android.gms.games.achievement.Achievement.STATE_UNLOCKED ? 1 : 0;
	}
	private static int getTotalSteps(com.google.android.gms.games.achievement.Achievement gamesAchievement){
		if(isIncremental(gamesAchievement)){
			return gamesAchievement.getTotalSteps();
		}
		return 1;
	}

	@Override
	public void manualAchieve(ManualAchievement achievement) {
		if(achievement.isIncremental()){
			throw new IllegalArgumentException();
		}
		String key = manualAchievementIdMap.get(achievement);
		if(key == null){
			throw new UnsupportedOperationException("Unsupported manual achievement: " + achievement);
		}
		AchievementsClient achievementsClient = getAchievementsClient(getLastAccount());
		if(achievementsClient != null){
			achievementsClient.unlock(key);
		}
	}

	@Override
	public void manualIncrement(ManualAchievement achievement, int amount) {
		if(!achievement.isIncremental()){
			manualAchieve(achievement);
			return;
		}
		String key = manualAchievementIdMap.get(achievement);
		if(key == null){
			throw new UnsupportedOperationException("Unsupported manual achievement: " + achievement);
		}
		AchievementsClient achievementsClient = getAchievementsClient(getLastAccount());
		if(achievementsClient != null){
			achievementsClient.increment(key, amount);
		}
	}

	@Override
	public void manualReveal(ManualAchievement achievement) {
		String key = manualAchievementIdMap.get(achievement);
		if(key == null){
			throw new UnsupportedOperationException("Unsupported manual achievement: " + achievement);
		}
		AchievementsClient achievementsClient = getAchievementsClient(getLastAccount());
		if(achievementsClient != null){
			achievementsClient.reveal(key);
		}
	}

	@Override
	public boolean isSupported(GameEvent event) {
		return eventIdMap.containsKey(event);
	}

	@Override
	public boolean isSupported(ManualAchievement achievement) {
		return manualAchievementIdMap.containsKey(achievement);
	}

	// region Show Region
	@Override
	public void showAchievements() {
		GoogleSignInAccount account = getLastAccount();
		if(account == null){
			throw new IllegalStateException("account is null! We cannot show achievements now! You should check isCurrentlyAbleToShowAchievements()!");
		}
		AchievementsClient client = requireNonNull(getAchievementsClient(account));

		Task<Intent> task = requireNonNull(client.getAchievementsIntent());
		task.addOnSuccessListener(result -> activity.startActivityForResult(result, REQUEST_IGNORE));
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

		Task<Intent> task = requireNonNull(client.getAllLeaderboardsIntent());

		task.addOnSuccessListener(result -> activity.startActivityForResult(result, REQUEST_IGNORE));
	}
	@Override public boolean isEverAbleToShowLeaderboards() { return true; }
	@Override public boolean isCurrentlyAbleToShowLeaderboards() { return isSignedIn(); }
	// endregion

	@Override
	public void submitScore(int score) {
		LeaderboardsClient client = getLeaderboardsClient(getLastAccount());
		if(client != null) {
			client.submitScore(highScoreKey, score);
		}
	}

	private class BasicListener implements LifecycleListener {

		@Override
		public void pause() {
		}
		@Override
		public void resume() {
			onResume();
		}

		@Override
		public void dispose() {
		}
	}
	private static class AchievementCache {

		final String id;
		final int steps;
		final int totalSteps;
		final boolean unlocked;
		final boolean incremental;

		AchievementCache(com.google.android.gms.games.achievement.Achievement achievement){
			id = achievement.getAchievementId();
			steps = getCurrentSteps(achievement);
			totalSteps = getTotalSteps(achievement);
			unlocked = isUnlocked(achievement);
			incremental = isIncremental(achievement);
		}
	}
}
