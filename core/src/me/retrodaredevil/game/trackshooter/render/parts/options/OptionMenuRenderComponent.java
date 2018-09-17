package me.retrodaredevil.game.trackshooter.render.parts.options;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import me.retrodaredevil.controller.input.InputPart;
import me.retrodaredevil.controller.input.JoystickPart;
import me.retrodaredevil.controller.options.ConfigurableControllerPart;
import me.retrodaredevil.controller.options.ControlOption;
import me.retrodaredevil.controller.options.OptionValue;
import me.retrodaredevil.game.input.GameInput;
import me.retrodaredevil.game.trackshooter.RenderObject;
import me.retrodaredevil.game.trackshooter.render.components.RenderComponent;
import me.retrodaredevil.game.trackshooter.util.MathUtil;

public class OptionMenuRenderComponent implements RenderComponent {
	private final OptionMenu optionMenu;
	private final RenderObject renderObject;
	private final ConfigurableControllerPart configController;
	private final GameInput menuController;

//	private final Table table;
	private final Dialog dialog;
	private final Map<ControlOption, SingleOption> handleMap = new HashMap<>();

	private int selectedOptionIndex = 0;

	OptionMenuRenderComponent(OptionMenu optionMenu, RenderObject renderObject,
							  ConfigurableControllerPart configController, GameInput menuController){
		this.optionMenu = optionMenu;
		this.renderObject = renderObject;
		this.configController = configController;
		this.menuController = menuController;

		this.dialog = new Dialog("Options", renderObject.getUISkin());
		this.dialog.setMovable(false);
		this.dialog.setResizable(false);

	}
	@Override
	public void render(float delta, Stage stage) {

		stage.addActor(dialog);
		dialog.setSize(stage.getWidth() * .92f, stage.getHeight() * .7f);
		dialog.setPosition(stage.getWidth() / 2f - dialog.getWidth() / 2f, stage.getHeight() / 2f - dialog.getHeight() / 2f);

		// TODO These are all very temporary ways to get the inputs
		JoystickPart selectJoystick = menuController.getMainJoystick();
		InputPart selectButton = menuController.getFireButton();
		InputPart backButton = menuController.getBackButton();
		Collection<SingleOption.SelectAction> requestingActions = new ArrayList<>();
		int newOptionIndex = selectedOptionIndex;
		if(selectJoystick.getYAxis().isPressed()){ // will be true if digital position just changed to 1 or -1
			int digitalY = selectJoystick.getYAxis().getDigitalPosition();
			newOptionIndex -= digitalY; // minus equals because the menu is shown top to bottom
			requestingActions.add(SingleOption.SelectAction.CHANGE_OPTION);
		}
		if(backButton.isPressed()){
			requestingActions.add(SingleOption.SelectAction.EXIT_MENU);
		}

		Collection<? extends ControlOption> controlOptions = configController.getControlOptions();
		newOptionIndex = MathUtil.mod(newOptionIndex, controlOptions.size());
		List<SingleOption> handledOptions = new ArrayList<>(); // maintains order
		int i = 0;
		for(ControlOption controlOption : controlOptions){
			SingleOption singleOption = handleMap.get(controlOption);
			if(singleOption == null){
				singleOption = getSingleOption(controlOption);
				handleMap.put(controlOption, singleOption);
			}
			handledOptions.add(singleOption);
			singleOption.update(dialog.getContentTable(), optionMenu);
			if(i == selectedOptionIndex){
				singleOption.selectUpdate(delta, selectJoystick, selectButton, backButton, requestingActions);
			}

			i++;
		}
		// The reason we use 'shouldReset' is that there is a bug whenever one or multiple options
		// are removed, a blank row appears. This is the simplest solution but obviously isn't ideal.
		boolean shouldReset = false;
		for(Iterator<SingleOption> it = handleMap.values().iterator(); it.hasNext(); ){
			SingleOption singleOption = it.next();
			if(!handledOptions.contains(singleOption)){
				singleOption.remove();
				it.remove();
				shouldReset = true;
			}
		}
		if(shouldReset){
			dialog.getContentTable().clearChildren();
		}

		if(requestingActions.contains(SingleOption.SelectAction.CHANGE_OPTION)){
			handledOptions.get(selectedOptionIndex).deselect();
			selectedOptionIndex = newOptionIndex;
		}
		if(requestingActions.contains(SingleOption.SelectAction.EXIT_MENU)){
			optionMenu.closeMenu();
			return;
		}
//		System.out.println("selectedOptionIndex: " + selectedOptionIndex);

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
