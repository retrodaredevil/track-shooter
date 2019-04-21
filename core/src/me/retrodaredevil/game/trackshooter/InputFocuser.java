package me.retrodaredevil.game.trackshooter;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;

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

	/** @param inputFocusable An InputProcessor to add and determine if it should get priority, or null to do nothing
	 * @return this for chaining */
	public InputFocuser add(InputFocusable inputFocusable){
		if(inputFocusable == this){
			throw new IllegalArgumentException("Cannot focus on myself!");
		}
		if(inputFocusable == null){
			return this;
		}
		if((primaryFocus == null || primaryFocus.getFocusPriority() < inputFocusable.getFocusPriority()) && inputFocusable.isWantsToFocus()){
			primaryFocus = inputFocusable;
		}
		return this;
	}
	/** @param processor An InputProcessor to add parallel, or null to do nothing
	 * @return this for chaining */
	public InputFocuser addParallel(InputProcessor processor){
		if(processor == null){
			return this;
		}
		parallelProcessors.add(processor);
		return this;
	}

	/** @param inputFocusable An InputFocusable to add parallel, or null to do nothing
	 * @return this for chaining */
	public InputFocuser addParallel(InputFocusable inputFocusable){
		if(inputFocusable == null){
			return this;
		}
		parallelFocusables.add(inputFocusable);
		return this;
	}


	/**
	 * Determines what should be focused on
	 */
	public void giveFocus(InputMultiplexer inputMultiplexer){
		Collection<? extends InputProcessor> processorsToFocus = getInputProcessorsToFocus();

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
	public Collection<? extends InputProcessor> getInputProcessorsToFocus() {
		final Collection<InputProcessor> processorsToFocus = new LinkedHashSet<>();

		if(primaryFocus != null) {
			processorsToFocus.addAll(primaryFocus.getInputProcessorsToFocus());
		}

		// parallel processors
		processorsToFocus.addAll(parallelProcessors);

		// parallel focusables
		for(InputFocusable focusable : parallelFocusables){
			if(focusable.isWantsToFocus()) {
				processorsToFocus.addAll(focusable.getInputProcessorsToFocus());
			}
		}
		return processorsToFocus;
	}
}
