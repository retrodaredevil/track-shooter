package me.retrodaredevil.game.trackshooter;

import me.retrodaredevil.game.trackshooter.account.AccountManager;
import me.retrodaredevil.game.trackshooter.account.achievement.AchievementHandler;

/**
 * A simple POJO that holds objects related to account things
 */
public final class AccountObject {
	private final AccountManager accountManager;
	private final AchievementHandler achievementHandler;

	public AccountObject(AccountManager accountManager, AchievementHandler achievementHandler) {
		this.accountManager = accountManager;
		this.achievementHandler = achievementHandler;
	}

	public AccountManager getAccountManager() {
		return accountManager;
	}

	public AchievementHandler getAchievementHandler() {
		return achievementHandler;
	}
}
