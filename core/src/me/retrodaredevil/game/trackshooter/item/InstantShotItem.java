package me.retrodaredevil.game.trackshooter.item;

import com.badlogic.gdx.scenes.scene2d.ui.Image;

import me.retrodaredevil.game.trackshooter.effect.Effect;
import me.retrodaredevil.game.trackshooter.effect.InstantShotPowerupEffect;
import me.retrodaredevil.game.trackshooter.entity.Bullet;
import me.retrodaredevil.game.trackshooter.entity.Entity;
import me.retrodaredevil.game.trackshooter.entity.player.Player;
import me.retrodaredevil.game.trackshooter.util.Resources;
import me.retrodaredevil.game.trackshooter.world.World;

/**
 * @deprecated This class should work but hasn't been used in a while and needs the texture to be updated
 */
@Deprecated
public class InstantShotItem extends EffectItem implements PowerupActivateListenerItem {

	private final Bullet.ShotType shotType;

	public InstantShotItem(Bullet.ShotType shotType){
		super(new Image(Resources.BULLET_TEXTURE)); // TODO if we use this class update this texture
		this.shotType = shotType;
	}
	@Override
	protected Effect createEffect(Entity entity) {
		Player player = (Player) entity;
		if(player.getEffects(InstantShotPowerupEffect.class) != null){
			return null;
		}

		return new InstantShotPowerupEffect(player, shotType);
	}

	@Override
	public boolean activatePowerup(World world, Player player) {
		return activateEffect(player);
	}

	@Override
	public void update(float delta, World world) {
	}
}
