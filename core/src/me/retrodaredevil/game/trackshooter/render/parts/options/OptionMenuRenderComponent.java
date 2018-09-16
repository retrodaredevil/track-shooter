package me.retrodaredevil.game.trackshooter.render.parts.options;

import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import me.retrodaredevil.controller.options.ConfigurableControllerPart;
import me.retrodaredevil.controller.options.ControlOption;
import me.retrodaredevil.controller.options.OptionValue;
import me.retrodaredevil.game.input.GameInput;
import me.retrodaredevil.game.trackshooter.RenderObject;
import me.retrodaredevil.game.trackshooter.render.components.RenderComponent;

public class OptionMenuRenderComponent implements RenderComponent {
	private final OptionMenu optionMenu;
	private final RenderObject renderObject;
	private final ConfigurableControllerPart configController;
	private final GameInput menuController;

//	private final Table table;
	private final Dialog dialog;
	private final Preferences optionPreferences;
	private final Map<ControlOption, SingleOption> handleMap = new HashMap<>();

	OptionMenuRenderComponent(OptionMenu optionMenu, RenderObject renderObject,
							  ConfigurableControllerPart configController, GameInput menuController, Preferences optionPreferences){
		this.optionMenu = optionMenu;
		this.renderObject = renderObject;
		this.configController = configController;
		this.menuController = menuController;

		this.dialog = new Dialog("Options", renderObject.getUISkin());
		this.dialog.setMovable(false);
		this.dialog.setResizable(false);

		this.optionPreferences = optionPreferences;
	}
	@Override
	public void render(float delta, Stage stage) {
		if(menuController.getBackButton().isDown()){
			optionMenu.closeMenu();
			return;
		}

		stage.addActor(dialog);
		dialog.setSize(stage.getWidth() * .8f, stage.getHeight() * .7f);
		dialog.setPosition(stage.getWidth() / 2f - dialog.getWidth() / 2f, stage.getHeight() / 2f - dialog.getHeight() / 2f);

		Set<SingleOption> handledOptionsSet = new HashSet<>();
		for(ControlOption controlOption : configController.getControlOptions()){
			SingleOption singleOption = handleMap.get(controlOption);
			if(singleOption == null){
				singleOption = getSingleOption(controlOption);
				handleMap.put(controlOption, singleOption);
			}
			handledOptionsSet.add(singleOption);
			singleOption.update(dialog.getContentTable(), optionMenu);

		}
		boolean shouldReset = false;
		for(Iterator<SingleOption> it = handleMap.values().iterator(); it.hasNext(); ){
			SingleOption singleOption = it.next();
			if(!handledOptionsSet.contains(singleOption)){
				singleOption.remove();
				it.remove();
				shouldReset = true;
			}
		}
		if(shouldReset){
			dialog.getContentTable().clearChildren();
		}

	}
	private SingleOption getSingleOption(ControlOption controlOption){
		OptionValue value = controlOption.getOptionValue();
		if(value.isOptionValueBoolean()){
			return new CheckBoxSingleOption(controlOption, renderObject);
		} else if(value.isOptionValueRadio()){
			return new DropDownSingleOption(controlOption, renderObject);
		} else {
			return new SliderSingleOption(controlOption, renderObject);
		}
	}

	public ConfigurableControllerPart getConfigController() {
		return configController;
	}

	@Override
	public void dispose() {
		for(SingleOption option : handleMap.values()){
			option.remove();
		}
		dialog.remove();
	}
}
