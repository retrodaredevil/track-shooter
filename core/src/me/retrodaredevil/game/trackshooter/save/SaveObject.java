package me.retrodaredevil.game.trackshooter.save;

/**
 * Represents objects to help the game run and save state.
 */
public class SaveObject {
	private final OptionSaver optionSaver;
	public SaveObject(){
		this.optionSaver = new OptionSaver();
	}

	public OptionSaver getOptionSaver(){ return optionSaver; }
}
