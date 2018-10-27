package me.retrodaredevil.game.trackshooter.render.selection.options.providers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import me.retrodaredevil.game.trackshooter.render.selection.SingleOption;
import me.retrodaredevil.game.trackshooter.render.selection.SingleOptionProvider;

public class BasicOptionProvider implements SingleOptionProvider {
	private final Collection<SingleOption> optionsToAdd;

	public BasicOptionProvider(Collection<? extends SingleOption> optionsToAdd){
		this.optionsToAdd = new ArrayList<>(optionsToAdd);
	}
	public BasicOptionProvider(SingleOption... optionsToAdd){
		this(Arrays.asList(optionsToAdd));
	}

	@Override
	public Collection<? extends SingleOption> getOptionsToAdd() {
        List<SingleOption> r = new ArrayList<>(optionsToAdd);
        optionsToAdd.clear();
        return r;
	}

	@Override
	public boolean shouldKeep(SingleOption singleOption) {
        return true;
	}
}
