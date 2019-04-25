package me.retrodaredevil.game.trackshooter.achievement.implementations;

import me.retrodaredevil.game.trackshooter.achievement.GameEvent;

public enum DefaultGameEvent implements GameEvent {
	SHARKS_KILLED,
	SNAKES_KILLED,
	CARGO_SHIPS_PROTECTED,
	POWER_UPS_COLLECTED,
	FRUIT_CONSUMED,
	GAMES_COMPLETED,
	SHOTS_FIRED;

	@Override
	public String getLocalSaveKey() {
		return toString();
	}
}
