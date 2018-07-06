package me.retrodaredevil.controller.input;

import java.util.Arrays;
import java.util.List;

import me.retrodaredevil.controller.ControllerPart;

/**
 * This class can be used if you want to map two buttons to the same control.
 */
public class HighestPositionInputPart extends SimpleInputPart {

	private final List<InputPart> parts;

	/**
	 *
	 * @param parts The controller parts to use. If a part's parent is null, it will automatically be handled by this, otherwise,
	 *              each part that already has a parent must update its position
	 */
	public HighestPositionInputPart(InputPart... parts){
		super(autoAxisTypeHelper(parts));
		this.parts = Arrays.asList(parts);
		addChildren(this.parts, false, true);
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
	public boolean isDown() {
		for(InputPart part : parts){
			if(!part.isConnected()){
				continue;
			}
			if(part.isDown()){
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean isPressed() {
		boolean onePressed = false;
		for(InputPart part : parts){
			boolean pressed = part.isPressed();
			if(part.isDown() && !pressed){
				return false;
			}
			onePressed = onePressed || pressed;
		}
		return onePressed;
	}

	@Override
	public boolean isReleased() {
		boolean oneReleased = false;
		for(InputPart part : parts){
			if(part.isDown()){
				return false;
			}
			oneReleased = oneReleased || part.isReleased();
		}
		return oneReleased;
	}

	@Override
	public boolean isConnected() {
		for(InputPart part : parts){
			if(part.isConnected()){
				return true;
			}
		}
		return false;
	}
}
