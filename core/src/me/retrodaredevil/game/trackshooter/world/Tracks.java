package me.retrodaredevil.game.trackshooter.world;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public final class Tracks {
	public static Track newWeirdTrack(){

		Vector2 bRight = new Vector2(7, -7), bLeft = new Vector2(-7, -7),
				uRight = new Vector2(7, 7), uLeft = new Vector2(-7, 7);
		Collection<? extends TrackPart> parts = new LineTrackPart.LineTrackPartBuilder(Color.GRAY,
				new Vector2(-6, -6))
				.connect(new Vector2(5, -7))
				.connect(new Vector2(5, -5))
				.connect(new Vector2(7, -5))
				.connect(uRight)
				.connect(new Vector2(0, 5))
				.connect(uLeft)
				.build(true);
		return new Track(parts);
	}
	public static Track newCircleTrack(){
		Collection<? extends TrackPart> parts = Collections.singletonList(new CircleTrackPart(7, Color.GRAY));
		return new Track(new ArrayList<>(parts));
	}
}
