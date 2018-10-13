package me.retrodaredevil.game.trackshooter.input.implementations;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

import me.retrodaredevil.controller.input.AutoCachingInputPart;
import me.retrodaredevil.controller.input.AxisType;
import me.retrodaredevil.controller.options.OptionValue;

/**
 * This class does not implement {@link me.retrodaredevil.controller.options.ConfigurableControllerPart} because
 * it has many use cases and the label and description may vary from use case to use case.
 */
public class GdxShakeButton extends AutoCachingInputPart {

	private final OptionValue thresholdValue;

	/**
	 *
	 * @param thresholdValue The OptionValue representing how much you have to shake it in m/s. Recommended range is [3, 16] with default of 8
	 */
	public GdxShakeButton(OptionValue thresholdValue){
		super(new AxisType(false, false), false);
		this.thresholdValue = thresholdValue;
	}


	@Override
	protected double calculatePosition() {
		float x = Gdx.input.getAccelerometerX();
		float y = Gdx.input.getAccelerometerY();
		float z = Gdx.input.getAccelerometerZ();
		float magnitude = (float) Math.sqrt(x * x + y * y + z * z);
		magnitude -= 9.8f;
		// magnitude may be less than 0
		return (magnitude >= thresholdValue.getOptionValue()) ? 1 : 0;
	}

	@Override
	public boolean isConnected() {
		return Gdx.input.isPeripheralAvailable(Input.Peripheral.Accelerometer);
	}

}
