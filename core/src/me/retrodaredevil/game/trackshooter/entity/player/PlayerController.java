package me.retrodaredevil.game.trackshooter.entity.player;

import me.retrodaredevil.game.input.GameInput;
import me.retrodaredevil.game.trackshooter.entity.EntityController;
import me.retrodaredevil.game.trackshooter.entity.movement.MoveComponent;
import me.retrodaredevil.game.trackshooter.entity.movement.OnTrackMoveComponent;
import me.retrodaredevil.game.trackshooter.world.World;
import me.retrodaredevil.input.JoystickPart;

public class PlayerController implements EntityController{
	private static final float VELOCITY_PER_SECOND = 5f;
	private static final float ROTATE_PER_SECOND = 360;
	private static final float ROTATION_PER_MOUSE_PIXEL = -.07f;

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
				trackMove.setVelocity((float) (movementJoy.getX() * VELOCITY_PER_SECOND * mult));
			} else {
				trackMove.setVelocity(0);
			}

			// ==== Rotation ====
			JoystickPart rotateJoy = gameInput.rotateJoystick();
			boolean isMouse = rotateJoy.getJoystickType() == JoystickPart.JoystickType.MOUSE;
			double x = rotateJoy.getX();

			if(isMouse){
				player.setRotation(player.getRotation() + (float) x * ROTATION_PER_MOUSE_PIXEL);
			} else {
				float desired = (float) (ROTATE_PER_SECOND * x);
				desired *= -1;
				if (rotateJoy.isDeadzone()) {
					desired = 0;
				}
				trackMove.setDesiredRotationalVelocity(desired, 60, 270);
			}
		}
		if (gameInput.fireButton().isPressed()) {
			player.shootBullet(world);
		}
	}
}
