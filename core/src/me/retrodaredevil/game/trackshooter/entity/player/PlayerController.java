package me.retrodaredevil.game.trackshooter.entity.player;

import me.retrodaredevil.game.input.GameInput;
import me.retrodaredevil.game.trackshooter.entity.EntityController;
import me.retrodaredevil.game.trackshooter.entity.movement.MoveComponent;
import me.retrodaredevil.game.trackshooter.entity.movement.OnTrackMoveComponent;
import me.retrodaredevil.game.trackshooter.world.World;
import me.retrodaredevil.controller.JoystickPart;

public class PlayerController implements EntityController{
	private static final float VELOCITY_PER_SECOND = 5f;
	private static final float ACCEL_ROTATE_PER_SECOND = -360; // target rotate velocity used for calculating accelerating but capped at max
	private static final float MAX_ROTATE_PER_SECOND = 270; // max abs rotate velocity
	private static final float FULL_SPEED_IN = 0; // in seconds - amount of time to fully accelerate rotational velocity
	private static final float ROTATION_PER_MOUSE_PIXEL = -.07f; // how many degrees to change when the mouse is moved one pixel

	private Player player;
	private GameInput gameInput;
	public PlayerController(Player player, GameInput gameInput){
		this.player = player;
		this.gameInput = gameInput;
	}

	@Override
	public void update(float delta, World world) {
		MoveComponent move = player.getMoveComponent();
		if(move instanceof OnTrackMoveComponent){

			// ==== Track Movement ====
			OnTrackMoveComponent trackMove = (OnTrackMoveComponent) move;
			JoystickPart movementJoy = gameInput.mainJoystick();
			boolean slow = gameInput.slow().isDown();
			if(!movementJoy.isDeadzone() || slow) {
				float mult = slow ? .5f : 1;
//				trackMove.setVelocity((float) (movementJoy.getX() * VELOCITY_PER_SECOND * mult));
				float movePercent = world.getTrack().getMovePercent((float) movementJoy.getAngle(), trackMove.getDistance());
				movePercent = Math.signum(movePercent) * (float) Math.ceil(Math.abs(movePercent));

				float joyScale = (float) JoystickPart.getScaled(movementJoy.getX(), movementJoy.getY(), movementJoy.getAngle());
				trackMove.setVelocity(
						(float) movementJoy.getMagnitude() * joyScale
						* movePercent // movePercent is -1, 0 or 1
						* VELOCITY_PER_SECOND * mult);
			} else {
				trackMove.setVelocity(0);
			}

			// ==== Rotation ====
			JoystickPart rotateJoy = gameInput.rotateJoystick();
			boolean isMouse = rotateJoy.getJoystickType() == JoystickPart.JoystickType.MOUSE;
			double x = rotateJoy.getX();

			if(isMouse){
				player.setRotation(player.getRotation() + (float) x * ROTATION_PER_MOUSE_PIXEL); // note ROTATION_PER_MOUSE_PIXEL should be negative
//				trackMove.setDesiredRotationalVelocity((float) x * ROTATION_PER_MOUSE_PIXEL / delta, 0, Float.MAX_VALUE);
			} else {
				float desired = (float) (ACCEL_ROTATE_PER_SECOND * x);
				if (rotateJoy.isDeadzone()) {
					desired = 0;
				}
				trackMove.setDesiredRotationalVelocity(desired, (1f / FULL_SPEED_IN), MAX_ROTATE_PER_SECOND);
			}
		}
		if (gameInput.fireButton().isPressed()) {
			player.shootBullet(world, null);
		}
	}
}
