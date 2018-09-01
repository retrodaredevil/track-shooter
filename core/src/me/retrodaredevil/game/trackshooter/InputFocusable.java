package me.retrodaredevil.game.trackshooter;

import com.badlogic.gdx.scenes.scene2d.Stage;

public interface InputFocusable {
	boolean isWantsToHandleInput();

	/**
	 *
	 * @return The input priority. A higher value represents a higher priority
	 */
	int getInputPriority();

//	@Override
//	default int compareTo(InputFocusable inputFocusRenderable){
//		boolean selfWants = isWantsToHandleInput();
//		boolean compareWants = inputFocusRenderable.isWantsToHandleInput();
//		if(selfWants && !compareWants){
//			return 1;
//		} else if(!selfWants && compareWants){
//			return -1;
//		}
//		return this.getInputPriority() - inputFocusRenderable.getInputPriority();
//	}

	void giveInputFocus(Stage mainStage);
}
