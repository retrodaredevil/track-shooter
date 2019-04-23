package me.retrodaredevil.game.trackshooter.render.components;

import com.badlogic.gdx.scenes.scene2d.Stage;
import me.retrodaredevil.game.trackshooter.render.Renderable;

public class StageRenderable implements Renderable {
	private final RenderComponent renderComponent;

	public StageRenderable(Stage stage) {
		renderComponent = new StageRenderComponent(stage);
	}

	@Override
	public RenderComponent getRenderComponent() {
		return renderComponent;
	}
	private static class StageRenderComponent implements RenderComponent {
		private final Stage stage;

		private StageRenderComponent(Stage stage) {
			this.stage = stage;
		}

		@Override
		public void render(float delta) {
			stage.act(delta);
			stage.draw();
		}

		@Override
		public void dispose() {
			stage.dispose();
		}
	}
}
