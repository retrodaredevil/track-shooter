package me.retrodaredevil.game.trackshooter.input;

import com.badlogic.gdx.Input;

import me.retrodaredevil.controller.input.SimpleJoystickPart;
import me.retrodaredevil.controller.input.TwoAxisJoystickPart;
import me.retrodaredevil.game.trackshooter.input.implementations.KeyInputPart;

public final class FourKeyJoystick {
	private FourKeyJoystick(){}

	public static SimpleJoystickPart createFromKeys(int up, int down, int left, int right){
		return TwoAxisJoystickPart.createFromFour(new KeyInputPart(up), new KeyInputPart(down),
				new KeyInputPart(left), new KeyInputPart(right));
	}

	public static SimpleJoystickPart newArrowKeyJoystick(){
//		return new FourKeyJoystick(new Key)
		return createFromKeys(Input.Keys.UP, Input.Keys.DOWN, Input.Keys.LEFT, Input.Keys.RIGHT);
	}

	public static SimpleJoystickPart newWASDJoystick(){
		return createFromKeys(Input.Keys.W, Input.Keys.S, Input.Keys.A, Input.Keys.D);
	}
}
