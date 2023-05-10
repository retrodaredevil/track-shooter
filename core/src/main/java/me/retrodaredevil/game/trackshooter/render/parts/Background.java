package me.retrodaredevil.game.trackshooter.render.parts;

import com.badlogic.gdx.scenes.scene2d.Stage;

import me.retrodaredevil.game.trackshooter.render.RenderObject;
import me.retrodaredevil.game.trackshooter.render.Renderable;
import me.retrodaredevil.game.trackshooter.render.components.RenderComponent;
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
//		private ClearActor clear = new ClearActor();
		@Override
		public void render(float delta, Stage stage) {
			RenderUtil.clearScreen(renderObject.getMainSkin().getColor("background"));
		}

		@Override
		public void dispose() {
		}
	}
//	class ClearActor extends Actor {
//		@Override
//		public void draw(Batch batch, float parentAlpha) {
//			super.draw(batch, parentAlpha);
//			RenderUtil.clearScreen(renderObject.getMainSkin().getColor("background"));
//		}
//	}
}
