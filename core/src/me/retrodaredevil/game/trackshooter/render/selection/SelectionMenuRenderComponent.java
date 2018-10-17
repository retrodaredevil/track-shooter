package me.retrodaredevil.game.trackshooter.render.selection;

import com.badlogic.gdx.scenes.scene2d.Stage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

import me.retrodaredevil.controller.input.InputPart;
import me.retrodaredevil.controller.input.JoystickPart;
import me.retrodaredevil.game.trackshooter.input.GameInput;
import me.retrodaredevil.game.trackshooter.render.RenderObject;
import me.retrodaredevil.game.trackshooter.render.components.RenderComponent;
import me.retrodaredevil.game.trackshooter.util.MathUtil;

/**
 * A class that deals with rendering and logic for a selection menu
 */
public class SelectionMenuRenderComponent implements RenderComponent {

	private static final int DEFAULT_INDEX = 0;

	private final ContentTableProvider contentTableProvider;
	private final Collection<? extends SingleOptionProvider> optionProviders;
	private final ExitRequestListener exitRequestListener;
	private final List<OptionPair> singleOptionPairs = new ArrayList<>();

	/** The menu controller. May be null*/
	private GameInput menuController;

	/** The option index. null represents none selected*/
	private Integer selectedOptionIndex = null;

	public SelectionMenuRenderComponent(RenderObject renderObject, GameInput menuController,
										ContentTableProvider contentTableProvider,
										Collection<? extends SingleOptionProvider> optionProviders,
										ExitRequestListener exitRequestListener){
		setMenuController(menuController);
		this.contentTableProvider = contentTableProvider;
		this.optionProviders = optionProviders;
		this.exitRequestListener = exitRequestListener;

	}
	public void setMenuController(GameInput menuController){ this.menuController = menuController; }
	public GameInput getMenuController(){ return menuController; }

	public void clearTable(){
		contentTableProvider.resetTable();
	}

	@Override
	public void render(float delta, Stage stage) {
		contentTableProvider.render(delta, stage);

		final JoystickPart selectJoystick = menuController == null ? null : Objects.requireNonNull(menuController.getSelectorJoystick());
		final InputPart selectButton = menuController == null ? null : Objects.requireNonNull(menuController.getEnterButton());
		final InputPart backButton = menuController == null ? null : Objects.requireNonNull(menuController.getBackButton());
		final Collection<SingleOption.SelectAction> requestingActions = new ArrayList<>();
		Integer newOptionIndex = selectedOptionIndex;
		if(menuController != null) {
			if (selectJoystick.getYAxis().isPressed()) { // will be true if digital position just changed to 1 or -1
				int digitalY = selectJoystick.getYAxis().getDigitalPosition();
				if (newOptionIndex == null) {
					newOptionIndex = DEFAULT_INDEX;
				} else {
					newOptionIndex -= digitalY; // minus equals because the menu is shown top to bottom
				}
				requestingActions.add(SingleOption.SelectAction.CHANGE_OPTION);
			}
			if (backButton.isPressed()) {
				requestingActions.add(SingleOption.SelectAction.EXIT_MENU);
			}
		}

		for(SingleOptionProvider provider : optionProviders){
			for(SingleOption option : provider.getOptionsToAdd()){
				singleOptionPairs.add(new OptionPair(option, provider));
			}
		}
		{
			int i = 0;
			for (final Iterator<OptionPair> it = singleOptionPairs.iterator(); it.hasNext(); i++) {
				OptionPair optionPair = it.next();
				SingleOption singleOption = optionPair.getSingleOption();
				SingleOptionProvider provider = optionPair.getProvider();
				if (!provider.shouldKeep(singleOption)) {
					singleOption.remove();
					it.remove();
				} else {
					singleOption.renderUpdate(contentTableProvider.getContentTable());
					if(menuController != null && selectedOptionIndex != null && i == selectedOptionIndex){
						singleOption.selectUpdate(delta, selectJoystick, selectButton, backButton, requestingActions);
					}
				}
			}
		}

		if(requestingActions.contains(SingleOption.SelectAction.CHANGE_OPTION)){
			deselectCurrent();
			newOptionIndex = newOptionIndex == null ? null : MathUtil.mod(newOptionIndex, singleOptionPairs.size());
			selectedOptionIndex = newOptionIndex;
		}
		if(requestingActions.contains(SingleOption.SelectAction.EXIT_MENU)){
			deselectCurrent();
			selectedOptionIndex = null;
			exitRequestListener.onExit();
		}

	}
	private void deselectCurrent(){

		if(selectedOptionIndex != null && selectedOptionIndex >= 0 && selectedOptionIndex < singleOptionPairs.size()) {
			singleOptionPairs.get(selectedOptionIndex).getSingleOption().deselect();
		}
	}

	@Override
	public void dispose() {
		contentTableProvider.dispose();
		for(OptionPair pair : singleOptionPairs){
			pair.getSingleOption().remove();
		}
	}
	private static class OptionPair {
		private final SingleOption singleOption;
		private final SingleOptionProvider provider;

		OptionPair(SingleOption singleOption, SingleOptionProvider provider){
			this.singleOption = singleOption;
			this.provider = provider;
		}
		SingleOption getSingleOption(){ return singleOption; }
		SingleOptionProvider getProvider(){ return provider; }
	}
	public interface ExitRequestListener {
		void onExit();
	}
}
