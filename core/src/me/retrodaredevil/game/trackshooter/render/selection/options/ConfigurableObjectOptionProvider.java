package me.retrodaredevil.game.trackshooter.render.selection.options;

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
import me.retrodaredevil.game.trackshooter.save.SaveObject;
import me.retrodaredevil.game.trackshooter.util.Size;

public class ConfigurableObjectOptionProvider implements SingleOptionProvider {
	/*
	Some credit and help found at http://brokenshotgun.com/2014/02/08/libgdx-control-scene2d-buttons-with-a-controller/
	 */

	private final Size size;
	private final int playerIndex;
	private final ConfigurableObject configurableObject;
	private final RenderObject renderObject;
	private final SaveObject saveObject;
	private final Map<SingleOption, ControlOption> singleControlOptionMap = new HashMap<>();

	public ConfigurableObjectOptionProvider(Size size, int playerIndex, ConfigurableObject configurableObject, RenderObject renderObject, SaveObject saveObject){
		this.size = size;
		this.playerIndex = playerIndex;
		this.configurableObject = configurableObject;
		this.renderObject = renderObject;
		this.saveObject = saveObject;
	}

	@Override
	public Collection<? extends SingleOption> getOptionsToAdd() {
		List<SingleOption> r = new ArrayList<>();
		for(ControlOption option : configurableObject.getControlOptions()){
			if(!singleControlOptionMap.containsValue(option)){ // if its already in our map, don't add it
				SingleOption singleOption = getSingleOption(option);
				r.add(singleOption);
				singleControlOptionMap.put(singleOption, option);
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
		if(!configurableObject.getControlOptions().contains(option)){
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

}
