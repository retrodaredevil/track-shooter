package me.retrodaredevil.game.trackshooter.level;

import me.retrodaredevil.game.trackshooter.world.World;

import java.util.Collection;

/**
 * A useful interface utilized by SimpleLevel and that can be utilized elsewhere if needed
 */
public interface LevelFunction {

	/**
	 *
	 * @param functionsToAdd A Collection that if appended to will add all elements to the level's LevelFunction list
	 *                       and each of those elements will probably be called the same frame as this function.
	 * @return true if this LevelFunction done and should not be called again, false for this to keep getting called
	 */
	boolean update(float delta, World world, Collection<? extends LevelFunction> functionsToAdd);
}
