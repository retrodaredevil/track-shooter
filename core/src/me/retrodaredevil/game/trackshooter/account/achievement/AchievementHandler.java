package me.retrodaredevil.game.trackshooter.account.achievement;

import me.retrodaredevil.game.trackshooter.account.Show;

public interface AchievementHandler {


	void increment(GameEvent event, int amount);

	/**
	 * Works only on non-incremental achievements
	 * @param achievement The achievement
	 * @throw IllegalArgumentException if {@link Achievement#isIncremental()} ()}
	 */
	void manualAchieve(ManualAchievement achievement);

	/**
	 * Works on both incremental and non-incremental achievements. For a non-incremental achievement, this would be
	 * equivalent to calling {@link #manualAchieve(ManualAchievement)}
	 * @param achievement The achievement
	 * @param amount
	 */
	void manualIncrement(ManualAchievement achievement, int amount);

	void manualReveal(ManualAchievement achievement);

	boolean isSupported(GameEvent event);
	boolean isSupported(ManualAchievement achievement);

	Show getShowAchievements();
	Show getShowLeaderboards();

	/**
	 * If able, submits the score. Implementation may do nothing. You don't have to worry about that.
	 * @param score The score
	 */
	void submitScore(int score);

	default boolean incrementIfSupported(GameEvent event, int amount){
		if(!isSupported(event)){
			return false;
		}
		increment(event, amount);
		return true;
	}
	default boolean manualIncrementIfSupported(ManualAchievement achievement, int amount){
		if(!isSupported(achievement)){
			return false;
		}
		manualIncrement(achievement, amount);
		return true;
	}
	default boolean manualAchieveIfSupported(ManualAchievement achievement){
		if(!isSupported(achievement)){
			return false;
		}
		manualAchieve(achievement);
		return true;
	}
	default boolean manualRevealIfSupported(ManualAchievement achievement){
		if(!isSupported(achievement)){
			return false;
		}
		manualReveal(achievement);
		return true;
	}

	class Defaults {
		public static final AchievementHandler UNSUPPORTED_HANDLER = new AchievementHandler() {

			@Override
			public void increment(GameEvent event, int amount) {
				throw new UnsupportedOperationException();
			}
			@Override
			public void manualAchieve(ManualAchievement achievement) {
				throw new UnsupportedOperationException();
			}

			@Override
			public void manualIncrement(ManualAchievement achievement, int amount) {
				throw new UnsupportedOperationException();
			}

			@Override
			public void manualReveal(ManualAchievement achievement) {
				throw new UnsupportedOperationException();
			}

			@Override
			public boolean isSupported(GameEvent event) {
				return false;
			}

			@Override
			public boolean isSupported(ManualAchievement achievement) {
				return false;
			}

			@Override
			public Show getShowAchievements() {
				return Show.Defaults.NOT_ABLE;
			}

			@Override
			public Show getShowLeaderboards() {
				return Show.Defaults.NOT_ABLE;
			}

			@Override
			public void submitScore(int score) {
			}

		};
	}
}
