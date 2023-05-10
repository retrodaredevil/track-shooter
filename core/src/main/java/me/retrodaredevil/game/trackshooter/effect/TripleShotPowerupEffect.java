package me.retrodaredevil.game.trackshooter.effect;

import me.retrodaredevil.game.trackshooter.entity.player.Player;
import me.retrodaredevil.game.trackshooter.world.World;

public class TripleShotPowerupEffect extends TimedEffect {

	private Player player;

	public TripleShotPowerupEffect(World world, Player player) {
		super(world, 10000);
		this.player = player;
	}

	@Override
	protected void onStart() {
		player.setTriplePowerup(true);
	}
	@Override
	protected void onEnd() {
		player.setTriplePowerup(false);
	}
	@Override
	protected void onUpdate(float delta) {
	}

}
