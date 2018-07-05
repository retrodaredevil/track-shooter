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
	protected abstract Collection<ControllerPart> getPartsToUpdate();

	@Override
	public void update(ControlConfig config) {
		super.update(config); // this super method just sets this.config to config so it's okay to call
		for(ControllerPart part : getPartsToUpdate()){
			ControllerPart parent = part.getParent();
			if(parent == null){
				throw new IllegalStateException("One of the parts from getPartsToUpdate() doesn't have a parent");
			}
			if(parent != this){
				throw new IllegalStateException("One of our children doesn't claim us as a parent. child: " + part);
			}
			part.update(config);
			part.lateUpdate();
		}
	}
}
