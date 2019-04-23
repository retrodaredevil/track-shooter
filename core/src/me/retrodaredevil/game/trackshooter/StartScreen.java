package me.retrodaredevil.game.trackshooter;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.viewport.FitViewport;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import me.retrodaredevil.game.trackshooter.input.GameInput;
import me.retrodaredevil.game.trackshooter.render.ComponentRenderable;
import me.retrodaredevil.game.trackshooter.render.RenderObject;
import me.retrodaredevil.game.trackshooter.render.RenderParts;
import me.retrodaredevil.game.trackshooter.render.Renderable;
import me.retrodaredevil.game.trackshooter.render.Renderer;
import me.retrodaredevil.game.trackshooter.render.components.RenderComponent;
import me.retrodaredevil.game.trackshooter.render.selection.SelectionMenuRenderComponent;
import me.retrodaredevil.game.trackshooter.render.selection.options.providers.MultiActorOptionProvider;
import me.retrodaredevil.game.trackshooter.render.selection.tables.PlainTable;
import me.retrodaredevil.game.trackshooter.util.Constants;

public class StartScreen extends ScreenAdapter implements UsableScreen{
	private static final float DEMO_GAME_INIT_IDLE = 45;

	private final List<GameInput> gameInputs;
	private final GameInput gameInput;
	private final int gameInputPlayerIndex;
	private final RenderParts renderParts;
	private final RenderObject renderObject;
	private UsableScreen nextScreen = null;

	private final Stage uiStage;
	private final Renderable menuRenderable;
	private final Renderable tipsRenderable;

	private final Button startButton;
	private final Button optionsButton;
	private final Button creditsButton;
	private boolean optionsDown = false;
	private float idleTime = 0;

	public StartScreen(List<GameInput> gameInputs, RenderObject renderObject, RenderParts renderParts){
		this.gameInputs = Collections.unmodifiableList(new ArrayList<>(gameInputs)); // copy gameInputs for safe keeping
		this.gameInputPlayerIndex = 0;
		this.gameInput = gameInputs.get(gameInputPlayerIndex);
		this.renderParts = Objects.requireNonNull(renderParts);
		this.renderObject = Objects.requireNonNull(renderObject);
		this.uiStage = new Stage(new FitViewport(640, 640), renderObject.getBatch());

		final TextButton.TextButtonStyle style = renderObject.getUISkin().get(TextButton.TextButtonStyle.class);
		startButton = new TextButton("start", style); // do stuff with getStartButton.getStyle()
		optionsButton = new TextButton("options", style);
		creditsButton = new TextButton("info", style);

		// this is initialized after each button because it uses them
		this.menuRenderable = new ComponentRenderable(new SelectionMenuRenderComponent(
				renderObject,
                gameInputPlayerIndex,
				gameInput,
				new PlainTable(),
				Collections.singleton(new MultiActorOptionProvider(Constants.BUTTON_SIZE, startButton, optionsButton, creditsButton)),
				() -> {} // do nothing on back button
		));
		this.tipsRenderable = new ComponentRenderable(new TipsRenderComponent());

	}
	private Renderer createRenderer(){
		return new Renderer(renderObject.getBatch(), uiStage)
				.addRenderable(renderParts.getBackground())
				.addMainStage()
				.addRenderable(tipsRenderable)
				.addRenderable(renderParts.getOptionsMenu().isMenuOpen() ? renderParts.getOptionsMenu() : menuRenderable)
				.addRenderable(renderParts.getOverlay());
	}
	@Override
	public void render(float delta) {
		idleTime += delta;
		if(gameInput.getFireButton().isPressed() || gameInput.getEnterButton().isPressed()
				|| gameInput.getMainJoystick().getMagnitude() > .5 || Gdx.input.justTouched()
				|| renderParts.getOptionsMenu().isMenuOpen()){
			idleTime = 0;
		}

		if(gameInput.getStartButton().isPressed() || startButton.isPressed()){
			normalGame();
			return;
		}
		if(!renderParts.getOptionsMenu().isMenuOpen() && (gameInput.getBackButton().isPressed() || idleTime > DEMO_GAME_INIT_IDLE)){
			demoGame();
			return;
		}
		if(creditsButton.isPressed()){
			nextScreen = new CreditsScreen(gameInputs, renderObject, renderParts);
			return;
		}
		if(optionsDown && !optionsButton.isPressed()){ // just released options button
			renderParts.getOptionsMenu().setToController(gameInputPlayerIndex, gameInput, gameInputPlayerIndex, gameInput);
		}
		optionsDown = optionsButton.isPressed();
		renderParts.getOverlay().setPauseVisible(false);
		new InputFocuser()
				.add(new InputFocuser(0).addParallel(renderParts.getTouchpadRenderer()).addParallel(uiStage))
				.add(renderParts.getOptionsMenu()) // may or may not get focus
				.giveFocus(uiStage, renderParts.getInputMultiplexer());


		createRenderer().render(delta);
	}
	private void normalGame(){
		if(nextScreen == null) {
			nextScreen = new GameScreen(gameInputs, renderObject, renderParts, GameScreen.GameType.NORMAL);
		}
	}
	private void demoGame(){
		if(nextScreen == null) {
			nextScreen = new GameScreen(gameInputs, renderObject, renderParts, GameScreen.GameType.DEMO_AI);
		}
	}

	@Override
	public void resize(int width, int height) {
		uiStage.getViewport().update(width, height, true);
		renderParts.resize(width, height);
	}

	@Override
	public void dispose() {
		super.dispose();
		uiStage.dispose(); // we created uiStage so dispose it
	}

	@Override
	public boolean isScreenDone() {
		return nextScreen != null && !startButton.isPressed() && !creditsButton.isPressed();
	}

	@Override
	public UsableScreen createNextScreen() {
		return nextScreen;
	}


	private static final String[] TIPS = new String[] {
			"Change your controls in options!",
			"There will only be 4 shots at a time!",
			"If you save Mr. Spaceship from fire, you'll get bonus points!",
			"If you wait long enough, the AI will start playing the game!",
			"Don't turn your back on the snake!",
			"Keep shooting at the Sharks to make them spin longer!",
			"Shoot the starfish once to change its direction!",
			"Open Source!"
	};
	private class TipsRenderComponent implements RenderComponent {
		private final Group group = new Table(){{setFillParent(true);}};

		private TipsRenderComponent() {
			Table table = new Table(renderObject.getMainSkin());
			table.setFillParent(true);
			final String tip = TIPS[(int) (Math.random() * TIPS.length)];
			final Label label = new Label(tip, renderObject.getMainSkin(), "game_label", "tips");
			label.setFontScale(Math.min(34.0f / tip.length(), 1));
			table.add(label);
			table.center().bottom().padBottom(120);

			group.addActor(table);
		}

		@Override
		public void render(float delta, Stage stage) {
			stage.addActor(group);
		}

		@Override
		public void dispose() {
			group.remove();
		}
	}
}
