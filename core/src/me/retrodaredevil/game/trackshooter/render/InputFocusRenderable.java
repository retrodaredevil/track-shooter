package me.retrodaredevil.game.trackshooter.render;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;

public interface InputFocusRenderable extends Renderable {
	boolean isWantsToHandleInput();

	/**
	 *
	 * @return The input priority. A higher value represents a higher priority
	 */
	int getInputPriority();

//	@Override
//	default int compareTo(InputFocusRenderable inputFocusRenderable){
//		boolean selfWants = isWantsToHandleInput();
//		boolean compareWants = inputFocusRenderable.isWantsToHandleInput();
//		if(selfWants && !compareWants){
//			return 1;
//		} else if(!selfWants && compareWants){
//			return -1;
//		}
//		return this.getInputPriority() - inputFocusRenderable.getInputPriority();
//	}

	default void giveInputFocus(Stage mainStage){
		Stage stage = getPreferredStage();
		if(stage == null){
			stage = mainStage;
		}
		Gdx.input.setInputProcessor(stage);
	}
}
