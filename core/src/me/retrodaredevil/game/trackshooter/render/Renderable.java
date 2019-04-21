package me.retrodaredevil.game.trackshooter.render;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Disposable;

import me.retrodaredevil.game.trackshooter.render.components.RenderComponent;

/**
 * Represents something that can be rendered if its {@link RenderComponent} isn't null.
 * <p>
 * When implementing, there is very little reason to override {@link #disposeRenderComponent()} unless
 * you switch between different RenderComponents. The other methods should probably not be overridden.
 * <p>
 * When rendering, you should almost always use a {@link Renderer} to allow to easier refactoring in the
 * future and because Renderer contains everything you need to render Renderables in the order you want.
 */
public interface Renderable {
	/**
	 * When this method is called, it is usually to call the return value's render (assuming null wasn't returned)
	 *
	 * @return The RenderComponent for this object or null if there is not currently one
	 */
	RenderComponent getRenderComponent();


	/**
	 * SHOULD NEVER BE OVERRIDDEN
	 * @see RenderComponent#render(float)
	 */
	default void render(float delta){
		RenderComponent renderComponent = getRenderComponent();
		if(renderComponent != null){
			renderComponent.render(delta);
		}
	}


	/**
	 * SHOULD NEVER BE OVERRIDDEN.
	 * @see RenderComponent#resize(int, int)
	 */
	default void resize(int width, int height){
		RenderComponent renderComponent = getRenderComponent();
		if(renderComponent != null){
			renderComponent.resize(width, height);
		}
	}

	/**
	 * Should be called instead of using getRenderComponent().dispose()
	 * <br/>
	 * Also note that you are allowed to call this twice as this should only disposeRenderComponent of the render component
	 * if it has any.
	 * <br/>
	 * HOWEVER, If this for whatever reason implements Disposable, then calling dispose() should call this method as well.
	 */
	default void disposeRenderComponent(){
		RenderComponent renderComponent = getRenderComponent();
		if(renderComponent != null){
			renderComponent.dispose();
		}
	}

}
