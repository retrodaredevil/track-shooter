package me.retrodaredevil.game.trackshooter.achievement.implementations;

import me.retrodaredevil.game.trackshooter.achievement.ManualAchievement;

public enum DefaultAchievement implements ManualAchievement {
	;

	@Override
	public String getLocalSaveKey() {
		return toString();
	}

	@Override
	public Integer getIncrementsForReveal() {
		return null; // TODO change this when implementing more enum values
	}

	@Override
	public int getIncrementsForAchieve() {
		return 0;
	}
}
