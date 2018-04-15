package me.retrodaredevil.game.trackshooter;

import com.sun.istack.internal.Nullable;
import me.retrodaredevil.game.trackshooter.render.RenderComponent;

public interface Renderable {
	@Nullable
	RenderComponent getRenderComponent();
}
