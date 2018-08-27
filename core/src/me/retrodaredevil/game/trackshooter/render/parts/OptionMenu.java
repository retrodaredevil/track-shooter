package me.retrodaredevil.game.trackshooter.render.parts;

import me.retrodaredevil.game.trackshooter.RenderObject;
import me.retrodaredevil.game.trackshooter.Renderable;
import me.retrodaredevil.game.trackshooter.render.RenderComponent;

public class OptionMenu implements Renderable {
	private final RenderObject renderObject;
	public OptionMenu(RenderObject renderObject) {
		this.renderObject = renderObject;
	}

	@Override
	public RenderComponent getRenderComponent() {
		return null;
	}
}
