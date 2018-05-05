package me.retrodaredevil.game.trackshooter.entity.enemies;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import me.retrodaredevil.game.trackshooter.entity.Bullet;
import me.retrodaredevil.game.trackshooter.entity.Entity;
import me.retrodaredevil.game.trackshooter.entity.Hittable;
import me.retrodaredevil.game.trackshooter.entity.SimpleEntity;
import me.retrodaredevil.game.trackshooter.entity.movement.MoveComponent;
import me.retrodaredevil.game.trackshooter.entity.movement.PointTargetMoveComponent;
import me.retrodaredevil.game.trackshooter.entity.movement.SpinMoveComponent;
import me.retrodaredevil.game.trackshooter.entity.player.Player;
import me.retrodaredevil.game.trackshooter.render.SharkRenderComponent;
import me.retrodaredevil.game.trackshooter.util.CannotHitException;
import me.retrodaredevil.game.trackshooter.util.Resources;
import me.retrodaredevil.game.trackshooter.world.World;

public class Shark extends SimpleEntity implements Hittable {
	private static final int POINTS = 100; // 100 points for killing a Shark

	private static TextureRegion[] regions = null;

	private int lives = 3;

	public Shark(){
		updateTextureRegions();
		setRenderComponent(new SharkRenderComponent(regions, this, 1.0f, 1.0f));

		setMoveComponent(new PointTargetMoveComponent(this, new Vector2(0, 0), 5, 1));

		setHitboxSize(.7f, .7f);
//		setHitboxSize(1, 1);
	}
	private static void updateTextureRegions(){

		Texture texture = Resources.SHARK_TEXTURE;
		int width;
		int height;
		if(texture.getWidth() > texture.getHeight()){
			width = texture.getWidth() / 3;
			height = texture.getHeight();
		} else {
			width = texture.getWidth();
			height = texture.getHeight() / 3;
		}
		TextureRegion[][] regions2d = TextureRegion.split(texture, width, height);
		regions = new TextureRegion[regions2d.length * regions2d[0].length];
		int index = 0;
		for(TextureRegion[] nestRegion : regions2d) for(TextureRegion region : nestRegion){
			regions[index] = region;
			index++;
		}
	}

	@Override
	public void onHit(World world, Entity other) throws CannotHitException {
		if(other.getShooter() == this || (!(other instanceof Bullet) && !(other instanceof Player))){
			throw new CannotHitException(other, this);
		}
		Gdx.app.debug("hit by", other.toString() + " at " + Gdx.app.getGraphics().getFrameId());
		MoveComponent wantedComponent = getMoveComponent();
		if(wantedComponent instanceof SpinMoveComponent){
			wantedComponent = wantedComponent.getNextComponent();
		}
		SpinMoveComponent spin = new SpinMoveComponent(this, 1000, 360 * (1.75f + .5f * MathUtils.random()));
		spin.setNextComponent(wantedComponent);
		this.setMoveComponent(spin);
		lives--;

		if(lives <= 0) {
			Player player = null;
			if (other instanceof Player) {
				player = (Player) other;
			} else {
				Entity shooter = other.getShooter();
				if (shooter instanceof Player) {
					player = (Player) shooter;
				}
			}
			if(player != null) {
				player.getScoreObject().onKill(this, other, POINTS);
			}
		}

	}

	@Override
	public boolean shouldRemove(World world) {
		return lives <= 0;
	}
}
