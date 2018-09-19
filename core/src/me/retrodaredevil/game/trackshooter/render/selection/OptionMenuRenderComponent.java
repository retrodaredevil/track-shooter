package me.retrodaredevil.game.trackshooter.render.selection;

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
import me.retrodaredevil.game.trackshooter.render.parts.options.OptionMenu;
import me.retrodaredevil.game.trackshooter.util.MathUtil;

public class OptionMenuRenderComponent implements RenderComponent {
	/*
	Some credit and help found at http://brokenshotgun.com/2014/02/08/libgdx-control-scene2d-buttons-with-a-controller/
	 */
	private static final int DEFAULT_INDEX = 0;
	private final OptionMenu optionMenu;
	private final RenderObject renderObject;
	private final ConfigurableControllerPart configController;
	private final GameInput menuController;

	private final Dialog dialog;
	private final Map<ControlOption, SingleOption> handleMap = new HashMap<>();

	private Integer selectedOptionIndex = null; // null represents none selected

	public OptionMenuRenderComponent(OptionMenu optionMenu, RenderObject renderObject,
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
		final JoystickPart selectJoystick = menuController.getMainJoystick();
		final InputPart selectButton = menuController.getFireButton();
		final InputPart backButton = menuController.getBackButton();
		final Collection<SingleOption.SelectAction> requestingActions = new ArrayList<>();
		Integer newOptionIndex = selectedOptionIndex;
		if(selectJoystick.getYAxis().isPressed()){ // will be true if digital position just changed to 1 or -1
			int digitalY = selectJoystick.getYAxis().getDigitalPosition();
			if(newOptionIndex == null){
				newOptionIndex = DEFAULT_INDEX;
			} else {
				newOptionIndex -= digitalY; // minus equals because the menu is shown top to bottom
			}
			requestingActions.add(SingleOption.SelectAction.CHANGE_OPTION);
		}
		if(backButton.isPressed()){
			requestingActions.add(SingleOption.SelectAction.EXIT_MENU);
		}

		final Collection<? extends ControlOption> controlOptions = configController.getControlOptions();
		newOptionIndex = newOptionIndex == null ? null : MathUtil.mod(newOptionIndex, controlOptions.size());
		final List<SingleOption> handledOptions = new ArrayList<>(); // maintains order
		int i = 0;
		for(ControlOption controlOption : controlOptions){
			SingleOption singleOption = handleMap.get(controlOption);
			if(singleOption == null){
				singleOption = getSingleOption(controlOption);
				handleMap.put(controlOption, singleOption);
			}
			handledOptions.add(singleOption);
			singleOption.renderUpdate(dialog.getContentTable(), optionMenu);
			if(selectedOptionIndex != null && i == selectedOptionIndex){
//				System.out.println("updating selected");
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
			if(selectedOptionIndex != null) {
				handledOptions.get(selectedOptionIndex).deselect();
			}
			selectedOptionIndex = newOptionIndex;
		}
		if(requestingActions.contains(SingleOption.SelectAction.EXIT_MENU)){
			optionMenu.closeMenu();
//			return;
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
