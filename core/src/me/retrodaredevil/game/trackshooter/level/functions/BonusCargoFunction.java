package me.retrodaredevil.game.trackshooter.level.functions;

import java.util.Collection;

import me.retrodaredevil.game.trackshooter.entity.Entity;
import me.retrodaredevil.game.trackshooter.entity.player.Player;
import me.retrodaredevil.game.trackshooter.level.Level;
import me.retrodaredevil.game.trackshooter.level.LevelEndState;
import me.retrodaredevil.game.trackshooter.level.LevelMode;
import me.retrodaredevil.game.trackshooter.util.EntityUtil;
import me.retrodaredevil.game.trackshooter.util.Resources;
import me.retrodaredevil.game.trackshooter.world.World;

/**
 * This allows you to make sure that the cargo entity survived the whole level. If it did, it will
 * give extra points to the player and remove the cargo entity
 */
public class BonusCargoFunction implements LevelFunction {
	private final Entity cargoEntity;
	private final Collection<? extends Player> players; // may be mutated
	private final Resources.Points points;

	private boolean failed = false;

	/**
	 *
	 * @param cargoEntity The entity that if survives the whole level, will give points to each player. This entity must be able to be removed
	 * @param players A list of players that may be mutated outside this class (NOT A COPY)
	 * @param points The points object with contains the drawable and how much it's worth
	 */
	public BonusCargoFunction(Entity cargoEntity, Collection<? extends Player> players, Resources.Points points){
		this.cargoEntity = cargoEntity;
		this.players = players;
		this.points = points;
		if(!cargoEntity.canSetToRemove()){
			throw new IllegalArgumentException("The 'cargo entity' must be able to be removed!");
		}
	}
	@Override
	public boolean update(float delta, World world, Collection<? super LevelFunction> functionsToAdd) {
		if(cargoEntity.isRemoved()){
			failed = true;
		}
		if(failed){
			return true; // we failed, end this function
		}
		if(world.getLevel().isEndingSoon()){
			cargoEntity.setToRemove();
			EntityUtil.displayScore(world, cargoEntity.getLocation(), points.getDrawable(world.getRenderObject()));
			for(Player player : players) {
				player.getScoreObject().onScore(points.getWorth());
			}
			System.out.println("Gave " + players.size() + " player(s) " + points + " points");
			return true;
		}
		return false;
	}

	@Override
	public void levelEnd(World world) {
		throw new AssertionError("This function should not be active when the level is ending!");
	}

	@Override
	public void onModeChange(Level level, LevelMode mode, LevelMode previous) {
	}

	@Override
	public LevelEndState canLevelEnd(World world) {
		return LevelEndState.CAN_END_SOON;
	}
}
