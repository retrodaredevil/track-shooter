package me.retrodaredevil.game.trackshooter.world;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;

import java.util.*;

public final class Tracks {
	private static final Color DEFAULT_COLOR = Color.GRAY;
//	public static Track newWeirdTrack(){
//		final float radius = 2;
//
//		Vector2 bRight = new Vector2(7, -7), bLeft = new Vector2(-7, -7),
//				uRight = new Vector2(7, 7), uLeft = new Vector2(-7, 7);
//		Vector2 u = new Vector2(0, 7), r = new Vector2(7, 0),
//				b = new Vector2(0, -7), l = new Vector2(-7, 0);
//		List<TrackPart> parts = new ArrayList<>();
//
//		return new Track(parts);
//	}
	public static Track newCircleTrack(World world){
		return new Track(Collections.singletonList(new CircleTrackPart(7, Vector2.Zero, world, -90, DEFAULT_COLOR)));
	}

	public static Track newKingdomTrack(World world){
		List<? extends TrackPart> parts = new LineTrackPart.LineTrackPartBuilder(DEFAULT_COLOR,
				new Vector2(-7, -7), world)
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
	public static Track newPlusTrack(World world){
		List<? extends TrackPart> parts = new LineTrackPart.LineTrackPartBuilder(DEFAULT_COLOR,
				new Vector2(-4, -4), world)
				.connect(-3, -7)
				.connect(3, -7)
				.connect(4, -4)
				.connect(7, -3)
				.connect(7, 3)
				.connect(4, 4)
				.connect(3, 7)
				.connect(-3, 7)
				.connect(-4, 4)
				.connect(-7, 3)
				.connect(-7, -3)
				.build(true);
		return new Track(parts);
	}
	public static Track newMazeTrack(World world){
		List<? extends TrackPart> parts = new LineTrackPart.LineTrackPartBuilder(DEFAULT_COLOR,
				new Vector2(-8, -7), world)
				.connect(7, -7)
				.connect(7, -5)
				.connect(-7, -5)
				.connect(-7, -3)
				.connect(7, -3)
				.connect(7, 3)
				.connect(-7, 3)
				.connect(-7, 5)
				.connect(7, 5)
				.connect(7, 7)
				.connect(-8, 7)
				.build(true);
		return new Track(parts);
	}
	public static Track newPointyTrack(World world){
		List<? extends TrackPart> parts = new LineTrackPart.LineTrackPartBuilder(DEFAULT_COLOR,
			new Vector2(0, 6), world)
				.connect(5.5f, 7)
				.connect(4.5f, 4.5f)
				.connect(7, 5.5f)
				.connect(6, 0)
				.connect(7, -5.5f)
				.connect(4.5f, -4.5f)
				.connect(5.5f, -7)
				.connect(0, -6)
				.connect(-5.5f, -7)
				.connect(-4.5f, -4.5f)
				.connect(-7, -5.5f)
				.connect(-6, 0)
				.connect(-7, 5.5f)
				.connect(-4.5f, 4.5f)
				.connect(-5.5f, 7)
				.build(true);
		return new Track(parts);
	}
}
