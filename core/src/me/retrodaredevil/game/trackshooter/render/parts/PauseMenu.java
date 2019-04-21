package me.retrodaredevil.game.trackshooter.render.parts;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.viewport.ExtendViewport;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import me.retrodaredevil.game.trackshooter.InputFocusable;
import me.retrodaredevil.game.trackshooter.Updateable;
import me.retrodaredevil.game.trackshooter.input.GameInput;
import me.retrodaredevil.game.trackshooter.render.RenderObject;
import me.retrodaredevil.game.trackshooter.render.RenderParts;
import me.retrodaredevil.game.trackshooter.render.Renderable;
import me.retrodaredevil.game.trackshooter.render.components.RenderComponent;
import me.retrodaredevil.game.trackshooter.render.selection.SelectionMenuRenderComponent;
import me.retrodaredevil.game.trackshooter.render.selection.options.providers.MultiActorOptionProvider;
import me.retrodaredevil.game.trackshooter.render.selection.tables.PlainTable;
import me.retrodaredevil.game.trackshooter.util.Constants;

public class PauseMenu implements Updateable, Renderable, InputFocusable, CloseableMenu {

	private final List<GameInput> gameInputs;
	private final RenderParts renderParts;
	private final ExitGameAction exitGameAction;
	private final Stage stage;
	private final SelectionMenuRenderComponent renderComponent;

	private final Button resumeButton;
	private boolean wasResumeDown = false;
	private final Button optionsButton;
	private final TextButton exitButton;
	private boolean wasExitDown = false;

	private boolean open = false;

	/** The time in system milliseconds when the exit button was first pressed*/
	private Long exitPressedTime = null;

	public PauseMenu(List<GameInput> gameInputs, RenderObject renderObject, RenderParts renderParts, ExitGameAction exitGameAction){
		this.gameInputs = gameInputs;
		this.renderParts = renderParts;
		this.exitGameAction = exitGameAction;
		stage = new Stage(new ExtendViewport(640, 640), renderObject.getBatch());
		renderComponent = new SelectionMenuRenderComponent(
				renderObject, 0, gameInputs.get(0), new PlainTable(),
				Collections.singleton(new MultiActorOptionProvider(
						Constants.BUTTON_SIZE,
						resumeButton = new TextButton("resume", renderObject.getUISkin()),
						optionsButton = new TextButton("options", renderObject.getUISkin()),
						exitButton = new TextButton("exit game", renderObject.getUISkin())
				)),
				() -> {}
		);
	}
	public void setControllerAndOpen(int playerIndex, GameInput menuController){
		if(menuController == null){
			System.err.println("Setting to a null menuController for the pause menu!");
		}
		renderComponent.setMenuController(playerIndex, menuController);
		open = true;
	}
	@Override
	public void closeMenu(){
		open = false;
		renderComponent.setMenuController(null, null);
		exitPressedTime = null;
		renderParts.getOptionsMenu().closeMenu();
	}
	@Override
	public boolean isMenuOpen(){
		return open;
	}

	@Override
	public void update(float delta) {
		if(renderParts.getOptionsMenu().isMenuOpen()){ // wait for options menu to be closed
			renderParts.getOverlay().setPauseVisible(false);
			renderComponent.clearTable(); // clear so the hit boxes of the thing in the table don't get in the way
			return;
		}
		{
			int i = 0;
			for (GameInput input : gameInputs) {
				if (input.getPauseButton().isPressed()) {
					toggle(i, input);
					break;
				}
				i++;
			}
		}
		renderParts.getOverlay().setPauseVisible(Gdx.input.isPeripheralAvailable(Input.Peripheral.MultitouchScreen));
		if(renderParts.getOverlay().isPausePressed()){
			toggle(0, gameInputs.get(0));
		}
		if(!isMenuOpen()){
			return;
		}
		final long time = System.currentTimeMillis();
		if(isExitButtonConfirmation(time)){
			exitButton.setText("exit (confirm)");
		} else {
			exitButton.setText("exit");
		}

		final boolean exitDown = exitButton.isPressed();
		if(!exitDown && wasExitDown){ // released
			exitPress(time);
		}
		wasExitDown = exitDown;

		final boolean resumeDown = resumeButton.isPressed();
		if(!resumeDown && wasResumeDown){ // released
			closeMenu();
		}
		wasResumeDown = resumeDown;

		if(optionsButton.isPressed()){
			final GameInput gameInput = renderComponent.getMenuController();
			final int playerIndex = renderComponent.getPlayerIndex();
			renderParts.getOptionsMenu().setToController(playerIndex, gameInput, playerIndex, gameInput);
		}
	}
	private void toggle(int playerIndex, GameInput controller){
		if(isMenuOpen()){
			closeMenu();
		} else {
			setControllerAndOpen(playerIndex, controller);
		}
	}

	private void exitPress(final long time){
		if(isExitButtonConfirmation(time)){
			exitGameAction.exitGame();
			return;
		}
		exitPressedTime = time;
	}
	private boolean isExitButtonConfirmation(final long time){
		return exitPressedTime != null && exitPressedTime + 3000 > time;
	}


	@Override
	public RenderComponent getRenderComponent() {
		// TODO It might be better to have a table and remove that from the stage instead of not rendering...
		// the stage/this render component so the stage can get input events. That would be extra work
		// of maintaining a table, but it would make sure our stage is consistent with input events.
		if(!isMenuOpen() || renderParts.getOptionsMenu().isMenuOpen()){
			return null;
		}
		return renderComponent;
	}

	@Override
	public Stage getPreferredStage() {
		return stage;
	}

	@Override
	public boolean isWantsToFocus() {
		return isMenuOpen();
	}

	@Override
	public int getFocusPriority() {
		return 1;
	}

	@Override
	public Collection<? extends InputProcessor> getInputProcessorsToFocus() {
		return Collections.singleton(stage);
	}
	public interface ExitGameAction {
		void exitGame();
	}
}
