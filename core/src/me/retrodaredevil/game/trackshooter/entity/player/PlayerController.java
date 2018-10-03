package me.retrodaredevil.game.trackshooter.entity.player;

import me.retrodaredevil.controller.input.InputPart;
import me.retrodaredevil.controller.input.JoystickPart;
import me.retrodaredevil.controller.output.ControllerRumble;
import me.retrodaredevil.game.input.GameInput;
import me.retrodaredevil.game.trackshooter.entity.EntityController;
import me.retrodaredevil.game.trackshooter.entity.movement.MoveComponent;
import me.retrodaredevil.game.trackshooter.entity.movement.OnTrackMoveComponent;
import me.retrodaredevil.game.trackshooter.entity.movement.RotationalVelocitySetterMoveComponent;
import me.retrodaredevil.game.trackshooter.entity.movement.TravelVelocitySetterMoveComponent;
import me.retrodaredevil.game.trackshooter.world.World;

public class PlayerController implements EntityController{
	private static final float VELOCITY_PER_SECOND = 5f;
	private static final float ROTATE_PER_SECOND = -270; // target rotate velocity used for calculating accelerating but capped at max
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
			JoystickPart movementJoy = gameInput.getMainJoystick();
			boolean slow = gameInput.getSlowButton().isDown();
			final float velocity;
			if(!movementJoy.isDeadzone() || slow) {
				float mult = slow ? .5f : 1;
//				trackMove.setVelocity((float) (movementJoy.getX() * VELOCITY_PER_SECOND * mult));
				float movePercent = world.getTrack().getMovePercent((float) movementJoy.getAngle(), trackMove.getDistanceOnTrack());
				int moveDirection = (int) (Math.signum(movePercent) * (float) Math.ceil(Math.abs(movePercent))); // round this up/down and base speed off magnitude

				float correctMagnitude = (float) movementJoy.getCorrectMagnitude();
				if(correctMagnitude > 1){
					if(!movementJoy.getJoystickType().isRangeOver()){
						throw new IllegalArgumentException("movementJoy's correct magnitude is: " + correctMagnitude);
					}
					correctMagnitude = 1;
				}
				velocity =
						correctMagnitude
						* moveDirection
						* VELOCITY_PER_SECOND
						* mult;
			} else {
				velocity = 0;
			}
			// now velocity is initialized
			if(move instanceof TravelVelocitySetterMoveComponent) {
				((TravelVelocitySetterMoveComponent) move).getTravelVelocitySetter().setVelocity(velocity);
			} else {
				System.err.println("move not instanceof TravelVelocityMoveComponent. Remove print error if intended.");
				trackMove.setDistanceOnTrack(trackMove.getDistanceOnTrack() + delta * velocity);
			}

		} else {
			System.err.println("MoveComponent is not on track. Remove print statement if intended.");
		}
		// ==== Rotation ====
		InputPart rotateAxis = gameInput.getRotateAxis();
		double position = rotateAxis.getPosition();
		if (rotateAxis.getAxisType().isShouldUseDelta()) { // normal joystick
//			if(rotateAxis.getAxisType().isRangeOver()){
//				if(Math.abs(position) > 1){
//					position = Math.signum(position);
//				}
//			}
			float desired = (float) (ROTATE_PER_SECOND * position);
			if (rotateAxis.isDeadzone()) {
				desired = 0;
			}

			if(move instanceof RotationalVelocitySetterMoveComponent){
				((RotationalVelocitySetterMoveComponent) move).getRotationalVelocitySetter().setVelocity(desired);
			} else {
				System.err.println("move not instanceof RotationalVelocityMoveComponent. Remove print error if intended.");
				player.setRotation(player.getRotation() + delta * desired);
			}
		} else { // probably a mouse
			if(move instanceof RotationalVelocitySetterMoveComponent){ // 0 velocity just in case
				((RotationalVelocitySetterMoveComponent) move).getRotationalVelocitySetter().setVelocity(0);
			}
			// change rotation manually
			player.setRotation(player.getRotation() + (float) position * ROTATION_PER_MOUSE_PIXEL); // note ROTATION_PER_MOUSE_PIXEL should be negative
		}

		// ==== Rumble and Shoot ====
		if (gameInput.getFireButton().isPressed()) {
			boolean didShoot = player.shootBullet(world, null) != null;
			ControllerRumble rumble = gameInput.getRumble();
			if(didShoot && rumble.isConnected()){
				rumble.rumbleTime(100, .1);
			}
		}

		// ==== Powerup ====
		if(gameInput.getActivatePowerup().isPressed()){
			player.activatePowerup(world);
		}
	}
}
