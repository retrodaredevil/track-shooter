package me.retrodaredevil.controller.types;

import me.retrodaredevil.controller.output.ControllerRumble;

public interface RumbleCapableController {
	/**
	 *
	 * @return null or ControllerRumble
	 */
	ControllerRumble getRumble();
}
