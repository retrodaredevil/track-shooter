package me.retrodaredevil.game.trackshooter.world;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;

import java.util.*;

public final class Tracks {
	public static Track newWeirdTrack(){

		Vector2 bRight = new Vector2(7, -7), bLeft = new Vector2(-7, -7),
				uRight = new Vector2(7, 7), uLeft = new Vector2(-7, 7);
		List<? extends TrackPart> parts = new LineTrackPart.LineTrackPartBuilder(Color.GRAY,
				new Vector2(-6, -6))
				.connect(5, -7)
				.connect(5, -5)
				.connect(7, -5)
				.connect(uRight)
				.connect(0, 5)
				.connect(uLeft)
				.build(true);
		return new Track(parts);
	}
	public static Track newCircleTrack(){
		return new Track(Collections.singletonList(new CircleTrackPart(7, Color.GRAY)));
	}

	public static Track newKingdomTrack(){
		List<? extends TrackPart> parts = new LineTrackPart.LineTrackPartBuilder(Color.GRAY,
				new Vector2(-7, -7))
				.connect(-5, -7)
				.connect(-5, -6)
				.connect( 5, -6)
				.connect( 5, -7)
				.connect( 7, -7)
				.connect( 7, -5)
				.connect( 6, -5)
				.connect( 6,  5)
				.connect( 7,  5)
				.connect( 7,  7)
				.connect( 5,  7)
				.connect( 5,  6)
				.connect(-5,  6)
				.connect(-5,  7)
				.connect(-7,  7)
				.connect(-7,  5)
				.connect(-6,  5)
				.connect(-6, -5)
				.connect(-7, -5)
				.build(true);
		return new Track(parts);
	}
}
