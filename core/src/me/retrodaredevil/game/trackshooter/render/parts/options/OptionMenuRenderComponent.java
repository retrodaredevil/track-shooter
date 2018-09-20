package me.retrodaredevil.game.trackshooter.render.parts.options;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.retrodaredevil.controller.options.ConfigurableControllerPart;
import me.retrodaredevil.controller.options.ControlOption;
import me.retrodaredevil.controller.options.OptionValue;
import me.retrodaredevil.game.input.GameInput;
import me.retrodaredevil.game.trackshooter.render.RenderObject;
import me.retrodaredevil.game.trackshooter.render.selection.CheckBoxSingleOption;
import me.retrodaredevil.game.trackshooter.render.selection.DropDownSingleOption;
import me.retrodaredevil.game.trackshooter.render.selection.SelectionMenuRenderComponent;
import me.retrodaredevil.game.trackshooter.render.selection.SingleOption;
import me.retrodaredevil.game.trackshooter.render.selection.SliderSingleOption;
import me.retrodaredevil.game.trackshooter.save.SaveObject;

public class OptionMenuRenderComponent extends SelectionMenuRenderComponent {
	private final Dialog dialog;
	private final ConfigurableControllerPart configController;
	private final OptionMenu optionMenu;
	private final SaveObject saveObject;

	private final Map<SingleOption, ControlOption> singleControlOptionMap = new HashMap<>();

	public OptionMenuRenderComponent(RenderObject renderObject, GameInput menuController,
									 ConfigurableControllerPart configurableControllerPart, OptionMenu optionMenu, SaveObject saveObject){
		super(renderObject, menuController);
		this.configController = configurableControllerPart;
		this.optionMenu = optionMenu;
		this.saveObject = saveObject;

		this.dialog = new Dialog("Options", renderObject.getUISkin());
		this.dialog.setMovable(false);
		this.dialog.setResizable(false);
	}
	public ConfigurableControllerPart getConfigController() {
		return configController;
	}


	@Override
	public void render(float delta, Stage stage) {
		stage.addActor(dialog);
		dialog.setSize(stage.getWidth() * .92f, stage.getHeight() * .7f);
		dialog.setPosition(stage.getWidth() / 2f - dialog.getWidth() / 2f, stage.getHeight() / 2f - dialog.getHeight() / 2f);

		super.render(delta, stage);
		if(isShouldExit()){
			optionMenu.closeMenu();
		}
	}

	@Override
	public void dispose() {
		super.dispose();
		dialog.remove();
	}

	@Override
	protected Table getContentTable() {
		return dialog.getContentTable();
	}

	@Override
	protected Collection<? extends SingleOption> getOptionsToAdd() {
		List<SingleOption> r = new ArrayList<>();
		for(ControlOption option : configController.getControlOptions()){
			if(!singleControlOptionMap.containsValue(option)){
				SingleOption singleOption = getSingleOption(option);
				r.add(singleOption);
				singleControlOptionMap.put(singleOption, option);
			}
		}
		return r;
	}

	@Override
	protected boolean shouldKeep(SingleOption singleOption) {
		return configController.getControlOptions().contains(singleControlOptionMap.get(singleOption));
	}
	private SingleOption getSingleOption(ControlOption controlOption){
		OptionValue value = controlOption.getOptionValue();
		if(value.isOptionValueBoolean()){
			return new CheckBoxSingleOption(controlOption, saveObject.getOptionSaver(), renderObject);
		} else if(value.isOptionValueRadio()){
			return new DropDownSingleOption(controlOption, saveObject.getOptionSaver(), renderObject);
		} else {
			return new SliderSingleOption(controlOption, saveObject.getOptionSaver(), renderObject);
		}
	}
}
