package me.retrodaredevil.game.trackshooter.achievement;

import me.retrodaredevil.game.trackshooter.account.achievement.ManualAchievement;

public enum DefaultAchievement implements ManualAchievement {
	CLEAR_LEVEL_5,
	CLEAR_LEVEL_10,
	CLEAR_LEVEL_30,
	;

	@Override
	public String getLocalSaveKey() {
		return toString();
	}

	@Override
	public Integer getIncrementsForReveal() {
		return null;
	}

	@Override
	public boolean isIncremental() {
		return false;
	}

}
