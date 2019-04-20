package me.retrodaredevil.game.trackshooter.entity.player;

import me.retrodaredevil.controller.input.InputPart;
import me.retrodaredevil.controller.input.JoystickPart;
import me.retrodaredevil.controller.output.ControllerRumble;
import me.retrodaredevil.game.trackshooter.entity.EntityController;
import me.retrodaredevil.game.trackshooter.entity.movement.*;
import me.retrodaredevil.game.trackshooter.input.GameInput;
import me.retrodaredevil.game.trackshooter.util.Constants;
import me.retrodaredevil.game.trackshooter.world.World;

public class PlayerController implements EntityController{
	private static final float ROTATE_PER_SECOND = -270; // rotate velocity when position is 1
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
//				trackMove.setVelocity((float) (movementJoy.getX() * VELOCITY * mult));
				float movePercent = world.getTrack().getMovePercent((float) movementJoy.getAngle(), trackMove.getDistanceOnTrack());
				int moveDirection = (int) (Math.signum(movePercent) * (float) Math.ceil(Math.abs(movePercent))); // round this up/down and base speed off magnitude

				float correctMagnitude = (float) movementJoy.getCorrectMagnitude();
				if(correctMagnitude > 1){
					if(!movementJoy.getJoystickType().isRangeOver()){
						throw new IllegalStateException("movementJoy's correct magnitude is: " + correctMagnitude);
					}
					correctMagnitude = 1;
				}
				velocity =
						correctMagnitude
						* moveDirection
						* Constants.PLAYER_VELOCITY
						* mult;
			} else {
				velocity = 0;
			}
			// now velocity is initialized
			if(move instanceof TravelVelocitySetterMoveComponent) {
				((TravelVelocitySetterMoveComponent) move).getTravelVelocitySetter().setVelocity(velocity);
			}

		} else if(move instanceof VectorVelocitySetterMoveComponent){
			VectorVelocitySetterMoveComponent vectorMove = (VectorVelocitySetterMoveComponent) move;
			JoystickPart movementJoy = gameInput.getMainJoystick();
			boolean slow = gameInput.getSlowButton().isDown();
			final float velocity;
			if(!movementJoy.isDeadzone() || slow){
				float mult = slow ? .5f : 1;
				velocity = (float) Math.min(movementJoy.getCorrectMagnitude(), 1) * mult * Constants.PLAYER_FREE_VELOCITY;
			} else {
				velocity = 0;
			}
			vectorMove.getVectorVelocitySetter().setDesiredVelocityAngleMagnitude((float) movementJoy.getAngle(), velocity, 16, Constants.PLAYER_FREE_VELOCITY);

		}
		// ==== Rotation ====
		InputPart rotateAxis = gameInput.getRotateAxis();
		double position = rotateAxis.getPosition();
		if (rotateAxis.getAxisType().isShouldUseDelta()) { // normal joystick
			float desired = (float) (ROTATE_PER_SECOND * position);
			if (rotateAxis.isDeadzone()) {
				desired = 0;
			}

			if(move instanceof RotationalVelocitySetterMoveComponent){
				((RotationalVelocitySetterMoveComponent) move).getRotationalVelocitySetter().setVelocity(desired);
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
			int numberShot = player.shootBullet(world, null).size();
			ControllerRumble rumble = gameInput.getRumble();
			if(numberShot > 0 && (numberShot > 1 || gameInput.getRumbleOnSingleShot().isDown()) && rumble.isConnected()){
				rumble.rumbleTime(70, .1);
			}
		}

		// ==== Powerup ====
		if(gameInput.getActivatePowerup().isPressed()){
			player.activatePowerup(world);
		}
	}
}
