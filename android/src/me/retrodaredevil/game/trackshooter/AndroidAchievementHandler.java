package me.retrodaredevil.game.trackshooter;

import android.content.Context;
import android.content.Intent;
import android.view.Gravity;
import android.view.View;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.games.*;
import com.google.android.gms.games.achievement.AchievementBuffer;
import com.google.android.gms.games.event.Event;
import com.google.android.gms.games.event.EventBuffer;
import com.google.android.gms.tasks.Task;
import me.retrodaredevil.game.trackshooter.account.Show;
import me.retrodaredevil.game.trackshooter.account.achievement.AchievementHandler;
import me.retrodaredevil.game.trackshooter.account.achievement.EventAchievement;
import me.retrodaredevil.game.trackshooter.account.achievement.GameEvent;
import me.retrodaredevil.game.trackshooter.account.achievement.ManualAchievement;
import me.retrodaredevil.game.trackshooter.util.Util;

import java.util.*;

import static java.util.Objects.requireNonNull;

class AndroidAchievementHandler implements AchievementHandler {
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
	private final GoogleAccountManager accountManager;

	private final Map<GameEvent, Set<EventAchievement>> eventAchievementSetMap;

	private final Show showAchievements = new GoogleSignInShow() {
		@Override void show(GoogleSignInAccount account) {
			AchievementsClient client = requireNonNull(getAchievementsClient(account));

			Task<Intent> task = requireNonNull(client.getAchievementsIntent());
			task.addOnSuccessListener(result -> activity.startActivityForResult(result, REQUEST_IGNORE));
		}
	};
	private final Show showLeaderboards = new GoogleSignInShow() {
		@Override void show(GoogleSignInAccount account) {
			LeaderboardsClient client = requireNonNull(getLeaderboardsClient(account));

			Task<Intent> task = requireNonNull(client.getAllLeaderboardsIntent());
			task.addOnSuccessListener(result -> activity.startActivityForResult(result, REQUEST_IGNORE));
		}
	};

	private View view;

	private AchievementsClient achievementsClient = null;
	private EventsClient eventsClient = null;
	private LeaderboardsClient leaderboardsClient = null;


	AndroidAchievementHandler(
			Map<? extends GameEvent, String> eventIdMap, Map<? extends EventAchievement, String> eventAchievementIdMap,
			Map<? extends ManualAchievement, String> manualAchievementIdMap,
			String highScoreKey,
			AndroidApplication activity, GoogleAccountManager accountManager){
		this.eventIdMap = eventIdMap;
		this.idEventMap = Util.reverseMap(eventIdMap, new HashMap<>());
		this.eventAchievementIdMap = eventAchievementIdMap;
		this.idEventAchievementMap = Util.reverseMap(eventAchievementIdMap, new HashMap<>());
		this.manualAchievementIdMap = manualAchievementIdMap;
//		this.idManualAchievementMap = Util.reverseMap(manualAchievementIdMap, new HashMap<>());
		this.highScoreKey = highScoreKey;
		this.activity = activity;
		this.accountManager = accountManager;

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


		GoogleSignInAccount account = accountManager.getLastAccount();
		if(account != null){
			onAccountSignIn(account);
		}
		accountManager.addSignInListener(this::onAccountSignIn);
	}

	void setView(View view){
		this.view = view;
		GoogleSignInAccount account = accountManager.getLastAccount();
		if(account != null){
			initGamesClient(Games.getGamesClient(context, account));
		}
	}

	// region Private Getters

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

	private void onAccountSignIn(GoogleSignInAccount account){
		requireNonNull(account);
		initGamesClient(Games.getGamesClient(context, account));
		checkReveals(getAchievementsClient(account), getEventsClient(account), true);
	}
	private void initGamesClient(GamesClient gamesClient){
		gamesClient.setGravityForPopups(Gravity.TOP);
		final View view = this.view;
		if(view != null) {
			gamesClient.setViewForPopups(view);
		}
	}

	@Override
	public synchronized void increment(GameEvent event, int amount) {
		final String value = eventIdMap.get(event);
		if(value == null){
			throw new NoSuchElementException("No value for event: " + event);
		}
		if(amount <= 0){
			throw new IllegalArgumentException("Amount must be > 0. amount: " + amount);
		}
		GoogleSignInAccount account = accountManager.getLastAccount();
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
	// region gms Achievement helper methods
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
	// endregion

	@Override
	public void manualAchieve(ManualAchievement achievement) {
		if(achievement.isIncremental()){
			throw new IllegalArgumentException();
		}
		String key = manualAchievementIdMap.get(achievement);
		if(key == null){
			throw new UnsupportedOperationException("Unsupported manual achievement: " + achievement);
		}
		AchievementsClient achievementsClient = getAchievementsClient(accountManager.getLastAccount());
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
		AchievementsClient achievementsClient = getAchievementsClient(accountManager.getLastAccount());
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
		AchievementsClient achievementsClient = getAchievementsClient(accountManager.getLastAccount());
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

	@Override
	public Show getShowAchievements() {
		return showAchievements;
	}

	@Override
	public Show getShowLeaderboards() {
		return showLeaderboards;
	}

	@Override
	public void submitScore(int score) {
		LeaderboardsClient client = getLeaderboardsClient(accountManager.getLastAccount());
		if(client != null) {
			client.submitScore(highScoreKey, score);
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
	private abstract class GoogleSignInShow implements Show {
		abstract void show(GoogleSignInAccount account);
		@Override
		public void show() {
			GoogleSignInAccount account = accountManager.getLastAccount();
			if(account == null){
				throw new IllegalStateException("account is null! We cannot show now! You should check isCurrentlyAbleToShow()!");
			}
			show(account);
		}

		@Override
		public boolean isCurrentlyAbleToShow() {
			return accountManager.isSignedIn();
		}

		@Override
		public boolean isEverAbleToShow() {
			return true;
		}
	}
}
