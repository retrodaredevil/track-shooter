package me.retrodaredevil.game.trackshooter.level.functions;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;

import me.retrodaredevil.game.trackshooter.entity.enemies.snake.SnakeAIController;
import me.retrodaredevil.game.trackshooter.entity.EntityDifficulty;
import me.retrodaredevil.game.trackshooter.entity.enemies.snake.SnakePart;
import me.retrodaredevil.game.trackshooter.entity.player.Player;
import me.retrodaredevil.game.trackshooter.level.Level;
import me.retrodaredevil.game.trackshooter.level.LevelEndState;
import me.retrodaredevil.game.trackshooter.level.LevelMode;
import me.retrodaredevil.game.trackshooter.world.World;

import java.util.Collection;
import java.util.List;

public class SnakeFunction implements LevelFunction {
	private static final float WAIT_TO_SPAWN = 10; // seconds
	private final Player target;

	public SnakeFunction(Player target){
		this.target = target;
	}

	@Override
	public boolean update(float delta, World world, Collection<? super LevelFunction> functionsToAdd) {
		Level level = world.getLevel();
		if(level.getMode() != LevelMode.NORMAL){
			return false;
		}
		if(level.getModeTime() < WAIT_TO_SPAWN){
			return false;
		}
		final EntityDifficulty difficulty;
		if(level.getNumber() >= 13){
			difficulty = EntityDifficulty.EXTREME;
		} else if(level.getNumber() >= 6){
			difficulty = EntityDifficulty.HARD;
		} else if(level.getNumber() >= 3){
			difficulty = EntityDifficulty.NORMAL;
		} else {
			difficulty = EntityDifficulty.EASY;
		}
		createSnake(world, difficulty);
		return true;
	}
	protected void createSnake(World world, EntityDifficulty difficulty){
		final Level level = world.getLevel();
		final Rectangle bounds = world.getBounds();

		final float x, y, rotation;
		if(MathUtils.randomBoolean()){ // up or down
			x = bounds.getX() + bounds.getWidth() / 2.0f; // center
			final boolean up = MathUtils.randomBoolean();
			y = bounds.getY() + (up ? 2 * bounds.getHeight() : -bounds.getHeight());
			rotation = up ? -90 : 90;
		} else { // left or right
			final boolean left = MathUtils.randomBoolean();
			x = bounds.getX() + (left ? -bounds.getWidth() : 2 * bounds.getWidth());
			y = bounds.getY() + bounds.getHeight() / 2.0f; // center
			rotation = left ? 0 : 180;
		}
		final int levelNumber = level.getNumber();
		final int amount;
		if(levelNumber == 2){
			amount = 15;
		} else if(levelNumber >= 22){
			amount = 35;
		} else if(levelNumber >= 14){
			amount = 30;
		} else if(levelNumber >= 8){
			amount = 25;
		} else if(levelNumber >= 3){
			amount = 20;
		} else { // level 1
			amount = 10;
		}

		final List<SnakePart> parts = SnakePart.createSnake(amount, difficulty);
		for(SnakePart part : parts){
			part.setLocation(x, y, rotation);
			level.addEntity(world, part);
			part.setEntityController(new SnakeAIController(part, target));
		}
	}

	@Override
	public void levelEnd(World world) {
	}

	@Override
	public void onModeChange(Level level, LevelMode mode, LevelMode previous) {
	}

	@Override
	public LevelEndState canLevelEnd(World world) {
		return LevelEndState.CANNOT_END; // while the snake hasn't been added, the player cannot go to the next level
	}
}
