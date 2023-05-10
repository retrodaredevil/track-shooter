package me.retrodaredevil.game.trackshooter.save;

/**
 * Represents objects to help the game run and save state.
 */
public class SaveObject {
	/*
	As of right now, yes, this only holds one object. The point of creating this class was because I thought there would
	eventually be many more objects related to saving. This is similar to RenderObject and RenderParts
	 */
	private final OptionSaver optionSaver;
	public SaveObject(){
		this.optionSaver = new OptionSaver();
	}

	public OptionSaver getOptionSaver(){ return optionSaver; }
}
