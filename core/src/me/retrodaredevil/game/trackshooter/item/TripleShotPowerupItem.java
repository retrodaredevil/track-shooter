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

	public TripleShotPowerupItem(Skin skin){
		super(new Image(skin.getDrawable("powerup")));
	}

	@Override
	public boolean activatePowerup(World world, Player player) {
		return activateEffect(player);
	}

	@Override
	protected Effect createEffect(Entity entity) {
		Player player = (Player) entity;
		if(player.getEffects(TripleShotPowerupEffect.class) != null){
			return null; // there's already this powerup active
		}
		return new TripleShotPowerupEffect(player);
	}


	@Override
	public void update(float delta, World world) {
	}
}
