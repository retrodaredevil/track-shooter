package me.retrodaredevil.game.trackshooter.util;

import com.badlogic.gdx.graphics.Camera;
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
}
