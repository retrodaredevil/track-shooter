package me.retrodaredevil.game.trackshooter.world;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;
import me.retrodaredevil.game.trackshooter.Entity;
import me.retrodaredevil.game.trackshooter.Renderable;
import me.retrodaredevil.game.trackshooter.Updateable;
import me.retrodaredevil.game.trackshooter.render.RenderComponent;
import me.retrodaredevil.game.trackshooter.render.WorldRenderComponent;

import java.util.*;

public class World implements Updateable, Renderable {

	private Track track;
	private List<Entity> entities = new ArrayList<>();
	private ListIterator<Entity> currentIterator = null;

	protected RenderComponent renderComponent;
	protected Rectangle bounds;

	public World(Track track, float width, float height){
		this.track = track;
		this.bounds = new Rectangle(width / -2f, height / -2f, width, height);
		this.renderComponent = new WorldRenderComponent(this);

	}


	@Override
	public void update(float delta, World world) {
		assert world == this || world == null;

		for(currentIterator = entities.listIterator(); currentIterator.hasNext(); ){
			Entity entity = currentIterator.next();
			entity.update(delta, this);
			if(entity.shouldRemove(this)){
				entity.afterRemove(this);
				currentIterator.remove();
			}
		}
		currentIterator = null;
	}

	public Track getTrack(){
		return track;
	}

	/**
	 * Normally, it is not recommended to call this because you shouldn't need it that much.
	 * Use #addEntity() to add entities instead of this.
	 * @return A Collection of Entities
	 */
	public Collection<Entity> getEntities(){
		return entities;
	}
	public void addEntity(Entity entity){
		if(currentIterator != null){
			currentIterator.add(entity);
		} else {
			entities.add(entity);
		}
	}
	public Rectangle getBounds(){
		return bounds;
	}

	@Override
	public RenderComponent getRenderComponent() {
		return renderComponent;
	}
}
