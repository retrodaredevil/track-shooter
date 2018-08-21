package me.retrodaredevil.game.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

import me.retrodaredevil.controller.input.AutoCachingInputPart;
import me.retrodaredevil.controller.ThresholdControllerPart;
import me.retrodaredevil.controller.input.AxisType;

public class GdxShakeButton extends AutoCachingInputPart implements ThresholdControllerPart{

	private double threshold;

	public GdxShakeButton(Double threshold) {
		super(new AxisType(false, false), false);
		if(threshold != null){
			setThreshold(threshold);
		} else {
			setToDefaultThreshold();
		}
	}
	public GdxShakeButton(double threshold){
		this((Double) threshold);
	}
	public GdxShakeButton(){
		this(null);
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
		return (magnitude >= threshold) ? 1 : 0;
	}

	@Override
	public boolean isConnected() {
		return Gdx.input.isPeripheralAvailable(Input.Peripheral.Accelerometer);
	}

	@Override
	public double getMaxThreshold() {
		return 16;
	}

	@Override
	public double getMinThreshold() {
		return 3;
	}

	@Override
	public double getThreshold() {
		return threshold;
	}

	@Override
	public void setThreshold(double threshold) {
		this.threshold = threshold;
	}

	@Override
	public double getDefaultThreshold() {
		return 8;
	}

	@Override
	public void setToDefaultThreshold() {
		this.threshold = 8;
	}
}
