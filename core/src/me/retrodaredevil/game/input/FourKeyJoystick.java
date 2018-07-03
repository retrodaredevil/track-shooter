package me.retrodaredevil.game.input;

import com.badlogic.gdx.Input;
import me.retrodaredevil.controller.ButtonDPad;
import me.retrodaredevil.controller.ControlConfig;
import me.retrodaredevil.controller.ControllerManager;
import me.retrodaredevil.controller.InputPart;

public class FourKeyJoystick extends ButtonDPad {

	private final InputPart up;
	private final InputPart down;
	private final InputPart left;
	private final InputPart right;


	public FourKeyJoystick(int up, int down, int left, int right){
		this(
				new KeyInputPart(up),
				new KeyInputPart(down),
				new KeyInputPart(left),
				new KeyInputPart(right)
		);
	}
	public FourKeyJoystick(InputPart up, InputPart down, InputPart left, InputPart right){
		this.up = up;
		this.down = down;
		this.left = left;
		this.right = right;

		up   .setParent(this);
		down .setParent(this);
		left .setParent(this);
		right.setParent(this);
	}
	public static FourKeyJoystick newArrowKeyJoystick(){
//		return new FourKeyJoystick(new Key)
		return new FourKeyJoystick(Input.Keys.UP, Input.Keys.DOWN, Input.Keys.LEFT, Input.Keys.RIGHT);
	}

	public static FourKeyJoystick newWASDJoystick(){
		return new FourKeyJoystick(Input.Keys.W, Input.Keys.S, Input.Keys.A, Input.Keys.D);
	}

	@Override
	public void update(ControlConfig config) {
		super.update(config);
		up.update(config);
		down.update(config);
		left.update(config);
		right.update(config);
	}

	@Override
	public InputPart up() {
		return up;
	}

	@Override
	public InputPart down() {
		return down;
	}

	@Override
	public InputPart left() {
		return left;
	}

	@Override
	public InputPart right() {
		return right;
	}

	@Override
	public boolean isConnected(ControllerManager manager) {
		return up.isConnected(manager) && down.isConnected(manager) && left.isConnected(manager) && right.isConnected(manager);
	}
}
