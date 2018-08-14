package me.retrodaredevil.controller;

public interface ControllerManager {
	/**
	 *
	 * @param controller The controller to add. controller.getParent() should be null
	 * @return true if the passed controller wasn't already added, false otherwise
	 */
	boolean addController(ControllerInput controller);

	/**
	 *
	 * @param controller The controller to remove
	 * @return true if the passed controller was removed, false if it wasn't added to begin with
	 */
	boolean removeController(ControllerInput controller);

	void update();
}
