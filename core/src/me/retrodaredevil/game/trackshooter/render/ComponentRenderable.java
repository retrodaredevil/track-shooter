package me.retrodaredevil.game.trackshooter.render;

import com.badlogic.gdx.scenes.scene2d.Stage;

import me.retrodaredevil.game.trackshooter.render.components.RenderComponent;

public class ComponentRenderable implements Renderable {

	private final RenderComponent renderComponent;
	private final Stage preferredStage;

	public ComponentRenderable(RenderComponent renderComponent, Stage preferredStage){
		this.renderComponent = renderComponent;
		this.preferredStage = preferredStage;
	}
	public ComponentRenderable(RenderComponent renderComponent){
		this(renderComponent, null);
	}

	@Override
	public RenderComponent getRenderComponent() {
		return renderComponent;
	}

	@Override
	public Stage getPreferredStage() {
		return preferredStage;
	}
}
