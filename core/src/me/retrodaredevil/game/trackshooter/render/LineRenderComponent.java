package me.retrodaredevil.game.trackshooter.render;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;

public class LineRenderComponent implements RenderComponent {
	private Vector2 start;
	private Vector2 end;

	private Color color;
	private float width;
	private ShapeRenderer renderer;

	/**
	 * Note when dispose() is called, it will NOT call renderer.dispose().
	 */
	public LineRenderComponent(Vector2 start, Vector2 end, Color color, float width, ShapeRenderer renderer){
		this.start = start;
		this.end = end;
		this.color = color;
		this.width = width;
		this.renderer = renderer;
	}
	@Override
	public void render(float delta, Stage stage) {
		Gdx.gl.glLineWidth(this.width);

		Camera camera = stage.getCamera();
		camera.update();
		renderer.setProjectionMatrix(camera.combined);
//		renderer.setTransformMatrix();
		renderer.begin(ShapeRenderer.ShapeType.Line);
		renderer.setColor(this.color);
		renderer.line(start, end);
		renderer.end();
		Gdx.gl.glLineWidth(1);
	}

	@Override
	public void dispose() {

	}
}
