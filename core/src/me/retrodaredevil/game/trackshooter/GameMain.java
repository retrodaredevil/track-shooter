package me.retrodaredevil.game.trackshooter;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;

public class GameMain extends Game {


	@Override
	public void create () {
		Gdx.app.setLogLevel(Application.LOG_DEBUG);
		setScreen(new GameScreen());
	}

	@Override
	public void render() {
		super.render();
		Gdx.graphics.setTitle("Track Shooter - FPS:" + Gdx.graphics.getFramesPerSecond());
	}
}
