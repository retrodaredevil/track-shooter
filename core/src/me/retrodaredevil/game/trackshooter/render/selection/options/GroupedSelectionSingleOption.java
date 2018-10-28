package me.retrodaredevil.game.trackshooter.render.selection.options;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import java.util.Collection;
import java.util.Set;

import me.retrodaredevil.controller.input.InputPart;
import me.retrodaredevil.controller.input.JoystickPart;
import me.retrodaredevil.game.trackshooter.render.selection.ContainerSingleOption;
import me.retrodaredevil.game.trackshooter.render.selection.ContentTableProvider;
import me.retrodaredevil.game.trackshooter.render.selection.OptionHolder;
import me.retrodaredevil.game.trackshooter.render.selection.OptionPair;
import me.retrodaredevil.game.trackshooter.render.selection.SelectAction;
import me.retrodaredevil.game.trackshooter.render.selection.SingleOptionProvider;
import me.retrodaredevil.game.trackshooter.util.Size;

public class GroupedSelectionSingleOption extends ContainerSingleOption {

	private final boolean horizontal;
	private final OptionHolder optionHolder;

	public GroupedSelectionSingleOption(Size size, boolean horizontal, Collection<? extends SingleOptionProvider> optionProviders){
		super(size);
		this.horizontal = horizontal;
        this.optionHolder = new OptionHolder(0, new ContainerContentTableProvider(), optionProviders);
	}

	@Override
	protected void onRequestActions(Collection<? super SelectAction> requestedActions) {
		super.onRequestActions(requestedActions);
		optionHolder.updateOptions(requestedActions);
	}

	@Override
	public void selectUpdate(float delta, JoystickPart selector, InputPart select, InputPart back, Set<? super SelectAction> requestedActions) {
		optionHolder.setActive(true);
		int index = optionHolder.getSelectedOptionIndex();

		if(horizontal) {
			final InputPart xAxis = selector.getXAxis();
			if (xAxis.isPressed()) {
				index += xAxis.getDigitalPosition();
				requestedActions.add(SelectAction.CHANGE_OPTION);
			}
		} else {
			final InputPart yAxis = selector.getYAxis();
			if (yAxis.isPressed()) {
				index -= yAxis.getDigitalPosition();
				requestedActions.add(SelectAction.CHANGE_OPTION);
                index = Math.max(0, Math.min(index, optionHolder.getOptionPairs().size() - 1));
			}
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
	public void resetOption() {
		super.resetOption();
		for(OptionPair optionPair : optionHolder.getOptionPairs()){
			optionPair.getSingleOption().resetOption();
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
            return horizontal;
		}
	}
}
