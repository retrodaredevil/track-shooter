package me.retrodaredevil.game.trackshooter.effect;

import me.retrodaredevil.game.trackshooter.entity.Bullet;
import me.retrodaredevil.game.trackshooter.entity.player.Player;
import me.retrodaredevil.game.trackshooter.world.World;

public class InstantShotPowerupEffect implements Effect {

	private boolean done = false;
	private final Player player;
	private final Bullet.ShotType shotType;

	public InstantShotPowerupEffect(Player player, Bullet.ShotType shotType){
		this.player = player;
		this.shotType = shotType;
	}

	@Override
	public boolean isDone() {
		return done;
	}

	@Override
	public float percentDone(World world) {
		return done ? 1 : 0; // likely will always be 0 but we'll put this line here just in case
	}

	@Override
	public void update(float delta, World world) {
		if(done){
			throw new IllegalStateException("This effect wasn't removed even though we were done!");
		}
		done = player.shootBullet(world, shotType) != null;
	}
}
