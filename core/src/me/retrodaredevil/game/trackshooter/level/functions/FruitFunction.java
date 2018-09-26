package me.retrodaredevil.game.trackshooter.level.functions;

import com.badlogic.gdx.scenes.scene2d.ui.Image;
import me.retrodaredevil.game.trackshooter.entity.powerup.Fruit;
import me.retrodaredevil.game.trackshooter.entity.powerup.PowerupEntity;
import me.retrodaredevil.game.trackshooter.entity.powerup.SimplePowerup;
import me.retrodaredevil.game.trackshooter.util.Resources;
import me.retrodaredevil.game.trackshooter.world.World;

public class FruitFunction extends PowerupFunction {
	public FruitFunction() {
		super(15000, 10000);
	}

	@Override
	protected PowerupEntity createPowerup(World world) {
		int level = world.getLevel().getNumber();
		Resources.Points points = Resources.Points.P100;
		String name = "cherry";
		if(level >= 15){
			points = Resources.Points.P1000;
			name = "pizza";
		} else if (level >= 10){
			points = Resources.Points.P700;
			name = "grapes";
		} else if (level >= 6){
			points = Resources.Points.P500;
			name = "strawberry";
		} else if(level >= 3){
			points = Resources.Points.P300;
			name = "apple";
		}
		return Fruit.createFruit(points, SimplePowerup.getRandomTrackStarting(world), new Image(world.getMainSkin().getDrawable(name)));
	}
}
