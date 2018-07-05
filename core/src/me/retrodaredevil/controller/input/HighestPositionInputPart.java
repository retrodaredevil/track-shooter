package me.retrodaredevil.controller.input;

import java.util.Arrays;
import java.util.Collection;

import me.retrodaredevil.controller.ControlConfig;

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

			anyFull = anyFull || type.isFull();
			anyAnalog = anyAnalog || type.isAnalog();
		}
		return new AxisType(anyFull, anyAnalog, false, true);
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
		throw new IllegalStateException("This method should not be called because we override positionUpdate()");
	}

	@Override
	public double getPosition(){
		double value = 0;
		for(InputPart part : parts){
			if(!part.isConnected()){
				continue;
			}
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
	public boolean isPressed(){
		for(InputPart part : parts){
			if(!part.isConnected()){
				continue;
			}
			if(part.isPressed()){
				return true;
			}
		}

		return false;
	}

	@Override
	public boolean isConnected() {
		for(InputPart part : parts){
			if(!part.isConnected()){
				continue;
			}
			if(part.isConnected()){
				return true;
			}
		}
		return false;
	}
}
