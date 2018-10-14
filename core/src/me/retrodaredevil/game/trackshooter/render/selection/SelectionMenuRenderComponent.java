package me.retrodaredevil.game.trackshooter.render.selection;

import com.badlogic.gdx.scenes.scene2d.Stage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

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

	private final GameInput menuController;
	private final ContentTableProvider contentTableProvider;
	private final Collection<? extends SingleOptionProvider> optionProviders;
	private final ExitRequestListener exitRequestListener;
	private final List<OptionPair> singleOptionPairs = new ArrayList<>();


	private Integer selectedOptionIndex = null; // null represents none selected

	public SelectionMenuRenderComponent(RenderObject renderObject, GameInput menuController,
										ContentTableProvider contentTableProvider,
										Collection<? extends SingleOptionProvider> optionProviders,
										ExitRequestListener exitRequestListener){
		this.menuController = menuController;
		this.contentTableProvider = contentTableProvider;
		this.optionProviders = optionProviders;
		this.exitRequestListener = exitRequestListener;

	}

	@Override
	public void render(float delta, Stage stage) {
		contentTableProvider.render(delta, stage);

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
					if(selectedOptionIndex != null && i == selectedOptionIndex){
						singleOption.selectUpdate(delta, selectJoystick, selectButton, backButton, requestingActions);
					}
				}
			}
		}

		if(requestingActions.contains(SingleOption.SelectAction.CHANGE_OPTION)){
			if(selectedOptionIndex != null && selectedOptionIndex >= 0 && selectedOptionIndex < singleOptionPairs.size()) {
				singleOptionPairs.get(selectedOptionIndex).getSingleOption().deselect();
			}
			newOptionIndex = newOptionIndex == null ? null : MathUtil.mod(newOptionIndex, singleOptionPairs.size());
			selectedOptionIndex = newOptionIndex;
		}
		if(requestingActions.contains(SingleOption.SelectAction.EXIT_MENU)){
			exitRequestListener.onExit();
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
