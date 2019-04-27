package me.retrodaredevil.game.trackshooter.achievement.implementations;

import me.retrodaredevil.game.trackshooter.achievement.Achievement;
import me.retrodaredevil.game.trackshooter.achievement.ManualAchievement;

import java.util.Collection;
import java.util.Collections;

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
	public boolean isIncremental() {
		return false;
	}

}
