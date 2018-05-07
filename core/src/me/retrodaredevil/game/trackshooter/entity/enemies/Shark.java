package me.retrodaredevil.game.trackshooter.entity.enemies;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
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
	private static final int POINTS = 200; // 200 points for killing a Shark

	private int lives = 3;
	private int spinLives = 40; // hitting many times while spinning worth double points

	public Shark(long pause){
		setRenderComponent(new SharkRenderComponent(Resources.SHARK_REGIONS, this, 1.0f, 1.0f));
		setMoveComponent(new TimedMoveComponent(pause, new SmoothTravelMoveComponent(this, new Vector2(0, 0), 5, 2)));
		setHitboxSize(.7f, .7f);
	}
	public Shark(){
		this(0);
	}

	@Override
	public void onHit(World world, Entity other) throws CannotHitException {
		if(other.getShooter() == this || (!(other instanceof Bullet) && !(other instanceof Player))){
			throw new CannotHitException(other, this);
		}
//		Gdx.app.debug("hit by", other.toString() + " at " + Gdx.app.getGraphics().getFrameId());
		MoveComponent wantedComponent = getMoveComponent();
		boolean spinning = false;
		if(wantedComponent instanceof SpinMoveComponent){
			spinning = true;
			wantedComponent = wantedComponent.getNextComponent();
			while(wantedComponent instanceof TimedMoveComponent){
				// if Shark is still and the player shoots them, we search until there are no more spins or waits.
				wantedComponent = wantedComponent.getNextComponent();
			}
		}
		SpinMoveComponent spin = new SpinMoveComponent(this, 1000, 360 * (1.75f + .5f * MathUtils.random()));
		spin.setNextComponent(wantedComponent);
		this.setMoveComponent(spin);
		if(spinning){
			spinLives--;
		} else {
			lives--;
		}
		boolean spinDeath = spinLives <= 0;
		if(lives <= 0 || spinDeath) {
			float multiplier = spinDeath ? 2 : 1;
			EntityUtil.displayScore(world, this.getLocation(), Resources.EVEN_BONUS_REGIONS[spinDeath ? 1 : 0]); // display 200

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
				player.getScoreObject().onKill(this, other, (int) (POINTS * multiplier));
			}
		}

	}

	@Override
	public boolean shouldRemove(World world) {
		return lives <= 0 || spinLives <= 0;
	}
}
