package me.retrodaredevil.game.trackshooter.input.implementations;

import com.badlogic.gdx.Gdx;
import me.retrodaredevil.controller.input.AxisType;
import me.retrodaredevil.controller.input.implementations.AutoCachingInputPart;

public class KeyInputPart extends AutoCachingInputPart {
	private final int code;
	private final boolean isButton;

	public KeyInputPart(int code, boolean isButton) {
		super(new AxisType(false, false));
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
		return true;
	}
}
