package me.retrodaredevil.game.trackshooter.render;

import me.retrodaredevil.game.trackshooter.render.components.RenderComponent;

public class ComponentRenderable implements Renderable {

	private final RenderComponent renderComponent;

	public ComponentRenderable(RenderComponent renderComponent) {
		this.renderComponent = renderComponent;
	}

	@Override
	public RenderComponent getRenderComponent() {
		return renderComponent;
	}

}
