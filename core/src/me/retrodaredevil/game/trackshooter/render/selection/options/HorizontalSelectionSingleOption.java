package me.retrodaredevil.game.trackshooter.render.selection.options;

import com.badlogic.gdx.scenes.scene2d.ui.Table;

import java.util.ArrayList;
import java.util.Collection;

import me.retrodaredevil.controller.input.InputPart;
import me.retrodaredevil.controller.input.JoystickPart;
import me.retrodaredevil.game.trackshooter.render.selection.ContainerSingleOption;
import me.retrodaredevil.game.trackshooter.render.selection.OptionPair;
import me.retrodaredevil.game.trackshooter.render.selection.SingleOption;
import me.retrodaredevil.game.trackshooter.render.selection.SingleOptionProvider;
import me.retrodaredevil.game.trackshooter.util.Size;

public class HorizontalSelectionSingleOption extends ContainerSingleOption {

	private final Collection<OptionPair> singleOptions = new ArrayList<>();
    private final Collection<? extends SingleOptionProvider> optionProviders;
	private int index = 0;
	private boolean active = false;

	public HorizontalSelectionSingleOption(Size size, Collection<? extends SingleOptionProvider> optionProviders){
		super(size);
        this.optionProviders = optionProviders;
	}

	@Override
	protected void onUpdate(Table container) {
		super.onUpdate(container);
		for(SingleOptionProvider provider : optionProviders){
			for(SingleOption option : provider.getOptionsToAdd()){
				singleOptions.add(new OptionPair(option, provider));
			}
		}
	}

	@Override
	public void selectUpdate(float delta, JoystickPart selector, InputPart select, InputPart back, Collection<SelectAction> requestedActions) {
		active = true;
	}

	@Override
	public void deselect() {
		active = false;
	}
}
