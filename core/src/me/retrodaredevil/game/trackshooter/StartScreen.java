package me.retrodaredevil.game.trackshooter;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;

import me.retrodaredevil.game.input.GameInput;
import me.retrodaredevil.game.trackshooter.util.Constants;

public class StartScreen extends ScreenAdapter{
	private final GameInput gameInput;
	private boolean start = false;
	public StartScreen(GameInput gameInput){
		this.gameInput = gameInput;
	}
	@Override
	public void render(float delta) {
		Color backgroundColor = Constants.BACKGROUND_COLOR;
		Gdx.gl.glClearColor(backgroundColor.r, backgroundColor.g, backgroundColor.b, backgroundColor.a);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		if(gameInput.startButton().isPressed()){
			start = true;
		}
	}

	@Override
	public void resize(int width, int height) {

	}
	public boolean isReadyToStart(){
		return start;
	}
}
