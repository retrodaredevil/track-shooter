package me.retrodaredevil.game.trackshooter.input;

public enum InputQuirk {
	NORMAL,
	WEAR,
	;

	public boolean isForceGyro() {
		return this == WEAR;
	}
}
