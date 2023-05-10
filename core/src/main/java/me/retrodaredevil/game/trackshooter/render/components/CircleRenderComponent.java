package me.retrodaredevil.game.trackshooter.render.components;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;

public class CircleRenderComponent implements RenderComponent {

	private final float radius;
	private final float width;
	private final Vector2 center = new Vector2();
	private final ShapeRenderer renderer;
	private final int segments;

//	private final CircleActor circle = new CircleActor();

	public CircleRenderComponent(float radius, float width, Vector2 center, Color color, int segments){
		this.radius = radius;
		this.width = width;
		this.center.set(center);
		this.segments = segments;
		renderer = new ShapeRenderer();
		renderer.setColor(color);

	}

	@Override
	public void render(float delta, Stage stage) {
		Camera camera = stage.getCamera();
		camera.update();
		renderer.setProjectionMatrix(camera.combined);

		Gdx.gl.glLineWidth(width);
		renderer.begin(ShapeRenderer.ShapeType.Line);
		renderer.circle(center.x, center.y, radius, segments);
		renderer.end();
		Gdx.gl.glLineWidth(1);
	}

	@Override
	public void dispose() {
		renderer.dispose();
	}

//	class CircleActor extends Actor {
//		@Override
//		public void draw(Batch batch, float parentAlpha) {
//			super.draw(batch, parentAlpha);
//			renderer.begin(ShapeRenderer.ShapeType.Line);
//			renderer.setProjectionMatrix(batch.getProjectionMatrix());
//			renderer.begin(ShapeRenderer.ShapeType.Line);
//			renderer.circle(center.x, center.y, radius, segments);
//			renderer.end();
//		}
//	}
}
