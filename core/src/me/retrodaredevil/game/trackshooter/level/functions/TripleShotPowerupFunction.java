package me.retrodaredevil.game.trackshooter.level.functions;

import me.retrodaredevil.game.trackshooter.entity.powerup.PowerupEntity;
import me.retrodaredevil.game.trackshooter.entity.powerup.SimplePowerup;
import me.retrodaredevil.game.trackshooter.entity.powerup.SimpleItemPowerupEntity;
import me.retrodaredevil.game.trackshooter.world.World;

public class TripleShotPowerupFunction extends PowerupFunction {
	public TripleShotPowerupFunction() {
		super(20000, 8000);
	}

	@Override
	protected PowerupEntity createPowerup(World world) {
		return SimpleItemPowerupEntity.createTripleShotPowerupEntity(2.0f, SimplePowerup.getRandomTrackStarting(world));
	}
}
