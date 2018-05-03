package me.retrodaredevil.game.trackshooter.world;

import com.badlogic.gdx.math.Rectangle;
import me.retrodaredevil.game.trackshooter.CollisionHandler;
import me.retrodaredevil.game.trackshooter.entity.Entity;
import me.retrodaredevil.game.trackshooter.Renderable;
import me.retrodaredevil.game.trackshooter.Updateable;
import me.retrodaredevil.game.trackshooter.render.RenderComponent;
import me.retrodaredevil.game.trackshooter.render.WorldRenderComponent;

import java.util.*;

public class World implements Updateable, Renderable {

	private Track track;
	private final List<Entity> entities = new ArrayList<>();
	private ListIterator<Entity> currentIterator = null;

	private CollisionHandler collisionHandler;

	protected RenderComponent renderComponent;
	protected Rectangle bounds;

	public World(Track track, float width, float height){
		this.track = track;
		this.bounds = new Rectangle(width / -2f, height / -2f, width, height);
		this.renderComponent = new WorldRenderComponent(this);
		this.collisionHandler = new CollisionHandler();

	}


	@Override
	public void update(float delta, World theWorld) {
		assert theWorld == this || theWorld == null;

		for(currentIterator = entities.listIterator(); currentIterator.hasNext(); ){
			Entity entity = currentIterator.next();
			entity.update(delta, this);
			if(entity.shouldRemove(this)){
				currentIterator.remove();
				entity.afterRemove(this);
			}
		}
		currentIterator = null;
		this.collisionHandler.update(delta, this);
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

	/**
	 * A simple util method that takes a list and removes elements from the passed instance if they are removed
	 *
	 * @param entities The list of entities to remove removed entities from
	 */
	public static void updateEntityList(List<? extends Entity> entities){
		if(!entities.isEmpty()){
			for(Iterator<? extends Entity> it = entities.iterator(); it.hasNext();){
				Entity entity = it.next();
				if(entity.isRemoved()){
					it.remove();
				}
			}
		}
	}
}
