package me.retrodaredevil.game.trackshooter.achievement;

import me.retrodaredevil.game.trackshooter.account.achievement.GameEvent;

public enum DefaultGameEvent implements GameEvent {
	SHARKS_KILLED,
	SNAKES_KILLED,
	CARGO_SHIPS_PROTECTED,
	CARGO_SHIPS_UNPROTECTED,
	POWER_UPS_COLLECTED,
	FRUIT_CONSUMED,
	GAMES_COMPLETED,
	REDIRECT_STARFISH
	;

	@Override
	public String getLocalSaveKey() {
		return toString();
	}
}
