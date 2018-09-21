package me.retrodaredevil.game.trackshooter;

import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.viewport.FitViewport;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import me.retrodaredevil.game.input.GameInput;
import me.retrodaredevil.game.trackshooter.render.RenderObject;
import me.retrodaredevil.game.trackshooter.render.RenderParts;
import me.retrodaredevil.game.trackshooter.render.Renderable;
import me.retrodaredevil.game.trackshooter.render.Renderer;
import me.retrodaredevil.game.trackshooter.render.components.RenderComponent;
import me.retrodaredevil.game.trackshooter.render.selection.PlainActorSingleOption;
import me.retrodaredevil.game.trackshooter.render.selection.SelectionMenuRenderComponent;
import me.retrodaredevil.game.trackshooter.render.selection.SingleOption;

public class StartScreen extends ScreenAdapter {
	private static final int BUTTON_WIDTH = 220;
	private static final int BUTTON_HEIGHT = 60;
	private final GameInput gameInput;
	private final RenderParts renderParts;
	private final RenderObject renderObject;
	private boolean start;

	private final Stage uiStage;
	private final Renderable menuRenderable;

	private final Button startButton;
	private final Button optionsButton;
	private boolean optionsDown = false;

	public StartScreen(GameInput gameInput, RenderObject renderObject, RenderParts renderParts){
		this.gameInput = gameInput;
		this.renderParts = renderParts;
		this.renderObject = renderObject;
		this.uiStage = new Stage(new FitViewport(640, 640), renderObject.getBatch());

		TextButton.TextButtonStyle style = renderObject.getUISkin().get(TextButton.TextButtonStyle.class);
		startButton = new TextButton("start", style); // do stuff with getStartButton.getStyle()
		optionsButton = new TextButton("options", style);

		this.menuRenderable = new StartScreenRenderable();

	}
	@Override
	public void render(float delta) {
		if(optionsDown && !optionsButton.isPressed()){ // just released options button
			renderParts.getOptionsMenu().setToController(gameInput, gameInput);
		}
		optionsDown = optionsButton.isPressed();
		InputFocuser focuser = new InputFocuser(renderParts.getInputMultiplexer());
		focuser.addInputFocus(renderParts.getOptionsMenu());
//		focuser.addParallelInputProcessor(inputCancellerTester);
		focuser.giveFocus(uiStage);

		Renderer renderer = new Renderer(renderObject.getBatch(), uiStage);
		renderer.addRenderable(renderParts.getBackground());
		renderer.addMainStage();
		renderer.addRenderable(renderParts.getOptionsMenu().isOpen() ? renderParts.getOptionsMenu() : menuRenderable);
		renderer.addRenderable(renderParts.getOverlay());

		renderer.render(delta);

//		System.out.println(gameInput.getActivatePowerup().isDown());
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

	public boolean isReadyToStart(){
		return start && !startButton.isPressed();
	}

	class StartScreenRenderable implements Renderable {
		private final RenderComponent renderComponent = new StartScreenMenuRenderComponent();

		@Override
		public RenderComponent getRenderComponent() {
			return renderComponent;
		}

	}
	class StartScreenMenuRenderComponent extends SelectionMenuRenderComponent{
		private final Table table = new Table(){{
			setFillParent(true);
			center();
		}};
		private final Map<Actor, SingleOption> actorSingleOptionMap = new HashMap<>();

		StartScreenMenuRenderComponent() {
			super(StartScreen.this.renderObject, gameInput);
		}

		@Override
		public void render(float delta, Stage stage) {

			if(gameInput.getStartButton().isPressed() || startButton.isPressed()){
				start = true;
			}
			stage.addActor(table);

			Collection<? extends SingleOption> options = getOptions();
			for(Iterator<SingleOption> it = actorSingleOptionMap.values().iterator(); it.hasNext(); ){
				SingleOption singleOption = it.next();
				if(!options.contains(singleOption)){
					it.remove();
				}
			}
			super.render(delta, stage);
		}

		@Override
		protected Table getContentTable() {
			return table;
		}

		@Override
		protected Collection<? extends SingleOption> getOptionsToAdd() {
			List<SingleOption> r = new ArrayList<>();
			tryAddActorAsSingleOption(startButton, r);
			tryAddActorAsSingleOption(optionsButton, r);
			return r;
		}
		private void tryAddActorAsSingleOption(Actor actor, Collection<? super SingleOption> optionCollection){
			if(actorSingleOptionMap.get(actor) == null){
				SingleOption option = new PlainActorSingleOption(actor, BUTTON_WIDTH, BUTTON_HEIGHT);
				optionCollection.add(option);
				actorSingleOptionMap.put(actor, option);
			}
		}

		@Override
		protected boolean shouldKeep(SingleOption singleOption) {
			return true;
		}
	}
}
