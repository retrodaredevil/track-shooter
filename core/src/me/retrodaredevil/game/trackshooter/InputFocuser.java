package me.retrodaredevil.game.trackshooter;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * NOTE: This should not be cached/stored because it does not store {@link InputFocusable}s. It is meant to be
 * temporary when rendering
 * <p>
 * This class allows one {@link InputFocusable} to be focused on at a time while other {@link InputProcessor}s
 * can also be focused on too
 */
public class InputFocuser implements InputFocusable{
	private final int priority;
	private final Set<InputProcessor> parallelProcessors = new LinkedHashSet<>();
	private final Set<InputFocusable> parallelFocusables = new LinkedHashSet<>();
	private InputFocusable primaryFocus = null; // we don't need to keep a collection of this because we can just replace it

	public InputFocuser(){
		this(Integer.MIN_VALUE);
	}
	public InputFocuser(int priority){
		this.priority = priority;
	}

	public InputFocuser add(InputFocusable inputFocusable){
		if(inputFocusable == this){
			throw new IllegalArgumentException("Cannot focus on myself!");
		}
		if((primaryFocus == null || primaryFocus.getFocusPriority() < inputFocusable.getFocusPriority()) && inputFocusable.isWantsToFocus()){
			primaryFocus = inputFocusable;
		}
		return this;
	}
	public InputFocuser addParallel(InputProcessor processor){
		parallelProcessors.add(processor);
		return this;
	}
	public InputFocuser addParallel(InputFocusable inputFocusable){
		parallelFocusables.add(inputFocusable);
		return this;
	}


	/**
	 * Determines what should be focused on
	 * @param mainStage The main stage
	 */
	public void giveFocus(Stage mainStage, InputMultiplexer inputMultiplexer){
		Collection<? extends InputProcessor> processorsToFocus = getInputProcessorsToFocus(mainStage);

		Array<InputProcessor> processorArray = new Array<>(processorsToFocus.size());
		for(InputProcessor processor : processorsToFocus){
			processorArray.add(processor);
		}
		inputMultiplexer.setProcessors(processorArray);

		Gdx.input.setInputProcessor(inputMultiplexer);
	}



	@Override
	public boolean isWantsToFocus() {
		return !parallelProcessors.isEmpty() || !parallelFocusables.isEmpty() || primaryFocus != null;
	}

	@Override
	public int getFocusPriority() {
		return priority;
	}


	@Override
	public Collection<? extends InputProcessor> getInputProcessorsToFocus(Stage mainStage) {
//		giveFocus(mainStage, false);
//		return Collections.singleton(inputMultiplexer);

		final Collection<InputProcessor> processorsToFocus = new LinkedHashSet<>();

		// mainStage or primaryFocus
		if(primaryFocus == null || !primaryFocus.isWantsToFocus()){
			if(mainStage != null) {
				processorsToFocus.add(mainStage);
			}
		} else {
			processorsToFocus.addAll(primaryFocus.getInputProcessorsToFocus(mainStage));
		}

		// parallel processors
		processorsToFocus.addAll(parallelProcessors);
		// parallel focusables
		for(InputFocusable focusable : parallelFocusables){
			if(focusable.isWantsToFocus()) {
				processorsToFocus.addAll(focusable.getInputProcessorsToFocus(mainStage));
			}
		}
		return processorsToFocus;
	}
}
