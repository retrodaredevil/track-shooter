package me.retrodaredevil.controller;

import java.util.Collection;

public abstract class ControllerInput extends ControllerPart{


	public abstract ControllerExtras getExtras();

	/**
	 * NOTE: This may not include all ControllerParts that are accessible from this object since some may not
	 * be handled by this.
	 * @return All parts that should be updated including all Joystick Axises and JoystickPart objects or any other
	 *         ControllerPart objects
	 */
	public abstract Collection<ControllerPart> getAllParts();

	@Override
	public void update(ControlConfig config) {
		super.update(config);
		for(ControllerPart part : getAllParts()){
			ControllerPart parent = part.getParent();
			assert parent != null : part.toString() + " doesn't have a parent.";
			if(parent == this){
				part.update(config);
				part.lateUpdate();
			}
		}
	}
}
