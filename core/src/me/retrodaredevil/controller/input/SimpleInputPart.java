package me.retrodaredevil.controller.input;

import me.retrodaredevil.controller.ControlConfig;
import me.retrodaredevil.controller.SimpleControllerPart;

public abstract class SimpleInputPart extends SimpleControllerPart implements InputPart{
	private final AxisType type;

	public SimpleInputPart(AxisType type){
		this.type = type;
	}

	@Override
	public AxisType getAxisType() {
		return type;
	}

	/**
	 * @return true if the current getValue() is in a deadzone or close enough to 0
	 */
	@Override
	public boolean isDeadzone() {
		return Math.abs(getPosition()) <= getDeadzone(getAxisType(), config);
	}
	public static double getDeadzone(AxisType type, ControlConfig config){
		if(type.isFull()){
			if(type.isAnalog()){
				return config.fullAnalogDeadzone;
			}
			return config.fullDigitalDeadzone;
		} else if(type.isAnalog()){ // !isFull()
			return config.analogDeadzone;
		}
		return config.digitalDeadzone;
	}

	@Override
	public int getDigitalPosition() {
		double value = getPosition();
		if(!getAxisType().isFull()){
			if(value > config.buttonDownDeadzone){
				return 1;
			}
		} else {
			if (value > config.analogDigitalValueDeadzone) {
				return 1;
			} else if (value < -config.analogDigitalValueDeadzone) {
				return -1;
			}
		}

		return 0;
	}

}
