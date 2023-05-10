package me.retrodaredevil.game.trackshooter.world;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import java.util.*;

import me.retrodaredevil.game.trackshooter.CollisionHandler;
import me.retrodaredevil.game.trackshooter.multiplayer.Multiplayer;
import me.retrodaredevil.game.trackshooter.render.RenderObject;
import me.retrodaredevil.game.trackshooter.render.Renderable;
import me.retrodaredevil.game.trackshooter.Updateable;
import me.retrodaredevil.game.trackshooter.entity.Entity;
import me.retrodaredevil.game.trackshooter.level.Level;
import me.retrodaredevil.game.trackshooter.level.LevelGetter;
import me.retrodaredevil.game.trackshooter.render.components.RenderComponent;
import me.retrodaredevil.game.trackshooter.render.components.WorldRenderComponent;


public class World implements Updateable, Renderable {

	private final Multiplayer multiplayer;
	private final LevelGetter levelGetter;
	private final RenderObject renderObject;
	private final WorldCoordinatesGetter worldCoordinatesGetter;
	private final Rectangle bounds;

	private final RenderComponent renderComponent;
	private final Updateable collisionHandler;
	private final Queue<Entity> entitiesToAdd = new LinkedList<>();
	private final List<Entity> entities = new ArrayList<>();

	/** The non-null level*/
	private Level level;
	private float timeInSeconds = 0;

	public World(Multiplayer multiplayer, LevelGetter levelGetter, float width, float height, RenderObject renderObject, WorldCoordinatesGetter worldCoordinatesGetter){
		this.multiplayer = multiplayer;
		this.levelGetter = levelGetter;
		this.renderObject = renderObject;
		this.worldCoordinatesGetter = worldCoordinatesGetter;
		this.bounds = new Rectangle(width / -2f, height / -2f, width, height);
		this.renderComponent = new WorldRenderComponent(this);
		if(multiplayer.isHost()) {
			this.collisionHandler = new CollisionHandler(this);
		} else {
			this.collisionHandler = (delta) -> {};
		}

		this.level = levelGetter.nextLevel(this);

	}

	public void getWorldCoordinates(int screenX, int screenY, Vector2 result){
		worldCoordinatesGetter.getWorldCoordinates(screenX, screenY, result);
	}


	@Override
	public void update(float delta) {
		timeInSeconds += delta;
		if(level.isDone()){
			level = levelGetter.nextLevel(this);
		}
		/*
		The ordering of how each thing is updated it on purpose:
		* Add entities that were added before this method was called,
		* Update the level so it can update the track, things on the track, etc before entities are updated
		* Update and remove entities if needed
		* Update the collision handler after everything has been placed correctly
		 */

		while(true){ // add all entities necessary
			Entity entity = entitiesToAdd.poll();
			if(entity == null){
				break;
			}
			entity.beforeSpawn();
			entities.add(entity);
		}
		this.level.update(delta); // update level

		for(Iterator<Entity> it = entities.listIterator(); it.hasNext(); ){ // update entities
			Entity entity = it.next();
			assert !entity.isRemoved();
			entity.update(delta);
			if(entity.shouldRemove()){
				it.remove();
				entity.afterRemove();
			}
		}
		this.collisionHandler.update(delta); // do collisions
	}

	public Skin getMainSkin(){
		return renderObject.getMainSkin();
	}
	public RenderObject getRenderObject(){ return renderObject; }

	/**
	 * This is the recommended way to get the track of the current level
	 * @return The current Track
	 */
	public Track getTrack(){
		return level.getTrack();
	}
	public Level getLevel(){
		return level;
	}

	/**
	 * Normally, it is not recommended to call this because you shouldn't need it that much.
	 * Use #addEntity() to add entities instead of this.
	 * @return A Collection of Entities
	 */
	public Collection<Entity> getEntities(){
		return entities;
	}

	/**
	 * @return All entities in {@link #getEntities()} and the entities that will be added next frame
	 */
	public Collection<Entity> getAllEntities(){
		List<Entity> r = new ArrayList<>(entities);
		r.addAll(entitiesToAdd);
		return r;
	}

	/**
	 * NOTE: This does not add it to the Collection returned in getEntities() immediately because it needs to add it next frame
	 * so it is initialized correctly
	 * <p>
	 * NOTE: Most of the time, you should use {@link Level#addEntity(Entity)}
	 * @param entity The entity to add to the list of entities next frame
	 */
	public void addEntity(Entity entity){
		entitiesToAdd.add(entity);
	}
	public Rectangle getBounds(){
		return bounds;
	}

	@Override
	public RenderComponent getRenderComponent() {
		return renderComponent;
	}

	/**
	 *
	 * @return The number of seconds this world has been active
	 */
	public float getTime(){
		return timeInSeconds;
	}

	public long getTimeMillis(){
		return (long) (timeInSeconds * 1000L);
	}

	/**
	 * A simple util method that takes a list and removes elements from the passed instance if they are removed
	 *
	 * @param entities The list of entities to remove removed entities from
	 */
	public static void updateEntityList(List<? extends Entity> entities){
		if(!entities.isEmpty()){
//			entities.removeIf(Entity::isRemoved);
			for(Iterator<? extends Entity> it = entities.iterator(); it.hasNext();){
				Entity entity = it.next();
				if(entity.isRemoved()){
					it.remove();
				}
			}
		}
	}
	public interface WorldCoordinatesGetter {
		void getWorldCoordinates(int screenX, int screenY, Vector2 result);
	}
}
