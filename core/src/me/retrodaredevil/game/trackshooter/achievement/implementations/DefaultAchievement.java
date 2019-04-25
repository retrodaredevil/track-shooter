package me.retrodaredevil.game.trackshooter.achievement.implementations;

import me.retrodaredevil.game.trackshooter.achievement.ManualAchievement;

public enum DefaultAchievement implements ManualAchievement {
	;

	@Override
	public String getLocalSaveKey() {
		return toString();
	}
}
