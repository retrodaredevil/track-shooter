package me.retrodaredevil.game.trackshooter.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;

public final class RenderUtil {
	private RenderUtil(){}

	/**
	 * Draws the stage without starting or ending the batch. Also applies the stage's viewport.
	 * <p>
	 * This expects that {@link Batch#begin()} has already been called. Calling this method will
	 * NOT flush the batch
	 * @param batch The batch
	 * @param stage The stage to draw
	 */
	public static void drawStage(Batch batch, Stage stage){
		if(batch == null){
			throw new NullPointerException("batch cannot be null!");
		}
		if(stage == null){
			throw new NullPointerException("Stage cannot be null!");
		}

		stage.getViewport().apply();
//		camera.update(); apply updates camera

		Group root = stage.getRoot();
		if (!root.isVisible()) return;

		batch.setProjectionMatrix(stage.getCamera().combined);
		root.draw(batch, 1);
	}

	public static void clearScreen(Color backgroundColor){
		Gdx.gl.glClearColor(backgroundColor.r, backgroundColor.g, backgroundColor.b, backgroundColor.a);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
	}
}
