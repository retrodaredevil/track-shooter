package me.retrodaredevil.game.trackshooter.render.selection.options.providers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.retrodaredevil.controller.options.ConfigurableObject;
import me.retrodaredevil.controller.options.ControlOption;
import me.retrodaredevil.controller.options.OptionValue;
import me.retrodaredevil.game.trackshooter.render.RenderObject;
import me.retrodaredevil.game.trackshooter.render.selection.SingleOption;
import me.retrodaredevil.game.trackshooter.render.selection.SingleOptionProvider;
import me.retrodaredevil.game.trackshooter.render.selection.options.CheckBoxSingleOption;
import me.retrodaredevil.game.trackshooter.render.selection.options.DropDownSingleOption;
import me.retrodaredevil.game.trackshooter.render.selection.options.SliderSingleOption;
import me.retrodaredevil.game.trackshooter.save.SaveObject;
import me.retrodaredevil.game.trackshooter.util.Size;

public class ConfigurableObjectOptionProvider implements SingleOptionProvider {
	/*
	Some credit and help found at http://brokenshotgun.com/2014/02/08/libgdx-control-scene2d-buttons-with-a-controller/
	 */

	private final Size size;
	private final Integer playerIndex;
	private final ConfigurableObject configurableObject;
	private final RenderObject renderObject;
	private final SaveObject saveObject;
	private final ControlOptionVisibility controlOptionVisibility;
	private final Map<SingleOption, ControlOption> singleControlOptionMap = new HashMap<>();

	/**
	 *
	 * @param size The size that must include a width. This width will be applied to each option
	 * @param playerIndex The player index of the configurableObject or null for global
	 * @param configurableObject The object/controller that has ControlOptions to be altered by
	 * @param renderObject The RenderObject
	 * @param saveObject The SaveObject
	 */
	public ConfigurableObjectOptionProvider(Size size, Integer playerIndex, ConfigurableObject configurableObject,
											RenderObject renderObject, SaveObject saveObject, ControlOptionVisibility controlOptionVisibility){
		this.size = size;
		this.playerIndex = playerIndex;
		this.configurableObject = configurableObject;
		this.renderObject = renderObject;
		this.saveObject = saveObject;
		this.controlOptionVisibility = controlOptionVisibility;
		size.requireWidth();
	}

	@Override
	public Collection<? extends SingleOption> getOptionsToAdd() {
		List<SingleOption> r = new ArrayList<>();
		for(ControlOption option : configurableObject.getControlOptions()){
			if (!singleControlOptionMap.containsValue(option)) {
				if (controlOptionVisibility.shouldShow(option)) { // if its already in our map, don't add it
					SingleOption singleOption = getSingleOption(option);
					r.add(singleOption);
					singleControlOptionMap.put(singleOption, option);
				} else {
					/*
					This fixes a bug where the controls are changed. When the controls are changed, there may be a configuration
					in the previous controls that needs to be loaded into the new controls. The only reason we have to do this
					is because when its not visible, it doesn't get loaded. We could do this even if we are showing it, but
					there's no reason to because the getSingleOption(option) should load it.
					 */
					// This isn't called while the game is running, only while the options menu is open so it's fine that this may be a little inefficient
					saveObject.getOptionSaver().loadControlOption(playerIndex, option);
				}
			}
		}
		return r;

	}
	@Override
	public boolean shouldKeep(SingleOption singleOption) {
		ControlOption option = singleControlOptionMap.get(singleOption);
		if(option == null){
			throw new IllegalStateException("Unexpected singleOption: " + singleOption);
		}
		if(!configurableObject.getControlOptions().contains(option) || !controlOptionVisibility.shouldShow(option)){
			singleControlOptionMap.remove(singleOption);
			return false;
		}
		return true;
	}
	private SingleOption getSingleOption(ControlOption controlOption){
		OptionValue value = controlOption.getOptionValue();
		if(value.isOptionValueBoolean()){
			return new CheckBoxSingleOption(size, playerIndex, controlOption, saveObject.getOptionSaver(), renderObject);
		} else if(value.isOptionValueRadio()){
			return new DropDownSingleOption(size, playerIndex, controlOption, saveObject.getOptionSaver(), renderObject);
		} else {
			return new SliderSingleOption(size, playerIndex, controlOption, saveObject.getOptionSaver(), renderObject);
		}
	}

	public interface ControlOptionVisibility {
		boolean shouldShow(ControlOption controlOption);
	}

}
