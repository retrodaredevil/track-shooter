package me.retrodaredevil.game.trackshooter.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;

public final class RenderUtil {
	private RenderUtil(){}

	/**
	 * Draws the stage without starting or ending the batch.
	 * @param batch
	 * @param stage
	 */
	public static void drawStage(Batch batch, Stage stage){

		Camera camera = stage.getCamera();
		camera.update();

		Group root = stage.getRoot();
		if (!root.isVisible()) return;

		batch.setProjectionMatrix(camera.combined);
		root.draw(batch, 1);
	}

	public static void clearScreen(Color backgroundColor){
		Gdx.gl.glClearColor(backgroundColor.r, backgroundColor.g, backgroundColor.b, backgroundColor.a);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
	}
}
