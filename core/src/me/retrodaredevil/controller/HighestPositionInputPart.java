package me.retrodaredevil.controller;

import java.util.Arrays;
import java.util.Collection;

public class HighestPositionInputPart extends InputPart {

	private final Collection<InputPart> parts;

	/**
	 *
	 * @param parts The controller parts to use. If a part's parent is null, it will automatically be handled by this, otherwise,
	 *              each part that already has a parent must update its position
	 */
	public HighestPositionInputPart(InputPart... parts){
		super(autoAxisTypeHelper(parts));
		this.parts = Arrays.asList(parts);
		setParentsToThis(this.parts, false, true);
	}
	private static AxisType autoAxisTypeHelper(InputPart... parts){
		boolean anyFull = false;
		boolean anyAnalog = false;
		for(InputPart part : parts){
			AxisType type = part.getAxisType();
			if(type == AxisType.FULL_DIGITAL){
				anyFull = true;
			} else if(type == AxisType.FULL_ANALOG){
				anyFull = true;
				anyAnalog = true;
			} else if(type == AxisType.ANALOG){
				anyAnalog = true;
			}
		}

		if(anyFull){
			if(anyAnalog){
				return AxisType.FULL_ANALOG;
			} else {
				return AxisType.FULL_DIGITAL;
			}
		}
		if(anyAnalog){
			return AxisType.ANALOG;
		}

		return AxisType.DIGITAL;
	}

	@Override
	public void update(ControlConfig config) {
		super.update(config);
		for(InputPart part : parts){
			part.update(config);
		}
	}

	@Override
	protected void positionUpdate() {
	}

	@Override
	protected double calculatePosition() {
		throw new IllegalStateException("This method should not be called because we overrid positionUpdate()");
	}

	@Override
	public double getPosition() {
		double value = 0;
		for(InputPart part : parts){
			double position = part.getPosition();
			if(Math.abs(position) > Math.abs(value)){
				value = position;
			} else if(position == -value){
				value = 0;
			}
		}
		return value;
	}

	@Override
	public boolean isPressed() {
		for(InputPart part : parts){
			if(part.isPressed()){
				return true;
			}
		}

		return false;
	}

	@Override
	public boolean isConnected(ControllerManager manager) {
		return false;
	}
}
