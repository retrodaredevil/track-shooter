package me.retrodaredevil.game.trackshooter.render.components;


import com.badlogic.gdx.utils.Disposable;
import me.retrodaredevil.game.trackshooter.render.Renderable;

public interface RenderComponent extends Disposable {

	/**
	 * Handles rendering.
	 *
	 * For most things, you make sure that {@link com.badlogic.gdx.scenes.scene2d.Actor}s are in the correct position
	 * on a {@link com.badlogic.gdx.scenes.scene2d.Stage}. If you are handling your own stage, you will render that stage here.
	 *
	 * Although you must know that if you are drawing a stage on your own, the order in which multiple {@link RenderComponent}'s {@link #render(float)} method
	 * is called will affect whether something is on top or bottom. It is up to you to design how this is used so when things are finally
	 * drawn to the screen, they are in the correct order.
	 * @param delta
	 */
	void render(float delta);

	/**
	 * By default, does nothing. This should handle a resize of the screen, if necessary.
	 *
	 * This is usually done by {@link com.badlogic.gdx.scenes.scene2d.Stage#getViewport()}.{@link com.badlogic.gdx.utils.viewport.Viewport#update(int, int) update(int, int)}
	 * @param width The new width of the screen
	 * @param height The new height of the screen
	 */
	default void resize(int width, int height){}

	/**
	 * This should normally never be called directly on a {@link RenderComponent}. It is better to call {@link Renderable#disposeRenderComponent()}
	 * @see Disposable#dispose()
	 */
	@Override
	void dispose();
}
