package me.retrodaredevil.game.trackshooter;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

import java.util.List;

import me.retrodaredevil.game.trackshooter.entity.enemies.shark.Shark;
import me.retrodaredevil.game.trackshooter.entity.enemies.shark.SharkAIController;
import me.retrodaredevil.game.trackshooter.entity.player.Player;
import me.retrodaredevil.game.trackshooter.level.EnemyLevel;
import me.retrodaredevil.game.trackshooter.level.Level;
import me.retrodaredevil.game.trackshooter.level.LevelGetter;
import me.retrodaredevil.game.trackshooter.level.functions.FruitFunction;
import me.retrodaredevil.game.trackshooter.level.functions.SnakeFunction;
import me.retrodaredevil.game.trackshooter.level.functions.TripleShotPowerupFunction;
import me.retrodaredevil.game.trackshooter.world.Track;
import me.retrodaredevil.game.trackshooter.world.Tracks;
import me.retrodaredevil.game.trackshooter.world.World;

/**
 * This is the default level getter used all of the time
 */
public class GameLevelGetter implements LevelGetter {
//	private static final Vector2 temp = new Vector2();

	private int levelNumber = 0; // still starts at 1 (incremented first thing in start of nextLevel())
	private final Track[] tracks;
	private final List<? extends Player> players;

	public GameLevelGetter(List<? extends Player> players){
		this.players = players;
		this.tracks = new Track[] {Tracks.newKingdomTrack(), Tracks.newWeirdTrack(), Tracks.newCircleTrack() };
	}

	@Override
	public Level nextLevel() {
		levelNumber++; // future programmers you're welcome that I put this on a separate line.
		return new EnemyLevel(levelNumber, tracks[(levelNumber - 1) % tracks.length]) {
			@Override
			protected void onStart(World world) {
				super.onStart(world);
				addFunction(new FruitFunction());
				if(levelNumber != 1 && levelNumber != 3 && levelNumber % 4 != 0){ // on all levels except 1, 3 and any multiples of 4
					for(Player player : players) {
						addFunction(new SnakeFunction(player));
					}
				}
				if(levelNumber % 2 == 1){
					addFunction(new TripleShotPowerupFunction());
				}

				final int amount = 4 + (levelNumber / 2); // add a shark every 2 levels
				final float spacing = world.getTrack().getTotalDistance() / amount;
				for(int i = 0; i < amount; i++){
					int sign = ((i % 2) * 2) - 1; // instead of using Math.pow(1, i), we use this
					float trackDistanceAway = sign * ((i / 2f) * spacing);
					float positionAngle = (i * 360f) / amount;
					positionAngle += 45;
					Vector2 location = new Vector2(MathUtils.cosDeg(positionAngle), MathUtils.sinDeg(positionAngle));
					float angle = positionAngle;
					Shark shark = new Shark(location, angle);
					shark.setEntityController(new SharkAIController(shark, players, trackDistanceAway, sign * (2f + (i * .5f / amount))));
					// start in the start position
					shark.setLocation(location, angle);

					addEntity(world, shark);
				}
			}
		};
	}
}
