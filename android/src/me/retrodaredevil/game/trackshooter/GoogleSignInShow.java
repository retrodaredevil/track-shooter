package me.retrodaredevil.game.trackshooter;


import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import me.retrodaredevil.game.trackshooter.account.Show;

public abstract class GoogleSignInShow implements Show {

	private final GoogleAccountManager accountManager;

	private GoogleSignInShow(GoogleAccountManager accountManager) {
		this.accountManager = accountManager;
	}

	abstract void show(GoogleSignInAccount account);
	@Override
	public void show() {
		GoogleSignInAccount account = accountManager.getLastAccount();
		if(account == null){
			throw new IllegalStateException("account is null! We cannot show now! You should check isCurrentlyAbleToShow()!");
		}
		show(account);
	}

	@Override
	public boolean isCurrentlyAbleToShow() {
		return accountManager.isSignedIn();
	}

	@Override
	public boolean isEverAbleToShow() {
		return true;
	}
	public interface OnShow{
		void show(GoogleSignInAccount account);
	}
	public static Show create(GoogleAccountManager account, OnShow onShow){
		return new GoogleSignInShow(account) {
			@Override
			void show(GoogleSignInAccount account) {
				onShow.show(account);
			}
		};
	}
}

