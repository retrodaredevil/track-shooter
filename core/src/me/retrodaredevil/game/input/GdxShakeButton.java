package me.retrodaredevil.game.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

import me.retrodaredevil.controller.input.AutoCachingInputPart;
import me.retrodaredevil.controller.input.AxisType;
import me.retrodaredevil.controller.options.OptionValueObject;

import static java.lang.Math.sqrt;

/**
 * This class does not implement {@link me.retrodaredevil.controller.options.ConfigurableControllerPart} because
 * it has many use cases and the label and description may vary from use case to use case.
 */
public class GdxShakeButton extends AutoCachingInputPart implements OptionValueObject{

	private int threshold;

	public GdxShakeButton(Integer threshold) {
		super(new AxisType(false, false), false);
		if(threshold != null){
			setOptionValue(threshold);
		} else {
			setToDefaultOptionValue();
		}
	}
	public GdxShakeButton(int threshold){
		this((Integer) threshold);
	}
	public GdxShakeButton(){
		this(null);
	}


	@Override
	protected double calculatePosition() {
		float x = Gdx.input.getAccelerometerX();
		float y = Gdx.input.getAccelerometerY();
		float z = Gdx.input.getAccelerometerZ();
//		float magnitude = (float) Math.sqrt(x * x + y * y + z * z);
		double magnitude = sqrt(sqrt(x * x + y * y) + z * z);
		magnitude -= 9.8f;
		// magnitude may be less than 0
//		System.out.println("magnitude: " + magnitude);
		return (magnitude >= threshold) ? 1 : 0;
	}

	@Override
	public boolean isConnected() {
		return Gdx.input.isPeripheralAvailable(Input.Peripheral.Accelerometer);
	}

	@Override
	public double getMaxOptionValue() {
		return 16;
	}

	@Override
	public double getMinOptionValue() {
		return 3;
	}

	@Override
	public boolean isOptionAnalog() {
		return false;
	}

	@Override
	public double getOptionValue() {
		return threshold;
	}

	@Override
	public void setOptionValue(double threshold) {
		this.threshold = (int) threshold;
	}

	@Override
	public double getDefaultOptionValue() {
		return 8;
	}

	@Override
	public void setToDefaultOptionValue() {
		this.threshold = 8;
	}
}
