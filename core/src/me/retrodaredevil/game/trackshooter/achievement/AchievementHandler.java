package me.retrodaredevil.game.trackshooter.achievement;

public interface AchievementHandler {

	/**
	 * Should only be called if {@link #isNeedsSignIn()} == true and {@link #isSignedIn()} == false
	 */
	void signIn();

	/**
	 * Should only be called if {@link #isNeedsSignIn()} == true and {@link #isSignedIn()} == true
	 */
	void logout();

	/**
	 * If this returns false, {@link #signIn()} should never be called
	 *
	 * NOTE: Throughout this object's lifetime, you should expect this to remain the same
	 *
	 * NOTE: This may not change if {@link #signIn()} is called. You should use {@link #isSignedIn()} to check if it is signed in
	 * @return true if {@link #signIn()} needs to be called for this to work, false otherwise.
	 */
	boolean isNeedsSignIn();

	/**
	 * This value only has meaning if {@link #isNeedsSignIn()} == true
	 * @return true if {@link #signIn()} was called and the user is logged in successfully. False otherwise
	 */
	boolean isSignedIn();

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

	void showAchievements();
	boolean isEverAbleToShowAchievements();
	boolean isCurrentlyAbleToShowAchievements();

	void showLeaderboards();
	boolean isEverAbleToShowLeaderboards();
	boolean isCurrentlyAbleToShowLeaderboards();

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
			public void signIn() {
				throw new UnsupportedOperationException();
			}

			@Override
			public void logout() {
				throw new UnsupportedOperationException();
			}

			@Override
			public boolean isNeedsSignIn() {
				return false;
			}

			@Override
			public boolean isSignedIn() {
				return false;
			}

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
			public void showAchievements() {
				throw new UnsupportedOperationException();
			}
			@Override public boolean isEverAbleToShowAchievements() { return false; }

			@Override public boolean isCurrentlyAbleToShowAchievements() { return false; }

			@Override
			public void showLeaderboards() {
				throw new UnsupportedOperationException();
			}
			@Override public boolean isEverAbleToShowLeaderboards() { return false; }
			@Override public boolean isCurrentlyAbleToShowLeaderboards() { return false; }

			@Override
			public void submitScore(int score) {
			}

		};
	}
}
