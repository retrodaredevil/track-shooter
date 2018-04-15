package me.retrodaredevil.game.trackshooter.render;


import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Disposable;

public interface RenderComponent extends Disposable {

	void render(float delta, Stage stage);
}
