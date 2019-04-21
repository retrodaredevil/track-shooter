package me.retrodaredevil.game.trackshooter.item;

import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import me.retrodaredevil.game.trackshooter.effect.Effect;
import me.retrodaredevil.game.trackshooter.effect.TripleShotPowerupEffect;
import me.retrodaredevil.game.trackshooter.entity.Entity;
import me.retrodaredevil.game.trackshooter.entity.player.Player;
import me.retrodaredevil.game.trackshooter.util.Resources;
import me.retrodaredevil.game.trackshooter.world.World;

public class TripleShotPowerupItem extends EffectItem implements PowerupActivateListenerItem {

	private final World world;

	public TripleShotPowerupItem(World world){
		super(new Image(world.getMainSkin().getDrawable("powerup")));
		this.world = world;
	}

	@Override
	public boolean activatePowerup(Player player) {
		return activateEffect(player);
	}

	@Override
	protected Effect createEffect(Entity entity) {
		Player player = (Player) entity;
		if(player.getEffects(TripleShotPowerupEffect.class) != null){
			return null; // there's already this powerup active
		}
		return new TripleShotPowerupEffect(world, player);
	}


	@Override
	public void update(float delta) {
	}
}
