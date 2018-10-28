package me.retrodaredevil.game.trackshooter.render.selection;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import me.retrodaredevil.controller.input.InputPart;
import me.retrodaredevil.controller.input.JoystickPart;
import me.retrodaredevil.game.trackshooter.util.MathUtil;

public class OptionHolder {
	private final ContentTableProvider contentTableProvider;
	private final Collection<? extends SingleOptionProvider> optionProviders;
	private final List<OptionPair> optionPairs = new ArrayList<>();
	/** The option index. */
	private int selectedOptionIndex;
	private boolean active = false;

	/**
	 * @param defaultIndex The default index
	 * @param contentTableProvider The content table provider. NOTE: This will not be updated automatically
	 * @param optionProviders The collection of option providers
	 */
	public OptionHolder(int defaultIndex, ContentTableProvider contentTableProvider, Collection<? extends SingleOptionProvider> optionProviders){
		this.contentTableProvider = contentTableProvider;
		this.optionProviders = optionProviders;

		setSelectedOptionIndex(defaultIndex);
	}

	public int getSelectedOptionIndex(){
		return selectedOptionIndex;
	}
	public void setSelectedOptionIndex(int selectedOptionIndex){
        if(!optionPairs.isEmpty()) {
			this.selectedOptionIndex = MathUtil.mod(selectedOptionIndex, optionPairs.size());
		} else {
        	this.selectedOptionIndex = selectedOptionIndex;
		}
	}
	public void setActive(boolean active){
		this.active = active;
	}
	public boolean isActive(){
		return active;
	}
	public ContentTableProvider getContentTableProvider(){
		return contentTableProvider;
	}
	public Collection<? extends OptionPair> getOptionPairs(){
		return optionPairs;
	}
	public void deselectCurrent(){
		if(active && selectedOptionIndex >= 0 && selectedOptionIndex < optionPairs.size()) {
			optionPairs.get(selectedOptionIndex).getSingleOption().deselect();
		}
		setActive(false);
	}

	/**
	 * Updates the options. This should be called each frame
	 * @param requestingActions The actions that are requesting to be completed. This is allowed to be mutated
	 */
	public void updateOptions(Collection<? super SingleOption.SelectAction> requestingActions){
		for(SingleOptionProvider provider : optionProviders){
			for(SingleOption option : provider.getOptionsToAdd()){
				optionPairs.add(new OptionPair(option, provider));
			}
		}
		for (final Iterator<OptionPair> it = optionPairs.iterator(); it.hasNext();) {
			OptionPair optionPair = it.next();
			SingleOption singleOption = optionPair.getSingleOption();
			SingleOptionProvider provider = optionPair.getProvider();
			if (!provider.shouldKeep(singleOption)) {
				singleOption.remove();
				it.remove();
			} else {
				singleOption.renderUpdate(contentTableProvider, requestingActions);
			}
		}
	}

	/**
	 * Renders the ContentTableProvider. This should be called when this is "selected"
	 * @param delta The delta time in seconds
	 * @param selectJoystick The joystick used to change selection
	 * @param selectButton The button used to select
	 * @param backButton The button used to go back
	 * @param requestingActions The actions that are requesting to be completed. This is allowed to be mutated
	 */
	public void updateSelection(float delta, JoystickPart selectJoystick,
								InputPart selectButton, InputPart backButton, Collection<? super SingleOption.SelectAction> requestingActions){
		final boolean hasController = selectJoystick != null && selectButton != null && backButton != null;

		{
			int i = 0;
			for (OptionPair optionPair : optionPairs) {
				SingleOption singleOption = optionPair.getSingleOption();
				if (hasController && active && i == selectedOptionIndex) {
					singleOption.selectUpdate(delta, selectJoystick, selectButton, backButton, requestingActions);
				}
				i++;
			}
		}
	}
}
