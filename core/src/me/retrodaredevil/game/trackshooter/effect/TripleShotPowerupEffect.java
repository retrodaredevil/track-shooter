package me.retrodaredevil.game.trackshooter.effect;

import me.retrodaredevil.game.trackshooter.entity.player.Player;
import me.retrodaredevil.game.trackshooter.world.World;

public class TripleShotPowerupEffect extends TimedEffect implements PowerupActivateListenerEffect {

	private Player player;
	private boolean active = false;

	public TripleShotPowerupEffect(Player player) {
		super(10000);
		this.player = player;
	}

	@Override
	protected void onStart(World world) {
	}
	@Override
	protected void onEnd(World world) {
		player.setTriplePowerup(false);
	}
	@Override
	protected void onUpdate(float delta, World world) {
	}

	@Override
	protected boolean resetCount() {
		return !active;
	}

	@Override
	public boolean activatePowerup(World world) {
		if(active){ // already active
			return false;
		}
		active = true;
		player.setTriplePowerup(true);
		return true;
	}
}
