package me.retrodaredevil.game.trackshooter.account;

public interface AccountManager {
	/**
	 * Should only be called if {@link #isEverAbleToSignIn()} == true and {@link #isSignedIn()} == false
	 */
	void signIn();

	/**
	 * Should only be called if {@link #isEverAbleToSignIn()} == true and {@link #isSignedIn()} == true
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
	boolean isEverAbleToSignIn();

	/**
	 * This value only has meaning if {@link #isEverAbleToSignIn()} == true
	 * @return true if {@link #signIn()} was called and the user is logged in successfully. False otherwise
	 */
	boolean isSignedIn();

	class Defaults {
		public static final AccountManager NO_MANAGER = new AccountManager() {
			@Override
			public void signIn() {
				throw new UnsupportedOperationException();
			}

			@Override
			public void logout() {
				throw new UnsupportedOperationException();
			}

			@Override
			public boolean isEverAbleToSignIn() {
				return false;
			}

			@Override
			public boolean isSignedIn() {
				return false;
			}
		};
	}
}
