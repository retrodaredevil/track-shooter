package me.retrodaredevil.game.trackshooter.entity.powerup;

import com.badlogic.gdx.scenes.scene2d.ui.Image;

import me.retrodaredevil.game.trackshooter.entity.player.Player;
import me.retrodaredevil.game.trackshooter.item.Item;
import me.retrodaredevil.game.trackshooter.item.TripleShotPowerupItem;
import me.retrodaredevil.game.trackshooter.render.ImageRenderComponent;
import me.retrodaredevil.game.trackshooter.world.World;

public abstract class SimpleItemPowerupEntity extends PowerupPackage {
	private final String drawableName;
	protected SimpleItemPowerupEntity(float velocity, float startingTrackDistance, String drawableName) {
		super(velocity, startingTrackDistance);
		this.drawableName = drawableName;
	}

	@Override
	public void beforeSpawn(World world) {
		super.beforeSpawn(world);
		ImageRenderComponent renderComponent = new ImageRenderComponent(new Image(world.getSkin().getDrawable(drawableName)), this, .8f, .8f);
		renderComponent.setFacingDirection(0);
		this.setRenderComponent(renderComponent);
	}

	public static SimpleItemPowerupEntity createTripleShotPowerupEntity(float velocity, float startingTrackDistance){
		return new SimpleItemPowerupEntity(velocity, startingTrackDistance, "powerup") {
			@Override
			protected Item createItem(World world, Player player) {
				return new TripleShotPowerupItem(world.getSkin());
			}
		};
	}


	protected abstract Item createItem(World world, Player player);

	@Override
	protected void onHit(World world, Player player) {
		player.addItem(createItem(world, player));
	}
}
