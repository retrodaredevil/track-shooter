package me.retrodaredevil.game.trackshooter.render.selection.options.providers;

import com.badlogic.gdx.scenes.scene2d.Actor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import me.retrodaredevil.game.trackshooter.render.selection.PlainActorSingleOption;
import me.retrodaredevil.game.trackshooter.render.selection.SingleOption;
import me.retrodaredevil.game.trackshooter.render.selection.SingleOptionProvider;
import me.retrodaredevil.game.trackshooter.util.Size;

public class MultiActorOptionProvider implements SingleOptionProvider {

	private final Size size;
	private final Collection<? extends Actor> actors;
	private final Set<Actor> alreadyAdded = new HashSet<>();

	/**
	 *
	 * @param size The size that will be applied to each actors' cell
	 * @param actors The actors that will be "provided" to be added
	 */
	public MultiActorOptionProvider(Size size, Actor... actors){
		this.size = size;
		this.actors = Arrays.asList(actors);
	}

	@Override
	public Collection<? extends SingleOption> getOptionsToAdd() {
		final List<SingleOption> r = new ArrayList<>();
		for(Actor a : actors){
			if(!alreadyAdded.contains(a)){
				r.add(new PlainActorSingleOption(a, size));
				alreadyAdded.add(a);
			}
		}
		return r;
	}

	@Override
	public boolean shouldKeep(SingleOption singleOption) {
		return true;
	}
}
