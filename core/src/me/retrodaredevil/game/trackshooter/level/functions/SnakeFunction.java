package me.retrodaredevil.game.trackshooter.level.functions;

import me.retrodaredevil.game.trackshooter.entity.enemies.SnakeAIController;
import me.retrodaredevil.game.trackshooter.entity.enemies.SnakePart;
import me.retrodaredevil.game.trackshooter.entity.player.Player;
import me.retrodaredevil.game.trackshooter.level.Level;
import me.retrodaredevil.game.trackshooter.level.LevelMode;
import me.retrodaredevil.game.trackshooter.world.World;

import java.util.Collection;
import java.util.List;

public class SnakeFunction implements LevelFunction {
	private Player target;
	private SnakePart head = null;

	public SnakeFunction(Player target){
		this.target = target;
	}

	@Override
	public boolean update(float delta, World world, Collection<? super LevelFunction> functionsToAdd) {
		Level level = world.getLevel();
		if(level.getMode() != LevelMode.NORMAL){
			return false;
		}
		long time = level.getModeTime();
		if(time < 10000){
			return false;
		}
		List<SnakePart> parts = SnakePart.createSnake(22);
		for(SnakePart part : parts){
			part.setEntityController(new SnakeAIController(part, target));
			level.addEntity(world, part);
		}
		return true;
	}

	@Override
	public void levelEnd(World world) {
	}

	@Override
	public void onModeChange(Level level, LevelMode mode, LevelMode previous) {
	}

	@Override
	public boolean canLevelEnd(World world) {
		return head != null && head.isRemoved();
	}
}
