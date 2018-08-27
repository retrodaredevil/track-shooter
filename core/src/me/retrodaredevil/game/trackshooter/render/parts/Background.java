package me.retrodaredevil.game.trackshooter.render.parts;

import com.badlogic.gdx.scenes.scene2d.Stage;

import me.retrodaredevil.game.trackshooter.RenderObject;
import me.retrodaredevil.game.trackshooter.Renderable;
import me.retrodaredevil.game.trackshooter.render.RenderComponent;
import me.retrodaredevil.game.trackshooter.util.RenderUtil;

public class Background implements Renderable {
	private final RenderObject renderObject;
	private final RenderComponent renderComponent;
	public Background(final RenderObject renderObject){
		this.renderObject = renderObject;
		this.renderComponent = new BackgroundRenderComponent();
	}
	@Override
	public RenderComponent getRenderComponent() {
		return renderComponent;
	}
	class BackgroundRenderComponent implements RenderComponent {
		@Override
		public void render(float delta, Stage stage) {
			RenderUtil.clearScreen(renderObject.getMainSkin().getColor("background"));
		}

		@Override
		public void dispose() {
		}
	}
}
