package me.retrodaredevil.game.trackshooter.entity.player;

import com.badlogic.gdx.math.Vector2;

import me.retrodaredevil.game.trackshooter.entity.EntityController;
import me.retrodaredevil.game.trackshooter.entity.movement.MoveComponent;
import me.retrodaredevil.game.trackshooter.entity.movement.OnTrackMoveComponent;
import me.retrodaredevil.game.trackshooter.entity.movement.RotationalVelocitySetterMoveComponent;
import me.retrodaredevil.game.trackshooter.entity.movement.TravelVelocitySetterMoveComponent;
import me.retrodaredevil.game.trackshooter.util.Constants;
import me.retrodaredevil.game.trackshooter.util.MathUtil;
import me.retrodaredevil.game.trackshooter.world.World;

import static java.lang.Math.max;
import static java.lang.Math.min;

public class PlayerAIController implements EntityController {
	private final World world;
	private final Player player;
	private Float lastShot = null;

	public PlayerAIController(World world, Player player) {
		this.world = world;
		this.player = player;
	}

	@Override
	public void update(float delta) {
		MoveComponent moveComponent = player.getMoveComponent();
		if(moveComponent instanceof OnTrackMoveComponent){
//			OnTrackMoveComponent trackMove = (OnTrackMoveComponent) moveComponent;
//			float forwardDirectionAngle = world.getTrack().getForwardDirection(trackMove.getDistanceOnTrack());

			final float velocity; // 1, -1 or 0
			final float time = world.getTime() % 30;
			if(time >= 28){
				velocity = 0;
			} else if(time >= 20){
				velocity = -1;
			} else if(time >= 15){
				velocity = -.75f;
			} else if(time >= 13){
				velocity = 1;
			} else if(time >= 7){
				velocity = -1;
			} else if(time >= 5){
				velocity = 0;
			} else {
				velocity = 1;
			}

			if(moveComponent instanceof TravelVelocitySetterMoveComponent){
				TravelVelocitySetterMoveComponent velocityMove = (TravelVelocitySetterMoveComponent) moveComponent;
				velocityMove.getTravelVelocitySetter().setDesiredVelocity(Constants.PLAYER_VELOCITY * velocity, 5, Constants.PLAYER_VELOCITY);
			}

			Float rotationalVelocity; // may use null as magic value to change later

			if(time >= 25){
				rotationalVelocity = null;
			} else if(time >= 20){
				rotationalVelocity = 90f;
			} else if(time >= 15){
				rotationalVelocity = -90f;
			} else if(time >= 7){
				rotationalVelocity = null;
			} else if(time >= 3){
				rotationalVelocity = 90f;
			} else {
				rotationalVelocity = 0f;
			}
			if(rotationalVelocity == null){
				final float targetRotation = Vector2.Zero.cpy().sub(player.getLocation()).angle();
				final float currentRotation = player.getRotation();
				final float minChange = MathUtil.minChange(targetRotation, currentRotation, 360);
				rotationalVelocity = max(min(minChange * 20, 60), -60);
			}

			if(moveComponent instanceof RotationalVelocitySetterMoveComponent){
				RotationalVelocitySetterMoveComponent rotationalMove = (RotationalVelocitySetterMoveComponent) moveComponent;
				rotationalMove.getRotationalVelocitySetter().setVelocity(rotationalVelocity);
			}
			if(lastShot == null || lastShot + .5f <= world.getTime()){
				if(player.shootBullet(null) != null){
					lastShot = world.getTime();
				}
			}
			player.activatePowerup();
		}
	}
}
