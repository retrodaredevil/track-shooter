package me.retrodaredevil.game.trackshooter.overlay;

import me.retrodaredevil.game.trackshooter.Renderable;
import me.retrodaredevil.game.trackshooter.entity.player.Player;
import me.retrodaredevil.game.trackshooter.render.RenderComponent;
import me.retrodaredevil.game.trackshooter.world.World;

public class Overlay implements Renderable {
	private final World world;

	private RenderComponent component;

	public Overlay(World world, Player player){
		this.world = world;
		this.component = new OverlayRenderer(player);
	}
	@Override
	public RenderComponent getRenderComponent() {
		return component;
	}

	@Override
	public void disposeRenderComponent() {
		if(component != null){
			component.dispose();
		}
	}
}
