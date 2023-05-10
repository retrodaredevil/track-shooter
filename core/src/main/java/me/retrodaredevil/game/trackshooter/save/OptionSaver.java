package me.retrodaredevil.game.trackshooter.save;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

import java.util.Collection;

import me.retrodaredevil.controller.options.ConfigurableControllerPart;
import me.retrodaredevil.controller.options.ConfigurableObject;
import me.retrodaredevil.controller.options.ControlOption;
import me.retrodaredevil.controller.options.OptionValue;

/**
 * TODO There are some bugs when saving and loading when there are multiple inputs in the same game...
 * When these are loaded and saved, some options load from the same key on each input which stops different
 * controllers from having different configurations. I intend to fix this bug in THIS class in the future.
 * This of course, would require some refactoring to tell which player is which, etc.
 */
public final class OptionSaver {

	private final Preferences optionPreferences;

	public OptionSaver(){
		optionPreferences = Gdx.app.getPreferences("options");
	}

	public void loadControllerConfiguration(Integer playerIndex, ConfigurableObject configController){
		final Collection<? extends ControlOption> options = configController.getControlOptions();
		for(ControlOption option : options){
			loadControlOption(playerIndex, option);
		}
	}

	/**
	 * Overrides the value of the control option using the saved value or leaves it untouched if
	 * there was no saved value found
	 * @param playerIndex The player index starting at 0 or null for global
	 * @param controlOption
	 */
	public void loadControlOption(Integer playerIndex, ControlOption controlOption){
		final OptionValue value = controlOption.getOptionValue();
		final float defaultValue = (float) value.getDefaultOptionValue();
		final float savedValue = optionPreferences.getFloat(getKey(playerIndex, controlOption), defaultValue);
		if(savedValue < value.getMinOptionValue() || savedValue > value.getMaxOptionValue() || defaultValue == savedValue){
			value.setToDefaultOptionValue();
		} else {
			value.setOptionValue(savedValue);
		}
	}

	/**
	 * Saves the value of the control option to a file
	 * @param playerIndex The player index starting at 0 or null for global
	 * @param controlOption The control option
	 */
	public void saveControlOption(Integer playerIndex, ControlOption controlOption, boolean flush){
		final OptionValue optionValue = controlOption.getOptionValue();
		final double value = optionValue.getOptionValue();
		final String key = getKey(playerIndex, controlOption);
		if(value == optionValue.getDefaultOptionValue()){
			if(optionPreferences.contains(key)) {
				optionPreferences.remove(key);
			}
		} else {
			optionPreferences.putFloat(key, (float) value);
		}
		if(flush){
			optionPreferences.flush();
		}
	}
	/**
	 * @see #saveControlOption(Integer, ControlOption, boolean)
	 */
	public void saveControlOption(Integer playerIndex, ControlOption controlOption){
		this.saveControlOption(playerIndex, controlOption, true);
	}
	private String getKey(Integer playerIndex, ControlOption controlOption){
		if(playerIndex == null){
			return "global." + controlOption.getCategory();
		}
		return "player." + playerIndex + "." + controlOption.getCategory();
	}
}
