package me.retrodaredevil.game.trackshooter.render.components;

import com.badlogic.gdx.scenes.scene2d.Stage;
import me.retrodaredevil.game.trackshooter.world.Track;
import me.retrodaredevil.game.trackshooter.world.TrackPart;

/**
 * Used to render a Track object. Uses a Track object's TrackParts to call their RenderComponents
 */
public class TrackRenderComponent implements RenderComponent {
	private Track track;
	public TrackRenderComponent(Track track){
		this.track = track;
	}
	@Override
	public void render(float delta, Stage stage) {
		for(TrackPart part : track.getParts()){
//			RenderComponent render = part.getRenderComponent();
//			if(render != null){
//				render.render(delta, stage);
//			}
			part.autoRender(delta, stage, false);
		}
	}

	@Override
	public void dispose() {
	}
}
