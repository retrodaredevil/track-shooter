package me.retrodaredevil.game.trackshooter.achievement.implementations;

import me.retrodaredevil.game.trackshooter.achievement.EventAchievement;
import me.retrodaredevil.game.trackshooter.achievement.GameEvent;

public enum DefaultEventAchievement implements EventAchievement {
	FIRST_GAME(DefaultGameEvent.GAMES_COMPLETED, 1),
	COMPLETE_20_GAMES(DefaultGameEvent.GAMES_COMPLETED, 20, 1),
	COMPLETE_100_GAMES(DefaultGameEvent.GAMES_COMPLETED, 100, 20),
	SHARKS_KILLED_5(DefaultGameEvent.SHARKS_KILLED, 5)
	;
	private final GameEvent gameEvent;
	private final int achieve;
	private final Integer reveal;

	DefaultEventAchievement(GameEvent gameEvent, int achieve, Integer reveal) {
		this.gameEvent = gameEvent;
		this.achieve = achieve;
		this.reveal = reveal;
	}
	DefaultEventAchievement(GameEvent gameEvent, int achieve){
		this(gameEvent, achieve, null);
	}

	@Override
	public boolean isProgressShown() {
		return true;
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
	public int getIncrementsForAchieve() {
		return achieve;
	}
}
