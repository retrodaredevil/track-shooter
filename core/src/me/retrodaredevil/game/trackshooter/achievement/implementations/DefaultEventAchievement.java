package me.retrodaredevil.game.trackshooter.achievement.implementations;

import me.retrodaredevil.game.trackshooter.achievement.EventAchievement;
import me.retrodaredevil.game.trackshooter.achievement.GameEvent;

public enum DefaultEventAchievement implements EventAchievement {
	FIRST_GAME(DefaultGameEvent.GAMES_COMPLETED, false),
	COMPLETE_20_GAMES(DefaultGameEvent.GAMES_COMPLETED, true, 1),
	COMPLETE_100_GAMES(DefaultGameEvent.GAMES_COMPLETED, true, 20),
	SHARKS_KILLED_5(DefaultGameEvent.SHARKS_KILLED, true),
	SHARKS_KILLED_20(DefaultGameEvent.SHARKS_KILLED, true, 5),
	SHARKS_KILLED_100(DefaultGameEvent.SHARKS_KILLED, true, 20)
	;
	private final GameEvent gameEvent;
	private final boolean incremental;
	private final Integer reveal;

	DefaultEventAchievement(GameEvent gameEvent, boolean incremental, Integer reveal) {
		this.gameEvent = gameEvent;
		this.incremental = incremental;
		this.reveal = reveal;
	}
	DefaultEventAchievement(GameEvent gameEvent, boolean incremental){
		this(gameEvent, incremental, null);
	}

	@Override
	public String getLocalSaveKey() {
		return toString();
	}

	@Override
	public GameEvent getGameEvent() {
		return gameEvent;
	}

	@Override
	public Integer getIncrementsForReveal() {
		return reveal;
	}

	@Override
	public boolean isIncremental() {
		return incremental;
	}
}
