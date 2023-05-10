package me.retrodaredevil.game.trackshooter;

import me.retrodaredevil.game.trackshooter.account.AccountManager;
import me.retrodaredevil.game.trackshooter.account.achievement.AchievementHandler;
import me.retrodaredevil.game.trackshooter.account.multiplayer.AccountMultiplayer;

public class BasicAndroidLauncher extends BaseAndroidLauncher {
	@Override
	protected AccountObject createAccountObject() {
		return new AccountObject(AccountManager.Defaults.NO_MANAGER, AchievementHandler.Defaults.UNSUPPORTED_HANDLER, AccountMultiplayer.Defaults.NOT_SUPPORTED);
	}

	@Override
	protected void postInitialize() {
	}
}
