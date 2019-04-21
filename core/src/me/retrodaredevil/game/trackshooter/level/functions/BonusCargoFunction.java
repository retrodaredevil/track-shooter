package me.retrodaredevil.game.trackshooter.level.functions;

import java.util.Collection;

import me.retrodaredevil.game.trackshooter.entity.Entity;
import me.retrodaredevil.game.trackshooter.entity.movement.MoveComponent;
import me.retrodaredevil.game.trackshooter.entity.player.Player;
import me.retrodaredevil.game.trackshooter.level.Level;
import me.retrodaredevil.game.trackshooter.level.LevelEndState;
import me.retrodaredevil.game.trackshooter.level.LevelMode;
import me.retrodaredevil.game.trackshooter.util.EntityUtil;
import me.retrodaredevil.game.trackshooter.util.Points;
import me.retrodaredevil.game.trackshooter.world.World;

/**
 * This allows you to make sure that the cargo entity survived the whole level. If it did, it will
 * give extra points to the player and remove the cargo entity
 */
public class BonusCargoFunction implements LevelFunction {
	private final World world;
	private final Entity cargoEntity;
	private final Collection<? extends Player> players; // may be mutated
	private final Points points;

	private boolean showedHelp = false;

	/**
	 *
	 * @param cargoEntity The entity that if survives the whole level, will give points to each player.
	 *                    This entity must be able to be removed. This entity should also already be added
	 *                    to the world as this function will not add it to the level or world.<p>
	 *                    It is also expected that the MoveComponent on this cargo entity is not null and that calling
	 *                    {@link MoveComponent#getCorrectLocation()} does not return null
	 * @param players A list of players that may be mutated outside this class (NOT A COPY).
	 *                These players will receive points at the end of the level.
	 * @param points The points object with contains the drawable and how much it's worth
	 */
	public BonusCargoFunction(World world, Entity cargoEntity, Collection<? extends Player> players, Points points){
		this.world = world;
		this.cargoEntity = cargoEntity;
		this.players = players;
		this.points = points;
		if(!cargoEntity.canSetToRemove()){
			throw new IllegalArgumentException("The 'cargo entity' must be able to be removed!");
		}
	}
	@Override
	public boolean update(float delta, Collection<? super LevelFunction> functionsToAdd) {
		if(cargoEntity.isRemoved()){
			return true; // we failed, end this function
		}
		if(!showedHelp){
			EntityUtil.displayScore(world, cargoEntity.getMoveComponent().getCorrectLocation(),
					world.getRenderObject().getMainSkin().getDrawable("help"), 1.2f, 3.0f);
			showedHelp = true;
		}

		if(world.getLevel().isEndingSoon()){
			cargoEntity.setToRemove();
			EntityUtil.displayScore(world, cargoEntity.getLocation(), points.getDrawable(world.getRenderObject()));
			for(Player player : players) {
				player.getScoreObject().onScore(points.getWorth());
			}
//			System.out.println("Gave " + players.size() + " player(s) " + points + " points");
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
