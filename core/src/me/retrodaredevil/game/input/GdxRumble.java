package me.retrodaredevil.game.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

import me.retrodaredevil.controller.SimpleControllerPart;
import me.retrodaredevil.controller.output.ControllerRumble;

public class GdxRumble extends SimpleControllerPart implements ControllerRumble {
	@Override
	public void rumble(float amount) {
		if(amount == 0){
			Gdx.input.cancelVibrate();
		} else {
			Gdx.input.vibrate((int) (amount * 200f));
		}
	}

	@Override
	public void rumble(float left, float right) {
		this.rumble((left + right) / 2.0f);
	}

	@Override
	public boolean isConnected() {
		return Gdx.input.isPeripheralAvailable(Input.Peripheral.Vibrator);
	}
}
