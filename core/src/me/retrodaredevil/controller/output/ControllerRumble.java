package me.retrodaredevil.controller.output;

import me.retrodaredevil.controller.ControllerManager;
import me.retrodaredevil.controller.ControllerPart;

public abstract class ControllerRumble extends ControllerPart {
	public abstract void rumble(float amount);
	public abstract void rumble(float left, float right);
}
