package me.retrodaredevil.game.trackshooter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;
import com.badlogic.gdx.LifecycleListener;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import me.retrodaredevil.game.trackshooter.account.AccountManager;

import java.util.Collection;
import java.util.LinkedHashSet;

import static java.util.Objects.requireNonNull;

public class GoogleAccountManager implements AccountManager {
	private static final int REQUEST_SIGN_IN = 9001;

	private final AndroidApplication activity;
	private final GoogleSignInClient signInClient;

	private final Context context;

	private final Collection<OnSignIn> signInListeners = new LinkedHashSet<>();

	private boolean wantsSignIn = false;

	public GoogleAccountManager(AndroidApplication activity, GoogleSignInClient signInClient) {
		this.activity = activity;
		this.signInClient = signInClient;

		context = activity.getContext();

		activity.addAndroidEventListener(this::onActivityResult);
		activity.addLifecycleListener(new BasicListener());
	}

	public GoogleSignInAccount getLastAccount(){
		return GoogleSignIn.getLastSignedInAccount(context);
	}
	public boolean addSignInListener(OnSignIn signInListener){
		return signInListeners.add(signInListener);
	}

	@Override
	public void signIn() {
		System.out.println("Going to sign in now isSignedIn: " + isSignedIn());
		wantsSignIn = true;
		activity.startActivityForResult(signInClient.getSignInIntent(), REQUEST_SIGN_IN);
	}
	private void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(requestCode == REQUEST_SIGN_IN){
			Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
			try {
				GoogleSignInAccount account = task.getResult(ApiException.class);
				doAccountSignIn(account);
				Toast.makeText(context, "Signed into Google", Toast.LENGTH_SHORT).show();
			} catch (ApiException apiException) {
				onAccountLogout();
				switch(apiException.getStatusCode()){
					case 12501:
						System.out.println("Pressed back button while signing in.");
						break;
					case 4:
						System.out.println("Dreaded status code 4...");
						new AlertDialog.Builder(activity)
								.setMessage("Status code 4. Unable to connect. This error is probably temporary and is likely being fixed.")
								.setNeutralButton(android.R.string.ok, null)
								.show();
						break;
					default:
						String message = apiException.getMessage();
						if (message == null || message.trim().isEmpty()) {
							message = "Unable to connect";
						}


						new AlertDialog.Builder(activity)
								.setMessage(message)
								.setNeutralButton(android.R.string.ok, null)
								.show();
						break;
				}
			}
		}
	}

	@Override
	public void logout() {
		wantsSignIn = false;
		if(isSignedIn()){
			signInClient.signOut()
					.addOnSuccessListener(result -> onAccountLogout())
					.addOnFailureListener(result -> {throw new IllegalStateException("Signing out should never fail!"); });
		}
	}

	private void onResume(){
		System.out.println("Going to silently sign in again");

		if(isSignedIn()){
			System.out.println("We're already signed in!");
		} else if(wantsSignIn){
			signInClient.silentSignIn().addOnCompleteListener(accountTask -> {
				if (accountTask.isSuccessful()) {
					final GoogleSignInAccount account = requireNonNull(accountTask.getResult());
					doAccountSignIn(account);
					System.out.println("Successfully connected to google");
				} else {
					Exception exception = requireNonNull(accountTask.getException());
					if (exception instanceof ApiException) {
						System.err.println("Tried to connect to google. My best guess on this exception is that the app isn't signed correctly. Status code: " + ((ApiException) exception).getStatusCode());
					}
					System.err.println("Failed to sign in. Message: " + exception.getMessage());
					exception.printStackTrace();
				}
			});
		}
	}
	@Override
	public boolean isSignedIn(){
		return getLastAccount() != null;
	}
	@Override
	public boolean isEverAbleToSignIn() {
		return true;
	}

	private void doAccountSignIn(GoogleSignInAccount account){
		requireNonNull(account);
		System.out.println("Successfully signed in");
		wantsSignIn = true;
		for(OnSignIn onSignIn : signInListeners){
			onSignIn.onSignIn(account);
		}
	}
	private void onAccountLogout(){
		System.out.println("Logging out");
		wantsSignIn = false;
	}



	private class BasicListener implements LifecycleListener {

		@Override
		public void pause() {
		}
		@Override
		public void resume() {
			onResume();
		}

		@Override
		public void dispose() {
		}
	}
	public interface OnSignIn {
		void onSignIn(GoogleSignInAccount account);
	}
}
