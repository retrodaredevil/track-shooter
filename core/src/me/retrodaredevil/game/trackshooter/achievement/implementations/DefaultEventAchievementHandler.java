package me.retrodaredevil.game.trackshooter.achievement.implementations;

import com.badlogic.gdx.Preferences;
import me.retrodaredevil.game.trackshooter.achievement.*;
import me.retrodaredevil.game.trackshooter.util.PreferencesGetter;

import java.util.Collection;

/**
 * An {@link AchievementHandler} that supports saving the number of events
 *
 * @deprecated Not used in Android implementation anymore and code may not be up to date
 */
@Deprecated
public class DefaultEventAchievementHandler implements AchievementHandler{

	private final Collection<? extends EventAchievement> achievements;
	private final PreferencesGetter preferencesGetter;
	private final OnEventAchievement onEventAchievement;

	public DefaultEventAchievementHandler(Collection<? extends EventAchievement> achievements, PreferencesGetter preferencesGetter, OnEventAchievement onEventAchievement) {
		this.achievements = achievements;
		this.preferencesGetter = preferencesGetter;
		this.onEventAchievement = onEventAchievement;
	}

	// region sign in
	@Override
	public void signIn() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void logout() {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean isNeedsSignIn() {
		return false;
	}

	@Override
	public boolean isSignedIn() {
		return false;
	}
	// endregion

	@Override
	public void increment(GameEvent event, int amount) {
		Preferences prefs = preferencesGetter.get();
		String saveKey = event.getLocalSaveKey();
		int current = prefs.getInteger(saveKey, 0);
		int newAmount = current + amount;
		prefs.putInteger(saveKey, newAmount);
		prefs.flush();
		System.out.println("newAmount for " + event + " is " + newAmount);
		for(EventAchievement a : achievements){
			if(a.getGameEvent() == event) {
				System.out.println(event + " is related to EventAchievement: " + a);
				onEventAchievement.onEventIncrement(a, amount);
				Integer reveal = a.getIncrementsForReveal();
				if (reveal != null && newAmount >= reveal) {
					onEventAchievement.onEventReveal(a);
				}
				if (newAmount >= a.getIncrementsForAchieve()) {
					onEventAchievement.onEventAchievement(a);
				}
			}
		}
	}

	@Override
	public void manualAchieve(ManualAchievement achievement) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void manualIncrement(ManualAchievement achievement) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean isSupported(GameEvent event) {
		return true;
	}

	@Override
	public boolean isSupported(ManualAchievement achievement) {
		return false;
	}

	@Override
	public void showAchievements() {
		throw new UnsupportedOperationException();
	}
	@Override public boolean isEverAbleToShowAchievements() { return false; }
	@Override public boolean isCurrentlyAbleToShowAchievements() { return false; }

	@Override
	public void showLeaderboards() {
		throw new UnsupportedOperationException();
	}
	@Override public boolean isEverAbleToShowLeaderboards() { return false; }
	@Override public boolean isCurrentlyAbleToShowLeaderboards() { return false; }

	@Override
	public void submitScore(int score) {
	}

	public interface OnEventAchievement {
		void onEventAchievement(EventAchievement achievement);
		void onEventReveal(EventAchievement achievement);

		/**
		 * @param achievement The achievement
		 */
		void onEventIncrement(EventAchievement achievement, int amount);
	}
}
