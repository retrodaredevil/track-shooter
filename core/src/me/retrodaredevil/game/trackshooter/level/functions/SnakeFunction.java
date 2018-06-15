package me.retrodaredevil.game.trackshooter.level.functions;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
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
		createSnake(world);
		return true;
	}
	protected void createSnake(World world){
		Level level = world.getLevel();

		Rectangle bounds = world.getBounds();

		final float x, y, rotation;
		if(MathUtils.randomBoolean()){ // up or down
			x = bounds.getX() + bounds.getWidth() / 2.0f; // center
			boolean up = MathUtils.randomBoolean();
			y = bounds.getY() + (up ? 2 * bounds.getHeight() : 0);
			rotation = up ? -90 : 90;
		} else { // left or right
			boolean left = MathUtils.randomBoolean();
			x = bounds.getX() + (left ? 0 : 2 * bounds.getWidth());
			y = bounds.getY() + bounds.getHeight() / 2.0f; // center
			rotation = left ? 0 : 180;
		}

		List<SnakePart> parts = SnakePart.createSnake(22);
		for(SnakePart part : parts){
			part.setLocation(x, y, rotation);
//			part.setEntityController(new SnakeAIController(part, target));
			level.addEntity(world, part);
		}
		SnakePart head = parts.get(0);
		head.setEntityController(new SnakeAIController(head, target));
	}

	@Override
	public void levelEnd(World world) {
	}

	@Override
	public void onModeChange(Level level, LevelMode mode, LevelMode previous) {
	}

	@Override
	public boolean canLevelEnd(World world) {
		return true; // Snake should return false on its own if needed
	}
}
