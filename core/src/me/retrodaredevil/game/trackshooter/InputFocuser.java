package me.retrodaredevil.game.trackshooter;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * NOTE: This should not be cached/stored because it does not store {@link InputFocusable}s. It is meant to be
 * temporary when rendering
 * <p>
 * This class allows one {@link InputFocusable} to be focused on at a time while other {@link InputProcessor}s
 * can also be focused on too
 */
public class InputFocuser {
//	private final SortedSet<InputFocusable> renderables = new TreeSet<>();
	private final Set<InputProcessor> parallelProcessors = new LinkedHashSet<>();
	private final InputMultiplexer inputMultiplexer;
	private InputFocusable primaryFocus = null; // we don't need to keep a collection of this because we can just replace it

	public InputFocuser(InputMultiplexer inputMultiplexer){
		this.inputMultiplexer = inputMultiplexer;
	}

	public void addInputFocus(InputFocusable inputFocusable){
//		renderables.add(inputFocusable)
		if((primaryFocus == null || primaryFocus.getFocusPriority() < inputFocusable.getFocusPriority()) && inputFocusable.isWantsToFocus()){
			primaryFocus = inputFocusable;
		}
	}
	public void addParallelInputProcessor(InputProcessor processor){
		parallelProcessors.add(processor);
	}

	/**
	 * Determines what should be focused on
	 * @param mainStage The main stage
	 */
	public void giveFocus(Stage mainStage){
		final Collection<InputProcessor> processorsToFocus = new ArrayList<>();
		if(primaryFocus == null || !primaryFocus.isWantsToFocus()){
			if(mainStage != null) {
				processorsToFocus.add(mainStage);
			}
		} else {
			processorsToFocus.addAll(primaryFocus.getInputProcessorsToFocus(mainStage));
			primaryFocus.onFocusGiven(mainStage);
		}
		processorsToFocus.addAll(parallelProcessors);

		Array<InputProcessor> processorArray = new Array<>(processorsToFocus.size());
		for(InputProcessor processor : processorsToFocus){
			processorArray.add(processor);
		}
		inputMultiplexer.setProcessors(processorArray);

		Gdx.input.setInputProcessor(inputMultiplexer);
	}

}
