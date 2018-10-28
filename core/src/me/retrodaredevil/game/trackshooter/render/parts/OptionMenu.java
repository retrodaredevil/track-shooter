package me.retrodaredevil.game.trackshooter.render.parts;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.viewport.ExtendViewport;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import me.retrodaredevil.controller.options.ConfigurableControllerPart;
import me.retrodaredevil.game.trackshooter.InputFocusable;
import me.retrodaredevil.game.trackshooter.input.GameInput;
import me.retrodaredevil.game.trackshooter.render.RenderObject;
import me.retrodaredevil.game.trackshooter.render.Renderable;
import me.retrodaredevil.game.trackshooter.render.components.RenderComponent;
import me.retrodaredevil.game.trackshooter.render.selection.options.ButtonExitMenuSingleOption;
import me.retrodaredevil.game.trackshooter.render.selection.options.ButtonSingleOption;
import me.retrodaredevil.game.trackshooter.render.selection.SelectionMenuRenderComponent;
import me.retrodaredevil.game.trackshooter.render.selection.options.GroupedSelectionSingleOption;
import me.retrodaredevil.game.trackshooter.render.selection.options.providers.BasicOptionProvider;
import me.retrodaredevil.game.trackshooter.render.selection.options.providers.ConfigurableObjectOptionProvider;
import me.retrodaredevil.game.trackshooter.render.selection.options.providers.PageControlOptionVisibility;
import me.retrodaredevil.game.trackshooter.render.selection.tables.DialogTable;
import me.retrodaredevil.game.trackshooter.save.SaveObject;
import me.retrodaredevil.game.trackshooter.util.Constants;
import me.retrodaredevil.game.trackshooter.util.Size;

public class OptionMenu implements Renderable, InputFocusable, CloseableMenu {
	private final RenderObject renderObject;
	private final SaveObject saveObject;
	private final Stage preferredStage;
	private SelectionMenuRenderComponent renderComponent = null;
	private ConfigurableControllerPart currentController = null;

	public OptionMenu(RenderObject renderObject, SaveObject saveObject) {
		this.renderObject = renderObject;
		this.saveObject = saveObject;

		this.preferredStage = new Stage(new ExtendViewport(480, 480), renderObject.getBatch());
	}

	@Override
	public RenderComponent getRenderComponent() {
		return renderComponent;
	}

	/**
	 *
	 * @param configController The Controller to config
	 * @param menuController The controller to control the options menu
	 */
	public void setToController(int configControllerPlayerIndex, ConfigurableControllerPart configController, int menuControllerPlayerIndex, GameInput menuController){
		if (renderComponent != null) {
			if(currentController == configController){
				System.err.println("it's the same!");
				return; // don't do anything, it's the same
			}
			renderComponent.dispose();
		}
		if(configController == null){
			renderComponent = null;
			return;
		}
		/*
        If you get an index out of bounds exception, it probably has something to do with the scroll pane
		 */
		final PageControlOptionVisibility pageControlOptionVisibility = new PageControlOptionVisibility();
		final Size topSize = Constants.OPTIONS_MENU_TOP_BUTTONS_SIZE;
		final Size size = Constants.OPTIONS_MENU_BOTTOM_BUTTONS_SIZE;
		renderComponent = new SelectionMenuRenderComponent(renderObject, menuControllerPlayerIndex, menuController,
				new DialogTable("Options", renderObject),
				Collections.singletonList(new BasicOptionProvider(
						new GroupedSelectionSingleOption(Size.widthOnly(400), true, Arrays.asList(new BasicOptionProvider(
								new ButtonSingleOption(new TextButton("main", renderObject.getUISkin(), "small"), topSize,
										requestingActions -> pageControlOptionVisibility.setPage(PageControlOptionVisibility.Page.MAIN)),
								new ButtonSingleOption(new TextButton("move", renderObject.getUISkin(), "small"), topSize,
										requestingActions -> pageControlOptionVisibility.setPage(PageControlOptionVisibility.Page.MOVEMENT)),
								new ButtonSingleOption(new TextButton("rotate", renderObject.getUISkin(), "small"), topSize,
										requestingActions -> pageControlOptionVisibility.setPage(PageControlOptionVisibility.Page.ROTATION)),
								new ButtonSingleOption(new TextButton("shoot", renderObject.getUISkin(), "small"), topSize,
										requestingActions -> pageControlOptionVisibility.setPage(PageControlOptionVisibility.Page.SHOOTING)),
								new ButtonSingleOption(new TextButton("misc", renderObject.getUISkin(), "small"), topSize,
										requestingActions -> pageControlOptionVisibility.setPage(PageControlOptionVisibility.Page.MISC)),
								new ButtonSingleOption(new TextButton("all", renderObject.getUISkin(), "small"), topSize,
										requestingActions -> pageControlOptionVisibility.setPage(PageControlOptionVisibility.Page.ALL))
						))),
						new GroupedSelectionSingleOption(Size.NONE, false, Arrays.asList(
								new ConfigurableObjectOptionProvider(Size.widthOnly(400), configControllerPlayerIndex, menuController, renderObject, saveObject, pageControlOptionVisibility))
						),
						new GroupedSelectionSingleOption(Size.widthOnly(400), true, Arrays.asList(new BasicOptionProvider(
								new ButtonExitMenuSingleOption(new TextButton("back", renderObject.getUISkin(), "small"), size),
								new ButtonSingleOption(new TextButton("reset", renderObject.getUISkin(), "small"), size,
										requestingActions -> {
											if(renderComponent != null) {
												renderComponent.resetAll();
											}
										})
						)))
				)),
				this::closeMenu);
		currentController = menuController; // TODO I believe I originally set this up to check for errors, but this may be unnecessary
	}
	@Override
	public void closeMenu(){
		setToController(-1, null, -1, null);
	}

	@Override
	public boolean isMenuOpen() {
		return renderComponent != null;
	}


	@Override
	public Stage getPreferredStage() {
		return preferredStage;
	}



	@Override
	public Collection<? extends InputProcessor> getInputProcessorsToFocus(Stage mainStage) {
		return Collections.singleton(preferredStage);
	}

	@Override
	public boolean isWantsToFocus() {
		return renderComponent != null;
	}

	@Override
	public int getFocusPriority() {
		return 5;
	}

}
