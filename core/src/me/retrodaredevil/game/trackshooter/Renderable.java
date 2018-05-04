package me.retrodaredevil.game.trackshooter;

import me.retrodaredevil.game.trackshooter.render.RenderComponent;

public interface Renderable {
	/**
	 *
	 * @return The RenderComponent for this object or null if there is not currently one
	 */
	RenderComponent getRenderComponent();
}
