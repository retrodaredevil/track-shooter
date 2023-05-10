package me.retrodaredevil.game.trackshooter.entity.powerup;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

import me.retrodaredevil.game.trackshooter.entity.player.Player;
import me.retrodaredevil.game.trackshooter.item.Item;
import me.retrodaredevil.game.trackshooter.item.TripleShotPowerupItem;
import me.retrodaredevil.game.trackshooter.render.components.ImageRenderComponent;
import me.retrodaredevil.game.trackshooter.world.World;

public abstract class SimpleItemPowerupEntity extends PowerupPackage {
	private final String drawableName;
	protected SimpleItemPowerupEntity(World world, float velocity, float startingTrackDistance, String drawableName) {
		super(world, velocity, startingTrackDistance);
		this.drawableName = drawableName;
	}

	@Override
	public void beforeSpawn() {
		super.beforeSpawn();
		ImageRenderComponent renderComponent = new ImageRenderComponent(new Image(world.getMainSkin().getDrawable(drawableName)), this, .8f, .8f);
		renderComponent.setFacingDirection(0);
		this.setRenderComponent(renderComponent);
	}

	public static SimpleItemPowerupEntity createTripleShotPowerupEntity(World world, float velocity, float startingTrackDistance){
		return new SimpleItemPowerupEntity(world, velocity * MathUtils.randomSign(), startingTrackDistance, "powerup") {
			@Override
			protected Item createItem(Player player) {
				return new TripleShotPowerupItem(world);
			}
		};
	}


	protected abstract Item createItem(Player player);

	@Override
	protected void onHit(Player player) {
		player.addItem(createItem(player));
	}
}
