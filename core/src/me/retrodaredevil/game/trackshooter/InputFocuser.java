package me.retrodaredevil.game.trackshooter;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;

public class InputFocuser {
//	private final SortedSet<InputFocusable> renderables = new TreeSet<>();
	private InputFocusable primaryFocus = null;

	public void addInputFocus(InputFocusable inputFocusable){
//		renderables.add(inputFocusable)
		if(primaryFocus == null|| primaryFocus.getInputPriority() < inputFocusable.getInputPriority()){
			primaryFocus = inputFocusable;
		}

	}

	public void giveFocus(Stage mainStage){
		if(primaryFocus == null || !primaryFocus.isWantsToHandleInput()){
			Gdx.input.setInputProcessor(mainStage);
		} else {
			primaryFocus.giveInputFocus(mainStage);
		}
	}

}
