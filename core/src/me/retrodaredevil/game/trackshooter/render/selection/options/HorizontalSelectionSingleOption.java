package me.retrodaredevil.game.trackshooter.render.selection.options;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import java.util.Collection;
import java.util.Collections;

import me.retrodaredevil.controller.input.InputPart;
import me.retrodaredevil.controller.input.JoystickPart;
import me.retrodaredevil.game.trackshooter.render.selection.ContainerSingleOption;
import me.retrodaredevil.game.trackshooter.render.selection.ContentTableProvider;
import me.retrodaredevil.game.trackshooter.render.selection.OptionHolder;
import me.retrodaredevil.game.trackshooter.render.selection.SelectAction;
import me.retrodaredevil.game.trackshooter.render.selection.SingleOptionProvider;
import me.retrodaredevil.game.trackshooter.util.Size;

public class HorizontalSelectionSingleOption extends ContainerSingleOption {

	private final OptionHolder optionHolder;

	public HorizontalSelectionSingleOption(Size size, Collection<? extends SingleOptionProvider> optionProviders){
		super(size);
        this.optionHolder = new OptionHolder(0, new ContainerContentTableProvider(), optionProviders);
	}

	@Override
	protected void onRequestActions(Collection<? super SelectAction> requestedActions) {
		super.onRequestActions(requestedActions);
		optionHolder.updateOptions(requestedActions);
	}

	@Override
	public void selectUpdate(float delta, JoystickPart selector, InputPart select, InputPart back, Collection<? super SelectAction> requestedActions) {
		optionHolder.setActive(true);
		int index = optionHolder.getSelectedOptionIndex();

		final InputPart xAxis = selector.getXAxis();
		if(xAxis.isPressed()){
			index += xAxis.getDigitalPosition();
			requestedActions.add(SelectAction.CHANGE_OPTION);
		}
		optionHolder.updateSelection(delta, selector, select, back, requestedActions);
		if(requestedActions.contains(SelectAction.CHANGE_OPTION) && index != optionHolder.getSelectedOptionIndex()){
			requestedActions.remove(SelectAction.CHANGE_OPTION); // remove it because requestedAction isn't ours
			optionHolder.deselectCurrent();

			optionHolder.setSelectedOptionIndex(index);
			optionHolder.setActive(true);
		}
	}

	@Override
	public void deselect() {
		optionHolder.deselectCurrent();
	}

	private class ContainerContentTableProvider implements ContentTableProvider {

		@Override
		public Table getContentTable() {
            return container;
		}

		@Override
		public void resetTable() {
			container.clearChildren();
		}

		@Override
		public void render(float delta, Stage stage) {
			throw new UnsupportedOperationException("This ContentTableProvider cannot render!");
		}

		@Override
		public void dispose() {
			container.clearChildren();
		}

		@Override
		public boolean isHorizontal() {
            return true;
		}
	}
}
