package me.retrodaredevil.game.trackshooter;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Disposable;

import me.retrodaredevil.game.trackshooter.render.RenderComponent;

// The reason for adding Disposable as a super interface was that a Renderable may have multiple
// render components that it switches between and calling getRenderComponent().disposeRenderComponent() would only disposeRenderComponent
// of one of them. By adding Disposable, there is more control
public interface Renderable {
	/**
	 * When this method is called, it is usually to call the return value's render (assuming null wasn't returned)
	 *
	 * @return The RenderComponent for this object or null if there is not currently one
	 */
	RenderComponent getRenderComponent();

	/**
	 * Renders the RenderComponent and uses the preferred stage if that isn't null
	 * NOTE: If this is overridden, it should be for a good reason and using {@link #getRenderComponent()}
	 * should have little difference if any.
	 * @param delta The delta time
	 * @param mainStage The main stage
	 */
	default void autoRender(float delta, Stage mainStage, boolean canUsePreferred){
		RenderComponent renderComponent = getRenderComponent();
		if(renderComponent != null){
			Stage preferredStage = getPreferredStage();
			renderComponent.render(delta, preferredStage != null && canUsePreferred ? preferredStage : mainStage);
		}
	}

	/**
	 * Should be called instead of using getRenderComponent().dispose()
	 * <br/>
	 * Also note that you are allowed to call this twice as this should only disposeRenderComponent of the render component
	 * if it has any.
	 * <br/>
	 * HOWEVER, If this for whatever reason implements Disposable, then calling dispose() should call this method as well.
	 */
	default void disposeRenderComponent(){
		RenderComponent renderComponent = getRenderComponent();
		if(renderComponent != null){
			renderComponent.dispose();
		}
	}

	/**
	 * NOTE: If this doesn't return null, it is likely that this should implement {@link Disposable}
	 * @return The preferred stage to be rendered on or null to use the default stage
	 */
	default Stage getPreferredStage(){ // preferred
		return null;
	}
	default void resize(int width, int height){
		Stage stage = getPreferredStage();
		if(stage != null){
			stage.getViewport().update(width, height, true);
		}
	}
}
