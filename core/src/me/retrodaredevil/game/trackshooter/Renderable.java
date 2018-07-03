package me.retrodaredevil.game.trackshooter;

import me.retrodaredevil.game.trackshooter.render.RenderComponent;

public interface Renderable {
	/**
	 * When this method is called, it is usually to call the return value's render (assuming null wasn't returned)
	 *
	 * @return The RenderComponent for this object or null if there is not currently one
	 */
	RenderComponent getRenderComponent();
}
