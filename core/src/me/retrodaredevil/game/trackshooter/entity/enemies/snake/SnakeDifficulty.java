package me.retrodaredevil.game.trackshooter.entity.enemies.snake;

public enum SnakeDifficulty {
	EASY(1), NORMAL(2), HARD(3), EXTREME(4);

	public final int value;
	SnakeDifficulty(int value){
		this.value = value;
	}
	public int getValue(){
		return value;
	}
}
