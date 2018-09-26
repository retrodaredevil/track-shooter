package me.retrodaredevil.game.trackshooter.save;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

import java.util.Collection;

import me.retrodaredevil.controller.options.ConfigurableControllerPart;
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

	public void loadControllerConfiguration(ConfigurableControllerPart configController){
		Collection<? extends ControlOption> options = configController.getControlOptions();
		for(ControlOption option : options){
			loadControlOption(option);
		}
//		System.out.println("Loaded " + options.size() + " options");
	}

	/**
	 * Overrides the value of the control option using the saved value or leaves it untouched if
	 * there was no saved value found
	 * @param controlOption
	 */
	public void loadControlOption(ControlOption controlOption){
		OptionValue value = controlOption.getOptionValue();
		float savedValue = optionPreferences.getFloat(controlOption.getKey(), (float) value.getDefaultOptionValue());
		if(savedValue < value.getMinOptionValue() || savedValue > value.getMaxOptionValue()){
			value.setToDefaultOptionValue();
		} else {
			value.setOptionValue(savedValue);
		}
	}

	/**
	 * Saves the value of the control option to a file
	 * @param controlOption The control option
	 */
	public void saveControlOption(ControlOption controlOption, boolean flush){
		optionPreferences.putFloat(controlOption.getKey(), (float) controlOption.getOptionValue().getOptionValue());
		if(flush){
			optionPreferences.flush();
		}
	}
	/**
	 * @see #saveControlOption(ControlOption, boolean)
	 */
	public void saveControlOption(ControlOption controlOption){
		this.saveControlOption(controlOption, true);
	}
}
