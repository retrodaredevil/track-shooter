package me.retrodaredevil.game.trackshooter.entity.player;

import me.retrodaredevil.controller.input.InputPart;
import me.retrodaredevil.controller.input.JoystickPart;
import me.retrodaredevil.controller.output.ControllerRumble;
import me.retrodaredevil.game.input.GameInput;
import me.retrodaredevil.game.trackshooter.entity.EntityController;
import me.retrodaredevil.game.trackshooter.entity.movement.MoveComponent;
import me.retrodaredevil.game.trackshooter.entity.movement.TravelRotateVelocityOnTrackMoveComponent;
import me.retrodaredevil.game.trackshooter.world.World;
import me.retrodaredevil.controller.input.SimpleJoystickPart;

public class PlayerController implements EntityController{
	private static final float VELOCITY_PER_SECOND = 5f;
	private static final float ACCEL_ROTATE_PER_SECOND = -270; // target rotate velocity used for calculating accelerating but capped at max
//	private static final float MAX_ROTATE_PER_SECOND = 270; // max abs rotate velocity
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
		if(move instanceof TravelRotateVelocityOnTrackMoveComponent){

			// ==== Track Movement ====
			TravelRotateVelocityOnTrackMoveComponent trackMove = (TravelRotateVelocityOnTrackMoveComponent) move;
			JoystickPart movementJoy = gameInput.mainJoystick();
			boolean slow = gameInput.slow().isDown();
			if(!movementJoy.isDeadzone() || slow) {
				float mult = slow ? .5f : 1;
//				trackMove.setVelocity((float) (movementJoy.getX() * VELOCITY_PER_SECOND * mult));
				float movePercent = world.getTrack().getMovePercent((float) movementJoy.getAngle(), trackMove.getDistanceOnTrack());
				int moveDirection = (int) (Math.signum(movePercent) * (float) Math.ceil(Math.abs(movePercent))); // round this up/down and base speed off magnitude

				float joyScale = movementJoy.getJoystickType().shouldScale()
						? (float) SimpleJoystickPart.getScaled(movementJoy.getAngle())
						: 1;

				float actualMagnitude = (float) (joyScale * movementJoy.getMagnitude());
				if(movementJoy.getJoystickType().isRangeOver()){
					if(actualMagnitude > 1){
						actualMagnitude = 1;
					}
				} else if(actualMagnitude > 1){
					System.err.println("Joystick: " + movementJoy + "'s magnitude is over 1!.");
				}

				trackMove.getTravelVelocitySetter().setVelocity(
						actualMagnitude
						* moveDirection
						* VELOCITY_PER_SECOND
						* mult
				);
			} else {
				trackMove.getTravelVelocitySetter().setVelocity(0);
			}

			// ==== Rotation ====
			InputPart rotateAxis = gameInput.rotateAxis();
			double position = rotateAxis.getPosition();
			if (rotateAxis.getAxisType().shouldUseDelta()) { // normal joystick
				if(rotateAxis.getAxisType().isRangeOver()){
					if(Math.abs(position) > 1){
						position = Math.signum(position);
					}
				}
//				System.out.println(position); // .004
				float desired = (float) (ACCEL_ROTATE_PER_SECOND * position);
				if (rotateAxis.isDeadzone()) {
					desired = 0;
				}
//				trackMove.setDesiredRotationalVelocity(desired, (1f / FULL_SPEED_IN), MAX_ROTATE_PER_SECOND);
//              player.setRotation(player.getRotation() + desired * delta);
				trackMove.getRotationalVelocitySetter().setVelocity(desired);
			} else { // probably a mouse
//				System.out.println(position);
				player.setRotation(player.getRotation() + (float) position * ROTATION_PER_MOUSE_PIXEL); // note ROTATION_PER_MOUSE_PIXEL should be negative
//				trackMove.setDesiredRotationalVelocity((float) x * ROTATION_PER_MOUSE_PIXEL / delta, 0, Float.MAX_VALUE);
			}
		}
		ControllerRumble rumble = gameInput.getRumble();
		if (gameInput.fireButton().isPressed()) {
			boolean didShoot = player.shootBullet(world, null) != null;
			if(didShoot && rumble != null && rumble.isConnected()){
				rumble.rumble(.5f);
			}
		}
		if(gameInput.activatePowerup().isPressed()){
			player.activatePowerup(world);
		}
	}
}
