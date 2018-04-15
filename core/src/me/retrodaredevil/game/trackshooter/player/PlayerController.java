package me.retrodaredevil.game.trackshooter.player;

import me.retrodaredevil.game.trackshooter.EntityController;
import me.retrodaredevil.game.trackshooter.MoveComponent;
import me.retrodaredevil.game.trackshooter.OnTrackMoveComponent;
import me.retrodaredevil.game.trackshooter.world.World;
import me.retrodaredevil.input.JoystickPart;
import me.retrodaredevil.input.StandardControllerInput;

public class PlayerController implements EntityController{

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
			JoystickPart joy = controller.leftJoy();
			if(!joy.isXDeadzone()) {
				trackMove.setVelocity((float) (controller.leftJoy().getX() * 4f));
			} else {
				trackMove.setVelocity(0);
			}
		}
	}
}
