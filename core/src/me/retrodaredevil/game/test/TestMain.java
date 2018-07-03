package me.retrodaredevil.game.test;

import com.badlogic.gdx.Game;
import me.retrodaredevil.game.input.GdxMouseJoystick;
import me.retrodaredevil.controller.ControlConfig;
import me.retrodaredevil.controller.MouseJoystick;

public class TestMain extends Game {
	private ControlConfig config = new ControlConfig();
	private MouseJoystick joystick;
	@Override
	public void create() {
		System.out.println("here");
		joystick = new GdxMouseJoystick();
	}

	@Override
	public void render() {
		super.render();
		joystick.update(config);
		System.out.println("y: " + joystick.getY());
	}
}
