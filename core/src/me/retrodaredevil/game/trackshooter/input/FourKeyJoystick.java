package me.retrodaredevil.game.trackshooter.input;

import com.badlogic.gdx.Input;

import me.retrodaredevil.controller.gdx.KeyInputPart;
import me.retrodaredevil.controller.input.JoystickPart;
import me.retrodaredevil.controller.input.implementations.TwoAxisJoystickPart;

public final class FourKeyJoystick {
	private FourKeyJoystick(){}

	public static JoystickPart createFromKeys(int up, int down, int left, int right){
		return TwoAxisJoystickPart.createFromFour(new KeyInputPart(up), new KeyInputPart(down),
				new KeyInputPart(left), new KeyInputPart(right));
	}

	public static JoystickPart newArrowKeyJoystick(){
		return createFromKeys(Input.Keys.UP, Input.Keys.DOWN, Input.Keys.LEFT, Input.Keys.RIGHT);
	}

	public static JoystickPart newWASDJoystick(){
		return createFromKeys(Input.Keys.W, Input.Keys.S, Input.Keys.A, Input.Keys.D);
	}
}
