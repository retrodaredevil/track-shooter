package me.retrodaredevil.game.trackshooter.entity.powerup;

import com.badlogic.gdx.math.MathUtils;
import me.retrodaredevil.game.trackshooter.CollisionIdentity;
import me.retrodaredevil.game.trackshooter.entity.SimpleEntity;
import me.retrodaredevil.game.trackshooter.world.World;

public class SimplePowerup extends SimpleEntity implements PowerupEntity {
	protected SimplePowerup(){
		super();
		canRespawn = false;
		canSetToRemove = true;
		collisionIdentity = CollisionIdentity.POWERUP;
	}

	public static float getRandomTrackStarting(World world){
		return MathUtils.random(world.getTrack().getTotalDistance());
	}
}
