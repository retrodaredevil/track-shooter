package me.retrodaredevil.game.trackshooter.entity.player;

import me.retrodaredevil.controller.input.InputPart;
import me.retrodaredevil.controller.input.JoystickPart;
import me.retrodaredevil.controller.output.ControllerRumble;
import me.retrodaredevil.game.input.GameInput;
import me.retrodaredevil.game.trackshooter.entity.EntityController;
import me.retrodaredevil.game.trackshooter.entity.movement.MoveComponent;
import me.retrodaredevil.game.trackshooter.entity.movement.OnTrackMoveComponent;
import me.retrodaredevil.game.trackshooter.entity.movement.RotationalVelocitySetter;
import me.retrodaredevil.game.trackshooter.entity.movement.TravelVelocityMoveComponent;
import me.retrodaredevil.game.trackshooter.entity.movement.TravelVelocitySetter;
import me.retrodaredevil.game.trackshooter.world.World;
import me.retrodaredevil.controller.input.SimpleJoystickPart;

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
			JoystickPart movementJoy = gameInput.mainJoystick();
			boolean slow = gameInput.slow().isDown();
			final float velocity;
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
				velocity =
						actualMagnitude
						* moveDirection
						* VELOCITY_PER_SECOND
						* mult;
			} else {
				velocity = 0;
			}
			if(move instanceof TravelVelocityMoveComponent) {
				((TravelVelocitySetter) move).getTravelVelocitySetter().setVelocity(velocity);
			} else {
				System.err.println("move not instanceof TravelVelocityMoveComponent. Remove print error if intended.");
				trackMove.setDistanceOnTrack(trackMove.getDistanceOnTrack() + delta * velocity);
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
				float desired = (float) (ROTATE_PER_SECOND * position);
				if (rotateAxis.isDeadzone()) {
					desired = 0;
				}

				if(move instanceof RotationalVelocitySetter){
					((RotationalVelocitySetter) move).getRotationalVelocitySetter().setVelocity(desired);
				} else {
					player.setRotation(player.getRotation() + delta * desired);
				}
			} else { // probably a mouse
				if(move instanceof RotationalVelocitySetter){
					((RotationalVelocitySetter) move).getRotationalVelocitySetter().setVelocity(0);
				}
				player.setRotation(player.getRotation() + (float) position * ROTATION_PER_MOUSE_PIXEL); // note ROTATION_PER_MOUSE_PIXEL should be negative
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
