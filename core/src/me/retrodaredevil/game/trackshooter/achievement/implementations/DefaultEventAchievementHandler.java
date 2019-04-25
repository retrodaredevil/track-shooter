package me.retrodaredevil.game.trackshooter.achievement.implementations;

import com.badlogic.gdx.Preferences;
import me.retrodaredevil.game.trackshooter.achievement.*;

import java.util.Collection;

/**
 * An {@link AchievementHandler} that supports saving the number of events
 */
public class DefaultEventAchievementHandler implements AchievementHandler{

	private final Collection<? extends EventAchievement> achievements;
	private final PreferenceGetter preferencesGetter;
	private final OnEventBasedAchievement onEventBasedAchievement;

	public DefaultEventAchievementHandler(Collection<? extends EventAchievement> achievements, PreferenceGetter preferencesGetter, OnEventBasedAchievement onEventBasedAchievement) {
		this.achievements = achievements;
		this.preferencesGetter = preferencesGetter;
		this.onEventBasedAchievement = onEventBasedAchievement;
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
				if(a.isProgressShown()){
					onEventBasedAchievement.onEventIncrement(a, amount);
				}
				Integer reveal = a.getIncrementsForReveal();
				if (reveal != null && newAmount >= reveal) {
					onEventBasedAchievement.onEventBasedUnlock(a);
				}
				if (newAmount >= a.getIncrementsForAchieve()) {
					onEventBasedAchievement.onEventBasedAchievement(a);
				}
			}
		}
	}

	@Override
	public void achieve(ManualAchievement achievement) {
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

	@Override
	public boolean canShowAchievements() {
		return false;
	}

	public interface OnEventBasedAchievement {
		void onEventBasedAchievement(EventAchievement achievement);
		void onEventBasedUnlock(EventAchievement achievement);

		/**
		 * NOTE: This is only fired if {@link EventAchievement#isProgressShown()}
		 * @param achievement The achievement
		 */
		void onEventIncrement(EventAchievement achievement, int amount);
	}
	public interface PreferenceGetter {
		Preferences get();
	}
}
