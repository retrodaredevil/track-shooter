package me.retrodaredevil.game.trackshooter.world;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import me.retrodaredevil.game.trackshooter.CollisionHandler;
import me.retrodaredevil.game.trackshooter.Renderable;
import me.retrodaredevil.game.trackshooter.Updateable;
import me.retrodaredevil.game.trackshooter.entity.Entity;
import me.retrodaredevil.game.trackshooter.level.Level;
import me.retrodaredevil.game.trackshooter.level.LevelGetter;
import me.retrodaredevil.game.trackshooter.render.RenderComponent;
import me.retrodaredevil.game.trackshooter.render.WorldRenderComponent;

import java.util.*;

public class World implements Updateable, Renderable {
//	private static final Vector2 temp = new Vector2();


	private final LevelGetter levelGetter;
	private final Skin skin;
	private Level level;

	private final Queue<Entity> entitiesToAdd = new ArrayDeque<>();
	private final List<Entity> entities = new ArrayList<>();
	private boolean iteratingEntities = false;

	private CollisionHandler collisionHandler;
	private final Rectangle bounds;
//	private final Rectangle largeBounds = new Rectangle(); // shouldn't be referenced without getLargeBounds()

	protected RenderComponent renderComponent;

	public World(LevelGetter levelGetter, float width, float height, Skin skin){
		this.levelGetter = levelGetter;
		this.skin = skin;
		this.bounds = new Rectangle(width / -2f, height / -2f, width, height);
		this.renderComponent = new WorldRenderComponent(this);
		this.collisionHandler = new CollisionHandler();

		this.level = levelGetter.nextLevel();

	}


	@Override
	public void update(float delta, World theWorld) {
		assert theWorld == this || theWorld == null;
		if(level == null || level.isDone()){
			level = levelGetter.nextLevel();
		}

		while(true){
			Entity entity = entitiesToAdd.poll();
			if(entity == null){
				break;
			}
			entity.beforeSpawn(this);
			entities.add(entity);
		}
		for(Iterator<Entity> it = entities.listIterator(); it.hasNext(); ){
			Entity entity = it.next();
			assert !entity.isRemoved();
			entity.update(delta, this);
			if(entity.shouldRemove(this)){
				it.remove();
				entity.afterRemove(this);
			}
		}
		this.collisionHandler.update(delta, this);
		this.level.update(delta, this);
	}

	public Skin getSkin(){
		return skin;
	}

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
	 * NOTE: This does not add it to the Collection returned in getEntities() immediately because it needs to add it next frame
	 * so it is initialized correctly
	 * @param entity The entity to add to the list of entities next frame
	 */
	public void addEntity(Entity entity){
		entitiesToAdd.add(entity);
	}
	public Rectangle getBounds(){
		return bounds;
	}

//	public Rectangle getLargeBounds(){ maybe use in future?
//		largeBounds.setSize(getBounds().getWidth() * 1.5f, getBounds().getHeight() * 1.5f);
//		largeBounds.setCenter(getBounds().getCenter(temp));
//		return largeBounds;
//	}

	@Override
	public RenderComponent getRenderComponent() {
		return renderComponent;
	}

	@Override
	public void disposeRenderComponent() {
		if(renderComponent != null){
			renderComponent.dispose();
		}
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
