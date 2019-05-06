package me.retrodaredevil.game.trackshooter.render.selection;

import com.badlogic.gdx.scenes.scene2d.Stage;

import java.util.Collection;
import java.util.EnumSet;
import java.util.Set;

import me.retrodaredevil.controller.input.InputPart;
import me.retrodaredevil.controller.input.JoystickPart;
import me.retrodaredevil.game.trackshooter.input.GameInput;
import me.retrodaredevil.game.trackshooter.render.RenderObject;
import me.retrodaredevil.game.trackshooter.render.components.RenderComponent;

import static java.util.Objects.requireNonNull;

/**
 * A class that deals with rendering and logic for a selection menu
 */
public class SelectionMenuRenderComponent implements RenderComponent {

	private final OptionHolder optionHolder;
	private final ExitRequestListener exitRequestListener;

	/** The menu controller. May be null*/
	private GameInput menuController = null;
	/** The player index of the player operating the menu*/
	private Integer playerIndex = null;


	/**
	 *
	 * @param renderObject The render object
	 * @param playerIndex The player index
	 * @param menuController The controller that will control this menu
	 * @param contentTableProvider The content table provider. This will automatically be disposed of
	 * @param optionProviders The option providers
	 * @param exitRequestListener Will be called when the back button is pressed
	 */
	public SelectionMenuRenderComponent(RenderObject renderObject, Integer playerIndex, GameInput menuController,
										ContentTableProvider contentTableProvider, Collection<? extends SingleOptionProvider> optionProviders, ExitRequestListener exitRequestListener){
		setMenuController(playerIndex, menuController);
		this.optionHolder = new OptionHolder(0, contentTableProvider, optionProviders);
		this.exitRequestListener = exitRequestListener;

	}
	public void setMenuController(Integer playerIndex, GameInput menuController){
		this.playerIndex = playerIndex;
		this.menuController = menuController;
	}
	public GameInput getMenuController(){ return menuController; }
	public Integer getPlayerIndex(){ return playerIndex; }

	public void clearTable(){
		optionHolder.getContentTableProvider().resetTable();
	}

	public void resetAll(){
		for(OptionPair optionPair : optionHolder.getOptionPairs()){
			optionPair.getSingleOption().resetOption();
		}
	}

	@Override
	public void render(float delta, Stage stage) {

		final JoystickPart selectJoystick = menuController == null ? null : requireNonNull(menuController.getSelectorJoystick());
		final InputPart selectButton = menuController == null ? null : requireNonNull(menuController.getEnterButton());
		final InputPart backButton = menuController == null ? null : requireNonNull(menuController.getBackButton());
		final Set<SelectAction> requestingActions = EnumSet.noneOf(SelectAction.class);
		boolean active = optionHolder.isActive();
		int newOptionIndex = optionHolder.getSelectedOptionIndex();
		if(menuController != null) {
			if (selectJoystick.getYAxis().isPressed()) { // will be true if digital position just changed to 1 or -1
				int digitalY = selectJoystick.getYAxis().getDigitalPosition();
				if(active) {
					newOptionIndex -= digitalY; // minus equals because the menu is shown top to bottom
				} else {
					active = true;
				}
				requestingActions.add(SelectAction.CHANGE_OPTION);
			}
			if (backButton.isPressed()) {
				requestingActions.add(SelectAction.EXIT_MENU);
			}
		}
		optionHolder.getContentTableProvider().render(delta, stage);
		optionHolder.updateOptions(requestingActions);
		optionHolder.updateSelection(delta, selectJoystick, selectButton, backButton, requestingActions);


		if(requestingActions.contains(SelectAction.CHANGE_OPTION)){
            optionHolder.deselectCurrent();
            optionHolder.setSelectedOptionIndex(newOptionIndex);
			optionHolder.setActive(active);
		}
		if(requestingActions.contains(SelectAction.EXIT_MENU)){
            optionHolder.deselectCurrent();
			exitRequestListener.onExit();
		}

	}

	@Override
	public void dispose() {
		optionHolder.getContentTableProvider().dispose();
		for(OptionPair optionPair : optionHolder.getOptionPairs()){
			optionPair.getSingleOption().remove();
		}
	}
	public interface ExitRequestListener {
		void onExit();
	}
}
