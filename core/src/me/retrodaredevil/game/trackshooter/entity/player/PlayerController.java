package me.retrodaredevil.game.trackshooter.entity.player;

import me.retrodaredevil.game.trackshooter.entity.EntityController;
import me.retrodaredevil.game.trackshooter.entity.movement.MoveComponent;
import me.retrodaredevil.game.trackshooter.entity.movement.OnTrackMoveComponent;
import me.retrodaredevil.game.trackshooter.world.World;
import me.retrodaredevil.input.JoystickPart;
import me.retrodaredevil.input.StandardControllerInput;

public class PlayerController implements EntityController{
	private static final float VELOCITY_PER_SECOND = 5f;
	private static final float ROTATE_PER_SECOND = 360;

	private Player player;
	private StandardControllerInput controller;
	public PlayerController(Player player, StandardControllerInput controller){
		this.player = player;
		this.controller = controller;
	}

	@Override
	public void update(float delta, World world) {
		MoveComponent move = player.getMoveComponent();
		if(move instanceof OnTrackMoveComponent){
			OnTrackMoveComponent trackMove = (OnTrackMoveComponent) move;
			JoystickPart movementJoy = controller.leftJoy();
			if(!movementJoy.isXDeadzone()) {
				trackMove.setVelocity((float) (controller.leftJoy().getX() * VELOCITY_PER_SECOND));
			} else {
				trackMove.setVelocity(0);
			}

			JoystickPart rotateJoy = controller.rightJoy();
			float desired = (float) (ROTATE_PER_SECOND * rotateJoy.getX());
			desired *= -1;
			if(rotateJoy.isDeadzone()){
				desired = 0;
			}
			trackMove.setDesiredRotationalVelocity(desired);
		}
		if (controller.rightTrigger().isPressed()) {
			player.shootBullet(world);
		}
	}
}
