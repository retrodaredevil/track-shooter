package me.retrodaredevil.game.trackshooter;

import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FitViewport;

import java.util.List;

import me.retrodaredevil.game.trackshooter.input.GameInput;
import me.retrodaredevil.game.trackshooter.render.RenderObject;
import me.retrodaredevil.game.trackshooter.render.RenderParts;
import me.retrodaredevil.game.trackshooter.render.Renderer;

public class CreditsScreen extends ScreenAdapter implements UsableScreen {

	private final List<GameInput> gameInputs;
	private final GameInput gameInput;
	private final RenderObject renderObject;
	private final RenderParts renderParts;
	private final Stage stage;
	private boolean done = false;

	public CreditsScreen(List<GameInput> gameInputs, RenderObject renderObject, RenderParts renderParts){
		this.gameInputs = gameInputs;
		this.gameInput = gameInputs.get(0);
		this.renderObject = renderObject;
		this.renderParts = renderParts;
		this.stage = new Stage(new FitViewport(640, 640), renderObject.getBatch());
		Table table = new Table(renderObject.getUISkin());
		table.setFillParent(true);
		stage.addActor(table);

		table.add("Game made by Joshua Shannon").center().row();
		table.add("Game made using LibGDX").center().row();
		table.add("This game is still in beta, please report any bugs you find").center().row();
		table.add("and requests features that are not currently in the game").center().row();
		table.add("").center().row();
		table.add("Default (mobile) control scheme:").center().row();
		table.add("Use the joystick on the left to move").center().row();
		table.add("Use the Y axis on the right side of the screen to rotate").center().row();
		table.add("Release the right side of the screen to shoot").center().row();
		table.add("The back button is used to exit menus and pause the game").center().row();
		table.add("").center().row();
		table.add("The control scheme can be changed in options").center().row();
	}
	private Renderer createRenderer(){
		Renderer r = new Renderer(renderObject.getBatch(), stage);
		r.addRenderable(renderParts.getBackground());
		r.addRenderable(renderParts.getOverlay());
		r.addMainStage(); // chances are, the main stage has already been added
		return r;
	}

	@Override
	public void render(float delta) {
		createRenderer().render(delta);

		if(gameInput.getBackButton().isPressed()){
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
		return new StartScreen(gameInputs, renderObject, renderParts);
	}
}
