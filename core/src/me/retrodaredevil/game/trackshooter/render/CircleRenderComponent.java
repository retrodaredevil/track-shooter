package me.retrodaredevil.game.trackshooter.render;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;

public class CircleRenderComponent implements RenderComponent {

	private final float radius;
	private final Vector2 center = new Vector2();
	private final ShapeRenderer renderer;
	private final int segments;

	public CircleRenderComponent(float radius, Vector2 center, Color color, int segments){
		this.radius = radius;
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
		renderer.begin(ShapeRenderer.ShapeType.Line);
//		renderer.setColor(this.color);
		renderer.circle(center.x, center.y, radius, segments);
		renderer.end();
	}

	@Override
	public void dispose() {
		renderer.dispose();
	}
}
