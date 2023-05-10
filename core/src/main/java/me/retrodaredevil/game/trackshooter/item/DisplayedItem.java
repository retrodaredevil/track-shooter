package me.retrodaredevil.game.trackshooter.item;

import com.badlogic.gdx.scenes.scene2d.ui.Image;

public interface DisplayedItem extends Item {
	/**
	 * NOTE: The returned image should not be handled by this item instance so the caller is usually
	 * expected to add the image or a Table or change its position. Doing this will not affect the
	 * item in any way.
	 * <p>
	 * Of course, if there are multiple callers that want to do different things with the image,
	 * that will cause bugs so it is only expected that one caller uses the image at a time. There
	 * is no way to enforce this but it will become quite obvious when the image doesn't go where you
	 * want it.
	 * @return The image of the item for display purposes
	 */
	Image getImage();
}
