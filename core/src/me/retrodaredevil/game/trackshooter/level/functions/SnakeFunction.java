package me.retrodaredevil.game.trackshooter.level.functions;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

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
		long time = level.getModeTime();
		if(time < 10000){
			return false;
		}
		EntityDifficulty difficulty = EntityDifficulty.EASY;

		if(level.getNumber() >= 2){
			difficulty = EntityDifficulty.NORMAL;
			if(level.getNumber() >= 6){
				difficulty = EntityDifficulty.EXTREME;
			} else if(level.getNumber() >= 4){
				difficulty = EntityDifficulty.HARD;
			}
		}
		createSnake(world, difficulty);
		return true;
	}
	protected void createSnake(World world, EntityDifficulty difficulty){
		Level level = world.getLevel();

		Rectangle bounds = world.getBounds();

		final float x, y, rotation;
		if(MathUtils.randomBoolean()){ // up or down
			x = bounds.getX() + bounds.getWidth() / 2.0f; // center
			boolean up = MathUtils.randomBoolean();
			y = bounds.getY() + (up ? 2 * bounds.getHeight() : -bounds.getHeight());
			rotation = up ? -90 : 90;
		} else { // left or right
			boolean left = MathUtils.randomBoolean();
			x = bounds.getX() + (left ? -bounds.getWidth() : 2 * bounds.getWidth());
			y = bounds.getY() + bounds.getHeight() / 2.0f; // center
			rotation = left ? 0 : 180;
		}
		int amount = 10;
		if(level.getNumber() == 2){
			amount = 15;
		} else if(level.getNumber() == 3){
			amount = 20;
		} else if(level.getNumber() >= 7){
			amount = 30;
		} else if(level.getNumber() >= 4){
			amount = 22;
		}

		List<SnakePart> parts = SnakePart.createSnake(amount, difficulty, world.getSkin());
		for(SnakePart part : parts){
			part.setLocation(x, y, rotation);
//			part.setEntityController(new SnakeAIController(part, target));
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
		return LevelEndState.CAN_END; // Snake should return false on its own if needed
	}
}
