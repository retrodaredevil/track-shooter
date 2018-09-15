package me.retrodaredevil.game.trackshooter.render.parts.options;

import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import java.util.HashMap;
import java.util.HashSet;
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

	private final Table table;
	private final Preferences optionPreferences;
	private final Map<ControlOption, SingleOption> handleMap = new HashMap<>();

	private boolean disposed = false;

	OptionMenuRenderComponent(OptionMenu optionMenu, RenderObject renderObject,
							  ConfigurableControllerPart configController, GameInput menuController, Preferences optionPreferences){
		this.optionMenu = optionMenu;
		this.renderObject = renderObject;
		this.configController = configController;
		this.menuController = menuController;

		table = new Table();
		table.setFillParent(true);

		this.optionPreferences = optionPreferences;
	}
	@Override
	public void render(float delta, Stage stage) {
		if(disposed) {
			System.err.println("Rendering when already disposed!");
		}
		if(menuController.getBackButton().isDown()){
			optionMenu.closeMenu();
		}

		stage.addActor(table);

		Set<SingleOption> handledOptions = new HashSet<>();
		for(ControlOption controlOption : configController.getControlOptions()){
			SingleOption singleOption = handleMap.get(controlOption);
			if(singleOption == null){
				OptionValue value = controlOption.getOptionValue();
				if(value.isOptionValueBoolean()){
					singleOption = new CheckBoxSingleOption(controlOption, renderObject);
				} else if(value.isOptionValueRadio()){
					singleOption = new DropDownSingleOption(controlOption, renderObject);
				} else {
					singleOption = new SliderSingleOption(controlOption, renderObject);
				}
				handleMap.put(controlOption, singleOption);
			}
			handledOptions.add(singleOption);
			singleOption.update(table, optionMenu);
		}
		for(SingleOption singleOption : handleMap.values()){
			if(!handledOptions.contains(singleOption)){
				singleOption.remove();
			}
		}

	}

	public ConfigurableControllerPart getConfigController() {
		return configController;
	}

	@Override
	public void dispose() {
		disposed = true;
		table.remove();
		table.reset();
		for(SingleOption option : handleMap.values()){
			option.remove();
		}
//		System.out.println("Disposing!");
	}
}
