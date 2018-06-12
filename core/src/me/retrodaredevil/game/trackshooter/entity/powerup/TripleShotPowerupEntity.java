package me.retrodaredevil.game.trackshooter.entity.powerup;

import com.badlogic.gdx.scenes.scene2d.ui.Image;
import me.retrodaredevil.game.trackshooter.entity.player.Player;
import me.retrodaredevil.game.trackshooter.item.TripleShotPowerupItem;
import me.retrodaredevil.game.trackshooter.render.ImageRenderComponent;
import me.retrodaredevil.game.trackshooter.util.Resources;
import me.retrodaredevil.game.trackshooter.world.World;

public class TripleShotPowerupEntity extends PowerupPackage {
	public TripleShotPowerupEntity(float velocity, float startingTrackDistance) {
		super(velocity, startingTrackDistance);

		ImageRenderComponent renderComponent = new ImageRenderComponent(new Image(Resources.POWERUP_TEXTURE), this, .8f, .8f);
		renderComponent.setFacingDirection(0);
		setRenderComponent(renderComponent);
	}

	@Override
	protected void onHit(World world, Player player) {
		player.addItem(new TripleShotPowerupItem());
	}
}
