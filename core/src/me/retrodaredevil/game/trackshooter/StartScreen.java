package me.retrodaredevil.game.trackshooter;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import me.retrodaredevil.game.input.GameInput;
import me.retrodaredevil.game.trackshooter.overlay.Overlay;
import me.retrodaredevil.game.trackshooter.render.RenderComponent;
import me.retrodaredevil.game.trackshooter.util.Constants;
import me.retrodaredevil.game.trackshooter.util.RenderUtil;

public class StartScreen extends ScreenAdapter {
	private final GameInput gameInput;
	private final Overlay overlay;
	private final RenderObject renderObject;
	private boolean start;

	private final Stage uiStage;

	private final Button startButton;

	public StartScreen(GameInput gameInput, Overlay overlay, RenderObject renderObject){
		this.gameInput = gameInput;
		this.overlay = overlay;
		this.renderObject = renderObject;
		this.uiStage = new Stage(new ScreenViewport(), renderObject.getBatch());

		Table table = new Table();
		table.setFillParent(true);
		table.center();
		TextButton.TextButtonStyle style = renderObject.getUISkin().get(TextButton.TextButtonStyle.class);
		startButton = new TextButton("start", style); // do stuff with startButton.getStyle()
		startButton.setChecked(false);
		table.add(startButton).width(220);


		uiStage.addActor(table);
	}
	@Override
	public void render(float delta) {
		RenderUtil.clearScreen(renderObject.getMainSkin().getColor("background"));

		if(Gdx.input.getInputProcessor() != uiStage) {
			Gdx.input.setInputProcessor(uiStage);
		}
		uiStage.act(delta);
		if(gameInput.startButton().isPressed() || startButton.isPressed()){
			start = true;
		}


		RenderComponent overlayRender = overlay.getRenderComponent();
		if(overlayRender != null){
			Stage stage = overlay.getStage();
			overlayRender.render(delta, stage);
			stage.act();

			Batch batch = renderObject.getBatch();
			batch.begin();
			RenderUtil.drawStage(batch, uiStage);
			RenderUtil.drawStage(batch, stage);
			batch.end();
//			uiStage.draw();
//			stage.draw();
		} else {
			uiStage.draw();
		}
//		Gdx.app.debug("dpad x", "" + Controllers.getControllers().first().getPov(0));
//		Gdx.app.debug("magnitude", "" + gameInput.mainJoystick().getMagnitude()
//				* (gameInput.mainJoystick().getJoystickType().isInputSquare() ? SimpleJoystickPart.getScaled(gameInput.mainJoystick().getAngle()) : 1));

//		ControllerRumble rumble = gameInput.getRumble();
//		if(rumble != null && rumble.isConnected()){
//			double intensity = System.currentTimeMillis() % 1400 < 700 ? .9 : .3;
////			System.out.println("intensity: " + intensity);
//			rumble.rumble(100, intensity); // if this stops being called, it will end
////			rumble.rumble(1);
//		}
//		JoystickPart joy = gameInput.mainJoystick();
//		if(!joy.getJoystickType().isInputSquare()){
//			System.out.println("hardware correct joystick");
//			System.out.println("magnitude: " + joy.getMagnitude());
//			System.out.println("angle: " + joy.getAngle());
//			System.out.println("angle rad: " + joy.getAngleRadians());
//			System.out.println();
//		}
	}

	@Override
	public void resize(int width, int height) {
		overlay.getStage().getViewport().update(width, height,true);
	}
	public boolean isReadyToStart(){
		return start && !startButton.isPressed();
	}
}
