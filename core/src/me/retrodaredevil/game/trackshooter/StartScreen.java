package me.retrodaredevil.game.trackshooter;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.scenes.scene2d.Stage;

import me.retrodaredevil.game.input.GameInput;
import me.retrodaredevil.game.trackshooter.overlay.Overlay;
import me.retrodaredevil.game.trackshooter.render.RenderComponent;
import me.retrodaredevil.game.trackshooter.util.Constants;
import me.retrodaredevil.game.trackshooter.util.RenderUtil;

public class StartScreen extends ScreenAdapter{
	private final GameInput gameInput;
	private final Overlay overlay;
	private boolean start = false;
	public StartScreen(GameInput gameInput, Overlay overlay){
		this.gameInput = gameInput;
		this.overlay = overlay;
	}
	@Override
	public void render(float delta) {
        RenderUtil.clearScreen(Constants.BACKGROUND_COLOR);
		if(gameInput.startButton().isPressed()){
			start = true;
		}
		RenderComponent overlayRender = overlay.getRenderComponent();
		if(overlayRender != null){
			Stage stage = overlay.getStage();
			overlayRender.render(delta, stage);

			if(Constants.SHOULD_ACT) {
				stage.act(delta);
			}
			stage.draw();
		}
//		Gdx.app.debug("dpad x", "" + Controllers.getControllers().first().getPov(0));
		Gdx.app.debug("magnitude", "" + gameInput.mainJoystick().getMagnitude());
	}

	@Override
	public void resize(int width, int height) {
		overlay.getStage().getViewport().update(width, height,true);
	}
	public boolean isReadyToStart(){
		return start;
	}
}
