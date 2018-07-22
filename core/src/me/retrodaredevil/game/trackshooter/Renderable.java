package me.retrodaredevil.game.trackshooter;

import com.badlogic.gdx.utils.Disposable;

import me.retrodaredevil.game.trackshooter.render.RenderComponent;

// The reason for adding Disposable as a super interface was that a Renderable may have multiple
// render components that it switches between and calling getRenderComponent().disposeRenderComponent() would only disposeRenderComponent
// of one of them. By adding Disposable, there is more control
public interface Renderable {
	/**
	 * When this method is called, it is usually to call the return value's render (assuming null wasn't returned)
	 *
	 * @return The RenderComponent for this object or null if there is not currently one
	 */
	RenderComponent getRenderComponent();

	/**
	 * Should be called instead of using getRenderComponent().dispose()
	 * <br/>
	 * Also note that you are allowed to call this twice as this should only disposeRenderComponent of the render component
	 * if it has any.
	 */
	void disposeRenderComponent();
}
