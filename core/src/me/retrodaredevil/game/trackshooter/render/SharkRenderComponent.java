package me.retrodaredevil.game.trackshooter.render;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import me.retrodaredevil.game.trackshooter.entity.Entity;
import me.retrodaredevil.game.trackshooter.entity.movement.MoveComponent;
import me.retrodaredevil.game.trackshooter.entity.movement.RotationalVelocityMoveComponent;
import me.retrodaredevil.game.trackshooter.entity.movement.SmoothTravelMoveComponent;
import me.retrodaredevil.game.trackshooter.entity.movement.VelocityTargetPositionMoveComponent;

public class SharkRenderComponent extends ImageRenderComponent {
	private static final int LEFT_FRAME = 2, RIGHT_FRAME = 0, STRAIGHT_FRAME = 1;

	private final TextureRegionDrawable[] frames;


	/**
	 * Note this assumes that the image is facing up and it rotates it accordingly.
	 *
	 * @param frames The array of frames to use that should have a size of 3
	 * @param entity The entity to follow
	 * @param width The width of the image
	 * @param height The height of the image
	 */
	public SharkRenderComponent(TextureRegionDrawable[] frames, Entity entity, float width, float height){
		super(new Image(), entity, width, height);
//		this.frames = new TextureRegionDrawable[frames.length];
//		for(int i = 0; i < frames.length; i++){
//			this.frames[i] = new TextureRegionDrawable(frames[i]);
//		}
		this.frames = frames;

	}
	@Override
	public void render(float delta, Stage stage) {
		MoveComponent moveComponent = entity.getMoveComponent();
		boolean animate = false;
		if(moveComponent instanceof VelocityTargetPositionMoveComponent){
			VelocityTargetPositionMoveComponent targetMove = (VelocityTargetPositionMoveComponent) moveComponent;
//			float change = targetMove.getRotationalChange();
			float change = targetMove.getRotationalChange();
			if(Math.abs(change) < 40){
				animate = true;
			} else if(change > 0){
				image.setDrawable(frames[LEFT_FRAME]);
			} else {
				image.setDrawable(frames[RIGHT_FRAME]);
			}
		} else {
			animate = true;
//			image.setDrawable(frames[STRAIGHT_FRAME]);
		}
		if(animate){
			final long FULL_CYCLE = 800;
			int frame = (int) ((System.currentTimeMillis() % FULL_CYCLE) / (FULL_CYCLE / 4L));
			if(frame == 3){
				frame = 1;
			}
			image.setDrawable(frames[frame]);
		}
		super.render(delta, stage);

	}

}
