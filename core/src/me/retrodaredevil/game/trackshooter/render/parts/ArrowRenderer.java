package me.retrodaredevil.game.trackshooter.render.parts;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import me.retrodaredevil.game.trackshooter.render.RenderObject;
import me.retrodaredevil.game.trackshooter.render.Renderable;
import me.retrodaredevil.game.trackshooter.render.components.RenderComponent;
import me.retrodaredevil.game.trackshooter.render.components.RenderComponentMultiplexer;

import static java.lang.Math.max;
import static java.lang.Math.min;

public class ArrowRenderer implements Renderable {

	private final RenderObject renderObject;
	private final Stage stage;
	private final RenderComponentMultiplexer multiplexer = new RenderComponentMultiplexer();

	public ArrowRenderer(RenderObject renderObject) {
		this.renderObject = renderObject;
		stage = new Stage(new ScreenViewport(), renderObject.getBatch());
	}
	public void createArrow(ShouldShowArrow shouldShowArrow){
		multiplexer.addComponent(new ArrowRenderComponent(shouldShowArrow, renderObject));
	}

	@Override
	public RenderComponent getRenderComponent() {
		return multiplexer;
	}

	@Override
	public Stage getPreferredStage() {
		return stage;
	}

	public interface ShouldShowArrow {
		boolean shouldShow();
		boolean isHorizontal();
		boolean isLeft();
	}
	static class ArrowRenderComponent implements RenderComponent {

		private final ShouldShowArrow shouldShowArrow;
		// note that the arrow image is horizontal by default
		private final Image image;

		ArrowRenderComponent(ShouldShowArrow shouldShowArrow, RenderObject renderObject) {
			this.shouldShowArrow = shouldShowArrow;
			image = new Image(renderObject.getMainSkin().getDrawable("arrow"));
		}

		@Override
		public void render(float delta, Stage stage) {
			stage.addActor(image);
			Color color = image.getColor();
			float a = color.a;
			if(shouldShowArrow.shouldShow()){
				a += delta / .4f;
			} else {
				a -= delta / .2f;
			}
			a = max(0, min(a, 1));
			color.a = a;

			if(shouldShowArrow.isHorizontal()){
				image.setRotation(0);
			} else {
				image.setRotation(90);
			}
			float width = stage.getWidth();
			float height = stage.getHeight();

			image.setOrigin(Align.center);
			image.setScale(height / image.getWidth() * .36f);

			final float x;
			if(shouldShowArrow.isLeft()){
				x = width * .15f;
			} else {
				x = width * (1 - .15f);
			}
			image.setPosition(
					x,
					height * .5f
			);
		}

		@Override
		public void dispose() {
			image.remove();
		}
	}
}
