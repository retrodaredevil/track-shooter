package me.retrodaredevil.game.trackshooter.level;

import me.retrodaredevil.game.trackshooter.world.World;

public interface CanLevelEnd {

	/**
	 * NOTE: Usually, this is only called when this object is in a list that checks for CanLevelEnd. This only works
	 * if the list this object is in is acted upon correctly.
	 * <p><p>
	 * Called only when the level may possibly end
	 * @param world The World object
	 * @return true if the level is allowed to end
	 */
	boolean canLevelEnd(World world);
}
