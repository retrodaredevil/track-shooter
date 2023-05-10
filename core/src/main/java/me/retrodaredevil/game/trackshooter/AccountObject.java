package me.retrodaredevil.game.trackshooter;

import me.retrodaredevil.game.trackshooter.account.AccountManager;
import me.retrodaredevil.game.trackshooter.account.achievement.AchievementHandler;
import me.retrodaredevil.game.trackshooter.account.multiplayer.AccountMultiplayer;

/**
 * A simple POJO that holds objects related to account things
 */
public final class AccountObject {
	private final AccountManager accountManager;
	private final AchievementHandler achievementHandler;
	private final AccountMultiplayer accountMultiplayer;

	public AccountObject(AccountManager accountManager, AchievementHandler achievementHandler, AccountMultiplayer accountMultiplayer) {
		this.accountManager = accountManager;
		this.achievementHandler = achievementHandler;
		this.accountMultiplayer = accountMultiplayer;
	}

	public AccountManager getAccountManager() {
		return accountManager;
	}

	public AchievementHandler getAchievementHandler() {
		return achievementHandler;
	}

	public AccountMultiplayer getAccountMultiplayer() {
		return accountMultiplayer;
	}
}
