package me.retrodaredevil.game.trackshooter.render.components;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;

public class LineRenderComponent implements RenderComponent {
	private final Vector2 start = new Vector2();
	private final Vector2 end = new Vector2();

	private final float width;
	private final ShapeRenderer renderer;

	private final LineActor line = new LineActor();
	private boolean disposed = false;

	/**
	 * Note when disposeRenderComponent() is called, it will NOT call renderer.disposeRenderComponent().
	 */
	public LineRenderComponent(Vector2 start, Vector2 end, Color color, float width, ShapeRenderer renderer){
		this.start.set(start);
		this.end.set(end);
		this.width = width;
		this.renderer = renderer;

		renderer.setColor(color);
	}
	@Override
	public void render(float delta, Stage stage) {
		if(line.getStage() != stage && !disposed){
			stage.addActor(line);
		}

//		Camera camera = stage.getCamera();
//		camera.update();
//		renderer.setProjectionMatrix(camera.combined);
//
//		Gdx.gl.glLineWidth(this.width);
//		renderer.begin(ShapeRenderer.ShapeType.Line);
//		renderer.line(start, end);
//		renderer.end();
//		Gdx.gl.glLineWidth(1);
	}

	@Override
	public void dispose() {
		this.disposed = true;
		if(line.getStage() != null){
			line.remove();
		}
	}

	class LineActor extends Actor {
		@Override
		public void draw(Batch batch, float parentAlpha) {
			batch.end();

			renderer.setProjectionMatrix(batch.getProjectionMatrix());
			renderer.setTransformMatrix(batch.getTransformMatrix());
			Gdx.gl.glLineWidth(width);
			renderer.begin(ShapeRenderer.ShapeType.Line);
			renderer.line(start, end);
			renderer.end();
			Gdx.gl.glLineWidth(1);

			batch.begin();
		}
	}
}
