package me.retrodaredevil.game.trackshooter;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import me.retrodaredevil.game.input.GameInput;
import me.retrodaredevil.game.trackshooter.overlay.Overlay;
import me.retrodaredevil.game.trackshooter.render.RenderComponent;
import me.retrodaredevil.game.trackshooter.util.Constants;
import me.retrodaredevil.game.trackshooter.util.RenderUtil;

public class StartScreen extends ScreenAdapter {
	private final GameInput gameInput;
	private final Overlay overlay;
	private final Batch batch;
	private boolean start;

	private final Stage uiStage;
//	private final Table table;
//	private final Button startButton;
	public StartScreen(GameInput gameInput, Overlay overlay, Batch batch){
		this.gameInput = gameInput;
		this.overlay = overlay;
		this.batch = batch;
		this.uiStage = new Stage(new ScreenViewport(), batch);

//		table = new Table(); // TODO complete table code
//		table.center();
//		Skin skin = new Skin();
//        TextureAtlas buttonAtlas = new TextureAtlas(Gdx.files.internal("buttons/buttons.pack"));
//        skin.addRegions(buttonAtlas);
//		TextButton.TextButtonStyle style = new TextButton.TextButtonStyle();
//		style.font = new BitmapFont();
//		style.up = skin.getDrawable("up-button");
//		style.down = skin.getDrawable("down-button");
//		style.checked = skin.getDrawable("checked-button");
//		startButton = new TextButton("start", style); // do stuff with startButton.getStyle()
//		table.add(startButton);
//
//		uiStage.addActor(table);
	}
	@Override
	public void render(float delta) {
        RenderUtil.clearScreen(Constants.BACKGROUND_COLOR);
		if(gameInput.startButton().isPressed()){
			start = true;
		}

		uiStage.act(delta);

		RenderComponent overlayRender = overlay.getRenderComponent();
		if(overlayRender != null){
			Stage stage = overlay.getStage();
			overlayRender.render(delta, stage);

			batch.begin();
			RenderUtil.drawStage(batch, uiStage);
			RenderUtil.drawStage(batch, stage);
			batch.end();
		} else {
			uiStage.draw();
		}
//		Gdx.app.debug("dpad x", "" + Controllers.getControllers().first().getPov(0));
//		Gdx.app.debug("magnitude", "" + gameInput.mainJoystick().getMagnitude()
//				* (gameInput.mainJoystick().getJoystickType().shouldScale() ? SimpleJoystickPart.getScaled(gameInput.mainJoystick().getAngle()) : 1));
		Gdx.app.debug("correct magnitude", "" + gameInput.mainJoystick().getCorrectMagnitude());
		Gdx.app.debug("is dead", "" + gameInput.mainJoystick().isDeadzone());
		System.out.println();
	}

	@Override
	public void resize(int width, int height) {
		overlay.getStage().getViewport().update(width, height,true);
	}
	public boolean isReadyToStart(){
		return start;
	}
}
