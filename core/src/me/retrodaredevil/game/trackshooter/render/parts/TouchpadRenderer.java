package me.retrodaredevil.game.trackshooter.render.parts;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import java.util.Collection;
import java.util.Collections;

import me.retrodaredevil.game.input.UsableGameInput;
import me.retrodaredevil.game.trackshooter.InputFocusable;
import me.retrodaredevil.game.trackshooter.render.RenderObject;
import me.retrodaredevil.game.trackshooter.render.Renderable;
import me.retrodaredevil.game.trackshooter.render.components.RenderComponent;
import me.retrodaredevil.game.trackshooter.render.components.RenderComponentMultiplexer;

/**
 * As of right now, this is only set up to handle a single Touchpad. In the future, we may support
 * multiple touchpads on one screen.
 */
public class TouchpadRenderer implements Renderable, InputFocusable {
	private final Stage stage;
	private final RenderObject renderObject;
	private final RenderComponentMultiplexer renderComponent = new RenderComponentMultiplexer();

	public TouchpadRenderer(RenderObject renderObject){
		this.renderObject = renderObject;
		stage = new Stage(new ScreenViewport());
	}
	public Touchpad createTouchpad(TouchpadVisibilityChanger visibilityChanger, Vector2 proportionalPosition, float proportionalRadius){
		TouchpadRenderComponent touchpadRenderComponent = new TouchpadRenderComponent(visibilityChanger, proportionalPosition, proportionalRadius, renderObject);
		renderComponent.addComponent(touchpadRenderComponent);
		return touchpadRenderComponent.touchpad;
	}

	@Override
	public RenderComponent getRenderComponent() {
		return renderComponent;
	}

	@Override
	public Stage getPreferredStage() {
		return stage;
	}

	@Override
	public boolean isWantsToFocus() {
		return true;
	}

	@Override
	public int getFocusPriority() {
		return 0;
	}

	@Override
	public void onFocusGiven(Stage mainStage) {
	}

	@Override
	public Collection<? extends InputProcessor> getInputProcessorsToFocus(Stage mainStage) {
		return Collections.singleton(stage);
	}

	static class TouchpadRenderComponent implements RenderComponent{

		private final TouchpadVisibilityChanger visibilityChanger;
		private final Touchpad touchpad;
		private final Vector2 proportionalPosition;
		private final float proportionalRadius;
		private final Drawable[] drawableArray;
		private final Drawable centerDrawable;

		private final Touchpad.TouchpadStyle style = new Touchpad.TouchpadStyle();

		TouchpadRenderComponent(TouchpadVisibilityChanger visibilityChanger, Vector2 proportionalPosition, float proportionalRadius, RenderObject renderObject){
			this.visibilityChanger = visibilityChanger;
			final Skin skin = renderObject.getArcadeSkin();
			touchpad = new Touchpad(0, skin);
			touchpad.setResetOnTouchUp(true);
			touchpad.setTouchable(Touchable.enabled);
			this.proportionalPosition = proportionalPosition;
			this.proportionalRadius = proportionalRadius;
			drawableArray = new Drawable[] {
					skin.getDrawable("joystick-r"), skin.getDrawable("joystick-ur"),
					skin.getDrawable("joystick-u"), skin.getDrawable("joystick-ul"),
					skin.getDrawable("joystick-l"), skin.getDrawable("joystick-dl"),
					skin.getDrawable("joystick-d"), skin.getDrawable("joystick-dr")
			};
			centerDrawable = skin.getDrawable("joystick");
		}

		@Override
		public void render(float delta, Stage stage) {
			if(visibilityChanger.shouldShowTouchpad()) {
				stage.addActor(touchpad);
			} else {
				touchpad.remove();
			}
			touchpad.setPosition(stage.getWidth() * proportionalPosition.x - touchpad.getWidth() / 2f,
				stage.getHeight() * proportionalPosition.y - touchpad.getHeight() / 2f);

			final float size = Math.min(stage.getWidth(), stage.getHeight()) * proportionalRadius;
			touchpad.setSize(size, size);

			final Drawable newDrawable;
			if(touchpad.getKnobPercentX() == 0 && touchpad.getKnobPercentY() == 0){
				newDrawable = centerDrawable;
			} else {
				float angleDeg = MathUtils.radiansToDegrees * MathUtils.atan2(touchpad.getKnobPercentY(), touchpad.getKnobPercentX());
				if (angleDeg < 0) {
					angleDeg += 360;
				}
				int index = Math.round(angleDeg / 45.0f);
				if(index == drawableArray.length){
					index = 0;
				}
				newDrawable = drawableArray[index];
			}
			if(style.background != newDrawable){
				style.background = newDrawable;
				touchpad.setStyle(style);
			}
		}

		@Override
		public void dispose() {
			touchpad.remove();
		}
	}
	public interface TouchpadVisibilityChanger {
		boolean shouldShowTouchpad();
	}

	/**
	 * A simple concrete class for {@link TouchpadVisibilityChanger}. This allows you to set
	 * a {@link UsableGameInput} that if active, will show the touchpad.
	 */
	public static class UsableGameInputTouchpadVisibilityChanger implements TouchpadVisibilityChanger{
		private UsableGameInput gameInput = null;

		@Override
		public boolean shouldShowTouchpad() {
			return gameInput != null && gameInput.isActiveInput();
		}
		public void setGameInput(UsableGameInput gameInput){
			this.gameInput = gameInput;
		}
		public UsableGameInput getGameInput(){ return gameInput; }
	}

}
