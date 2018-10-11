package me.retrodaredevil.game.trackshooter.render.selection;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import me.retrodaredevil.controller.input.InputPart;
import me.retrodaredevil.controller.input.JoystickPart;
import me.retrodaredevil.controller.options.ConfigurableControllerPart;
import me.retrodaredevil.game.input.GameInput;
import me.retrodaredevil.game.trackshooter.render.RenderObject;
import me.retrodaredevil.game.trackshooter.render.components.RenderComponent;
import me.retrodaredevil.game.trackshooter.util.MathUtil;

/**
 * An abstract class that deals with rendering and logic for a selection menu
 * <p>
 * TODO This class is abstract for good reason. However, in the future, we may be able to make...
 * this less abstract and have a list/collection of things that can add SingleOptions. This way,
 * instead of providing implementation for a content table and SingleOptions to add, it can just
 * do one of those things which will make this easier to extend and make changes to.
 */
public abstract class SelectionMenuRenderComponent implements RenderComponent {
	/*
	Some credit and help found at http://brokenshotgun.com/2014/02/08/libgdx-control-scene2d-buttons-with-a-controller/
	 */
	private static final int DEFAULT_INDEX = 0;
//	private final OptionMenu optionMenu;
	protected final RenderObject renderObject;
//	private final ConfigurableControllerPart configController;
	protected final GameInput menuController;

	private final List<SingleOption> singleOptions = new ArrayList<>();


	private Integer selectedOptionIndex = null; // null represents none selected
	private boolean shouldExit = false;

	public SelectionMenuRenderComponent(RenderObject renderObject, GameInput menuController){
		this.renderObject = renderObject;
		this.menuController = menuController;


	}
	public boolean isShouldExit(){
		return shouldExit;
	}
	public Collection<? extends SingleOption> getOptions(){
		return singleOptions;
	}
	protected abstract Table getContentTable();

	/**
	 * When this is called, the elements the the returned Collection are GUARANTEED to be added to the
	 * list/collection of SingleOptions. This means that you can do whatever logic you need to do to
	 * the returned value before returning it.
	 * @return The options to add
	 */
	protected abstract Collection<? extends SingleOption> getOptionsToAdd();
	protected abstract boolean shouldKeep(SingleOption singleOption);
	protected void resetTable(){
		getContentTable().clearChildren();
	}
	@Override
	public void render(float delta, Stage stage) {


		final JoystickPart selectJoystick = menuController.getSelectorJoystick();
		final InputPart selectButton = menuController.getEnterButton();
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

		Collection<? extends SingleOption> optionsToAdd = getOptionsToAdd();
		singleOptions.addAll(optionsToAdd);
		{
			int i = 0;
			for (Iterator<SingleOption> it = singleOptions.iterator(); it.hasNext(); i++) {
				SingleOption singleOption = it.next();
				if (!this.shouldKeep(singleOption)) {
					singleOption.remove();
					it.remove();
				} else {
					singleOption.renderUpdate(getContentTable());
					if(selectedOptionIndex != null && i == selectedOptionIndex){
						singleOption.selectUpdate(delta, selectJoystick, selectButton, backButton, requestingActions);
					}
				}
			}
		}

		if(requestingActions.contains(SingleOption.SelectAction.CHANGE_OPTION)){
			if(selectedOptionIndex != null && selectedOptionIndex >= 0 && selectedOptionIndex < singleOptions.size()) {
				singleOptions.get(selectedOptionIndex).deselect();
			}
			newOptionIndex = newOptionIndex == null ? null : MathUtil.mod(newOptionIndex, singleOptions.size());
			selectedOptionIndex = newOptionIndex;
		}
		if(requestingActions.contains(SingleOption.SelectAction.EXIT_MENU)){
			shouldExit = true;
		}

	}

	@Override
	public void dispose() {
		for(SingleOption option : singleOptions){
			option.remove();
		}
	}
}
