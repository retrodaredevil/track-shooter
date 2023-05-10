package me.retrodaredevil.game.trackshooter.render.selection.options;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;

import me.retrodaredevil.game.trackshooter.render.selection.options.providers.PageControlOptionVisibility;
import me.retrodaredevil.game.trackshooter.util.ActorUtil;
import me.retrodaredevil.game.trackshooter.util.Size;

public class PageButtonSingleOption extends ButtonSingleOption {
	private final Button button;
	private final PageControlOptionVisibility visibility;
	private final PageControlOptionVisibility.Page page;
	public PageButtonSingleOption(Button button, Size size, PageControlOptionVisibility.Page page, PageControlOptionVisibility visibility) {
		super(button, size, requestingActions -> visibility.setPage(page));
		this.button = button;
		this.page = page;
		this.visibility = visibility;
	}

	@Override
	protected void onUpdate() {
		super.onUpdate();
		if(visibility.getPage() == page){
			if(!button.isOver()) {
				ActorUtil.fireInputEvents(button, InputEvent.Type.enter);
			}
		} else {
			if(button.isOver()) {
				ActorUtil.fireInputEvents(button, InputEvent.Type.exit);
			}
		}
	}
}
