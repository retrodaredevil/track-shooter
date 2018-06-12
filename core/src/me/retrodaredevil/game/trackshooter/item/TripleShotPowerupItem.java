package me.retrodaredevil.game.trackshooter.item;

import me.retrodaredevil.game.trackshooter.effect.TripleShotPowerupEffect;
import me.retrodaredevil.game.trackshooter.entity.player.Player;
import me.retrodaredevil.game.trackshooter.world.World;

public class TripleShotPowerupItem implements PowerupActivateListenerItem {

	private boolean used = false;

	@Override
	public boolean activatePowerup(World world, Player player) {
		if(used){
			throw new IllegalStateException("Item should have been removed.");
		}
		if(player.getEffects(TripleShotPowerupEffect.class) != null){
			return false; // there's already this powerup active
		}
		used = true;
		player.addEffect(new TripleShotPowerupEffect(player));
		return true;
	}

	@Override
	public boolean isUsed() {
		return used;
	}

	@Override
	public void update(float delta, World world) {
	}
}
