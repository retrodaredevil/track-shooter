package me.retrodaredevil.game.trackshooter;

public interface Controlable {
	/**
	 *
	 * @return The EntityController that is currently controlling the entity
	 */
	EntityController getEntityController();

	/**
	 *
	 * @param controller The EntityController to set to control the entity
	 */
	void setEntityController(EntityController controller);

}
