package me.retrodaredevil.input;

import java.util.Collection;

public abstract class ControllerInput extends ControllerPart{


	public abstract Joysticks getJoysticks();

	/**
	 *
	 * @return All parts that should be updated including all Joystick Axises and JoystickInput objects or any other
	 *         ControllerPart objects
	 */
	public abstract Collection<ControllerPart> getAllParts();

	@Override
	public void update(ControlConfig config) {
		super.update(config);
		for(ControllerPart part : getAllParts()){
			ControllerPart parent = part.getParent();
			if(parent == null || parent == this){
				part.update(config);
				part.lateUpdate();
			}
		}
	}
}
