package me.retrodaredevil.game.trackshooter.entity;

public enum EntityDifficulty {
	EASY(1), NORMAL(2), HARD(3), EXTREME(4);

	public final int value;
	EntityDifficulty(int value){
		this.value = value;
	}
//	public int getValue(){
//		return value;
//	}
}
