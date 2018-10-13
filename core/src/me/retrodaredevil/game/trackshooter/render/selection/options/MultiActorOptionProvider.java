package me.retrodaredevil.game.trackshooter.render.selection.options;

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

public class MultiActorOptionProvider implements SingleOptionProvider {

	private final Float width, height;
	private final Collection<? extends Actor> actors;
	private final Set<Actor> alreadyAdded = new HashSet<>();

	public MultiActorOptionProvider(Float width, Float height, Actor... actors){
		this.width = width;
		this.height = height;
		this.actors = Arrays.asList(actors);
	}

	@Override
	public Collection<? extends SingleOption> getOptionsToAdd() {
		final List<SingleOption> r = new ArrayList<>();
		for(Actor a : actors){
			if(!alreadyAdded.contains(a)){
				r.add(new PlainActorSingleOption(a, width, height));
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
