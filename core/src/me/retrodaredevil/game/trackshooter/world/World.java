package me.retrodaredevil.game.trackshooter.world;

import me.retrodaredevil.game.trackshooter.Entity;
import me.retrodaredevil.game.trackshooter.Renderable;
import me.retrodaredevil.game.trackshooter.Updateable;
import me.retrodaredevil.game.trackshooter.render.RenderComponent;
import me.retrodaredevil.game.trackshooter.render.WorldRenderComponent;

import java.util.ArrayList;
import java.util.Collection;

public class World implements Updateable, Renderable {

	private Track track;
	private Collection<Entity> entities = new ArrayList<Entity>();

	protected RenderComponent renderComponent;

	public World(Track track){
		this.track = track;
		this.renderComponent = new WorldRenderComponent(this);
	}


	@Override
	public void update(float delta, World world) {
		assert world == this || world == null;

		for(Entity entity : entities){
			entity.update(delta, this);
		}
	}

	public Track getTrack(){
		return track;
	}
	public Collection<Entity> getEntities(){
		return entities;
	}

	@Override
	public RenderComponent getRenderComponent() {
		return renderComponent;
	}
}
