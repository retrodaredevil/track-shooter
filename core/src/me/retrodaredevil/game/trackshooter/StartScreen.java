package me.retrodaredevil.game.trackshooter;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;

import me.retrodaredevil.game.input.GameInput;
import me.retrodaredevil.game.trackshooter.overlay.Overlay;
import me.retrodaredevil.game.trackshooter.render.RenderComponent;
import me.retrodaredevil.game.trackshooter.util.Constants;

public class StartScreen extends ScreenAdapter{
	private final GameInput gameInput;
	private final Overlay overlay;
	private boolean start = false;
	public StartScreen(GameInput gameInput, Overlay overlay){
		this.gameInput = gameInput;
		this.overlay = overlay;
	}
	@Override
	public void render(float delta) {
		Color backgroundColor = Constants.BACKGROUND_COLOR;
		Gdx.gl.glClearColor(backgroundColor.r, backgroundColor.g, backgroundColor.b, backgroundColor.a);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		if(gameInput.startButton().isPressed()){
			start = true;
		}
		RenderComponent overlayRender = overlay.getRenderComponent();
		if(overlayRender != null){
			Stage stage = overlay.getStage();
			overlayRender.render(delta, stage);

			if(Constants.SHOULD_ACT) {
				stage.act(delta);
			}
			stage.draw();
		}
	}

	@Override
	public void resize(int width, int height) {
		overlay.getStage().getViewport().update(width, height,true);
	}
	public boolean isReadyToStart(){
		return start;
	}
}
