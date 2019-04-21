package me.retrodaredevil.game.trackshooter.level;

import me.retrodaredevil.game.trackshooter.world.World;

public interface LevelGetter {
	/**
	 * Returns a new Level each time it is called
	 * <p>
	 * <p>
	 * When this is first called, the returned Level's number (using getNumber()) will return 1 and when calling this
	 * again, it will be 2.
	 * @return The next level
	 */
	Level nextLevel(World world);
}
