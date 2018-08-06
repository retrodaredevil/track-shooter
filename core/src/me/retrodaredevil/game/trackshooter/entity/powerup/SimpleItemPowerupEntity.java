package me.retrodaredevil.game.trackshooter.entity.powerup;

import com.badlogic.gdx.scenes.scene2d.ui.Image;

import me.retrodaredevil.game.trackshooter.entity.player.Player;
import me.retrodaredevil.game.trackshooter.item.Item;
import me.retrodaredevil.game.trackshooter.item.TripleShotPowerupItem;
import me.retrodaredevil.game.trackshooter.render.ImageRenderComponent;
import me.retrodaredevil.game.trackshooter.util.Resources;
import me.retrodaredevil.game.trackshooter.world.World;

public abstract class SimpleItemPowerupEntity extends PowerupPackage {
	protected SimpleItemPowerupEntity(float velocity, float startingTrackDistance) {
		super(velocity, startingTrackDistance);
	}

	public static SimpleItemPowerupEntity createTripleShotPowerupEntity(float velocity, float startingTrackDistance){
		SimpleItemPowerupEntity entity = new SimpleItemPowerupEntity(velocity, startingTrackDistance) {
			@Override
			protected Item createItem(Player player) {
				return new TripleShotPowerupItem();
			}
		};

		ImageRenderComponent renderComponent = new ImageRenderComponent(new Image(Resources.POWERUP_TEXTURE), entity, .8f, .8f);
		renderComponent.setFacingDirection(0);
		entity.setRenderComponent(renderComponent);

		return entity;
	}

	protected abstract Item createItem(Player player);

	@Override
	protected void onHit(World world, Player player) {
		player.addItem(createItem(player));
	}
}
