package me.retrodaredevil.controller.output;

import me.retrodaredevil.controller.ControllerPart;

public interface ControllerRumble extends ControllerPart {
	void rumble(float amount);
	void rumble(float left, float right);
}
