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
	void achieve(ManualAchievement achievement);

	boolean isSupported(GameEvent event);
	boolean isSupported(ManualAchievement achievement);

	void showAchievements();
	boolean canShowAchievements();

	default boolean incrementIfSupported(GameEvent event, int amount){
		if(!isSupported(event)){
			return false;
		}
		increment(event, amount);
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
			public void achieve(ManualAchievement achievement) {
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

			@Override
			public boolean canShowAchievements() {
				return false;
			}
		};
	}
}
