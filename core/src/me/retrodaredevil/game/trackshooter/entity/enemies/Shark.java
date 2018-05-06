package me.retrodaredevil.game.trackshooter.entity.enemies;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import me.retrodaredevil.game.trackshooter.entity.Bullet;
import me.retrodaredevil.game.trackshooter.entity.Entity;
import me.retrodaredevil.game.trackshooter.entity.Hittable;
import me.retrodaredevil.game.trackshooter.entity.SimpleEntity;
import me.retrodaredevil.game.trackshooter.entity.movement.*;
import me.retrodaredevil.game.trackshooter.entity.player.Player;
import me.retrodaredevil.game.trackshooter.render.SharkRenderComponent;
import me.retrodaredevil.game.trackshooter.util.CannotHitException;
import me.retrodaredevil.game.trackshooter.util.EntityUtil;
import me.retrodaredevil.game.trackshooter.util.Resources;
import me.retrodaredevil.game.trackshooter.world.World;

public class Shark extends SimpleEntity implements Hittable {
	private static final int POINTS = 200; // 100 points for killing a Shark

	private int lives = 3;

	public Shark(long pause){
		setRenderComponent(new SharkRenderComponent(Resources.SHARK_REGIONS, this, 1.0f, 1.0f));

		ChainMoveComponent pauseComponent = new TimedMoveComponent(pause);
		pauseComponent.setNextComponent(new PointTargetMoveComponent(this, new Vector2(0, 0), 5, 2));
		setMoveComponent(pauseComponent);

		setHitboxSize(.7f, .7f);
//		setHitboxSize(1, 1);
	}
	public Shark(){
		this(0);
	}

	@Override
	public void onHit(World world, Entity other) throws CannotHitException {
		if(other.getShooter() == this || (!(other instanceof Bullet) && !(other instanceof Player))){
			throw new CannotHitException(other, this);
		}
		Gdx.app.debug("hit by", other.toString() + " at " + Gdx.app.getGraphics().getFrameId());
		MoveComponent wantedComponent = getMoveComponent();
		boolean spinning = false;
		if(wantedComponent instanceof SpinMoveComponent){
			spinning = true;
			wantedComponent = wantedComponent.getNextComponent();
		}
		SpinMoveComponent spin = new SpinMoveComponent(this, 1000, 360 * (1.75f + .5f * MathUtils.random()));
		spin.setNextComponent(wantedComponent);
		this.setMoveComponent(spin);
		if(spinning){
			return;
		}
		lives--;

		if(lives <= 0) {
			EntityUtil.displayScore(world, this.getLocation(), Resources.EVEN_BONUS_REGIONS[0]);

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
