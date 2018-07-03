package me.retrodaredevil.game.trackshooter.effect;

import me.retrodaredevil.game.trackshooter.entity.player.Player;
import me.retrodaredevil.game.trackshooter.world.World;

public class TripleShotPowerupEffect extends TimedEffect {

	private Player player;

	public TripleShotPowerupEffect(Player player) {
		super(10000);
		this.player = player;
	}

	@Override
	protected void onStart(World world) {
		player.setTriplePowerup(true);
	}
	@Override
	protected void onEnd(World world) {
		player.setTriplePowerup(false);
	}
	@Override
	protected void onUpdate(float delta, World world) {
	}

}
