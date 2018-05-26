package me.retrodaredevil.game.input;

import com.badlogic.gdx.Gdx;
import me.retrodaredevil.controller.ControllerManager;
import me.retrodaredevil.controller.InputPart;

public class KeyInputPart extends InputPart {
	private int code;
	private boolean isButton;

	public KeyInputPart(int code, boolean isButton) {
		super(AxisType.DIGITAL);
		this.code = code;
		this.isButton = isButton;
	}
	public KeyInputPart(int code){
		this(code, false);
	}

	@Override
	protected double calculatePosition() {
		if(isButton){
			return Gdx.input.isButtonPressed(code) ? 1 : 0;
		}
		return Gdx.input.isKeyPressed(code) ? 1 : 0;
	}

	@Override
	public boolean isConnected(ControllerManager manager) {
		return true;
	}
}
