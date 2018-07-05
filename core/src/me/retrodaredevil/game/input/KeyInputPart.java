package me.retrodaredevil.game.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

import me.retrodaredevil.controller.input.InputPart;

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
	public boolean isConnected() {
		return isButton || Gdx.input.isPeripheralAvailable(Input.Peripheral.HardwareKeyboard);
	}
}
