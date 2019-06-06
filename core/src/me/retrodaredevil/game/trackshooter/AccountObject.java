package me.retrodaredevil.game.trackshooter;

import me.retrodaredevil.game.trackshooter.account.AccountManager;
import me.retrodaredevil.game.trackshooter.account.achievement.AchievementHandler;
import me.retrodaredevil.game.trackshooter.account.multiplayer.Multiplayer;

/**
 * A simple POJO that holds objects related to account things
 */
public final class AccountObject {
	private final AccountManager accountManager;
	private final AchievementHandler achievementHandler;
	private final Multiplayer multiplayer;

	public AccountObject(AccountManager accountManager, AchievementHandler achievementHandler, Multiplayer multiplayer) {
		this.accountManager = accountManager;
		this.achievementHandler = achievementHandler;
		this.multiplayer = multiplayer;
	}

	public AccountManager getAccountManager() {
		return accountManager;
	}

	public AchievementHandler getAchievementHandler() {
		return achievementHandler;
	}

	public Multiplayer getMultiplayer() {
		return multiplayer;
	}
}
