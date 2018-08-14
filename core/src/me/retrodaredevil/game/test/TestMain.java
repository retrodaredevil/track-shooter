package me.retrodaredevil.game.test;

import com.badlogic.gdx.Game;
import me.retrodaredevil.controller.ControlConfig;
import me.retrodaredevil.controller.input.LargeRangeJoystick;

@Deprecated
public class TestMain extends Game {
	@Override
	public void create() {
		System.out.println("here");
	}

	@Override
	public void render() {
		super.render();
//		joystick.update(config);
//		System.out.println("y: " + joystick.getY());
	}
}
