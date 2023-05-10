package me.retrodaredevil.game.trackshooter;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.viewport.FitViewport;

import java.util.List;

import me.retrodaredevil.game.trackshooter.input.GameInput;
import me.retrodaredevil.game.trackshooter.render.RenderObject;
import me.retrodaredevil.game.trackshooter.render.RenderParts;
import me.retrodaredevil.game.trackshooter.render.Renderer;
import me.retrodaredevil.game.trackshooter.sound.VolumeControl;
import me.retrodaredevil.game.trackshooter.util.Constants;

public class CreditsScreen extends ScreenAdapter implements UsableScreen {

	private final List<GameInput> gameInputs;
	private final GameInput gameInput;
	private final RenderObject renderObject;
	private final RenderParts renderParts;
	private final AccountObject accountObject;
	private final VolumeControl volumeControl;
	private final Stage stage;
	private final Button backButton;
	private boolean done = false;

	public CreditsScreen(List<GameInput> gameInputs, RenderObject renderObject, RenderParts renderParts, AccountObject accountObject, VolumeControl volumeControl){
		this.gameInputs = gameInputs;
		this.gameInput = gameInputs.get(0);
		this.renderObject = renderObject;
		this.renderParts = renderParts;
		this.accountObject = accountObject;
		this.volumeControl = volumeControl;
		stage = new Stage(new FitViewport(640, 640), renderObject.getBatch());
		Table table = new Table(renderObject.getUISkin());
		table.setFillParent(true);
		stage.addActor(table);

		table.add("Game made by Lavender Shannon").center().row();
		table.add("Game made using LibGDX").center().row();
		table.add("This game is still in beta, please report any bugs you find").center().row();
		table.add("and request features to retrodaredevil@gmail.com").center().row();
		table.add("").center().row();
		table.add("Default (mobile) control scheme:").center().row();
		table.add("Use the joystick on the left to move").center().row();
		table.add("Use the Y axis on the right side of the screen to rotate").center().row();
		table.add("Release the right side of the screen to shoot").center().row();
		table.add("The back button is used to exit menus and pause the game").center().row();
		table.add("").center().row();
		table.add("If you get a power-up, shake your device to use it!").center().row();
		table.add("").center().row();
		table.add("The control scheme can be changed in options").center().row();
		table.add("Try out the gyro control scheme if your phone supports it!").center().row();
		Constants.START_SCREEN_BUTTON_SIZE.apply(table.add(backButton = new TextButton("back", renderObject.getUISkin(), "small")));
	}
	private Renderer createRenderer(){
		return new Renderer(renderObject.getBatch(), stage)
				.addRenderable(renderParts.getBackground())
				.addRenderable(renderParts.getOverlay())
				.addMainStage(); // chances are, the main stage has already been added, but add it anyway
	}

	@Override
	public void render(float delta) {
		createRenderer().render(delta);

		Gdx.input.setInputProcessor(stage);
		if(gameInput.getBackButton().isJustPressed() || backButton.isPressed()){
			done = true;
		}
	}

	@Override
	public void resize(int width, int height) {
		stage.getViewport().update(width, height, true);
		renderParts.resize(width, height);
	}

	@Override
	public boolean isScreenDone() {
		return done;
	}

	@Override
	public UsableScreen createNextScreen() {
		return new StartScreen(gameInputs, renderObject, renderParts, accountObject, volumeControl);
	}
}
