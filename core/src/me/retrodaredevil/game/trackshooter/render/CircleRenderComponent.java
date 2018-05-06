package me.retrodaredevil.game.trackshooter.render;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;

public class CircleRenderComponent implements RenderComponent {

	private float radius;
	private Vector2 center;
	private final ShapeRenderer renderer;
	private Color color;

	public CircleRenderComponent(float radius, Vector2 center, Color color){
		this.radius = radius;
		this.center = center;
		renderer = new ShapeRenderer();

	}

	@Override
	public void render(float delta, Stage stage) {
		Camera camera = stage.getCamera();
		camera.update();
		renderer.setProjectionMatrix(camera.combined);
		renderer.begin(ShapeRenderer.ShapeType.Line);
//		renderer.setColor(this.color);
		renderer.circle(center.x, center.y, radius, 40);
		renderer.end();
	}

	@Override
	public void dispose() {
		renderer.dispose();
	}
}
