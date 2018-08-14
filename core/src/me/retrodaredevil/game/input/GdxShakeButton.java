package me.retrodaredevil.game.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

import me.retrodaredevil.controller.input.AutoCachingInputPart;
import me.retrodaredevil.controller.input.AxisType;

public class GdxShakeButton extends AutoCachingInputPart {
	public GdxShakeButton() {
		super(new AxisType(false, false), false);
	}

	@Override
	protected double calculatePosition() {
		float x = Gdx.input.getAccelerometerX();
		float y = Gdx.input.getAccelerometerY();
		float z = Gdx.input.getAccelerometerZ();
		float magnitude = (float) Math.sqrt(x * x + y * y + z * z);
		magnitude -= 9.8f;
		// magnitude may be less than 0
//		System.out.println("magnitude: " + magnitude);
		return magnitude >= 8 ? 1 : 0;
	}

	@Override
	public boolean isConnected() {
		return Gdx.input.isPeripheralAvailable(Input.Peripheral.Accelerometer);
	}
}
