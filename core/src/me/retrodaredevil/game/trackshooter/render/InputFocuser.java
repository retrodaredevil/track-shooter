package me.retrodaredevil.game.trackshooter.render;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;

public class InputFocuser {
//	private final SortedSet<InputFocusRenderable> renderables = new TreeSet<>();
	private InputFocusRenderable primaryFocus = null;

	public void addInputFocus(InputFocusRenderable inputFocusRenderable){
//		renderables.add(inputFocusRenderable)
		if(primaryFocus == null|| primaryFocus.getInputPriority() < inputFocusRenderable.getInputPriority()){
			primaryFocus = inputFocusRenderable;
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
