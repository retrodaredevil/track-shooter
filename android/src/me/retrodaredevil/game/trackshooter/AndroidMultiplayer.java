package me.retrodaredevil.game.trackshooter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.Toast;
import com.badlogic.gdx.LifecycleListener;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.GamesActivityResultCodes;
import com.google.android.gms.games.GamesCallbackStatusCodes;
import com.google.android.gms.games.RealTimeMultiplayerClient;
import com.google.android.gms.games.multiplayer.Invitation;
import com.google.android.gms.games.multiplayer.realtime.*;
import me.retrodaredevil.game.trackshooter.account.Show;
import me.retrodaredevil.game.trackshooter.account.multiplayer.Multiplayer;

import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.requireNonNull;

public class AndroidMultiplayer implements Multiplayer {
	private static final int RC_SELECT_PLAYERS = 9006;
	private static final int RC_WAITING_ROOM = 9007;
	private static final int RC_INBOX = 9008;

	private final AndroidApplication activity;
	private final GoogleAccountManager accountManager;

	private final Context context;

	private final Show showInvitePlayers;
	private final Show showInbox;

	private Game game = null;

	public AndroidMultiplayer(AndroidApplication activity, GoogleAccountManager accountManager) {
		this.activity = activity;
		this.accountManager = accountManager;

		this.context = activity.getContext();

		showInvitePlayers = GoogleSignInShow.create(
				accountManager,
				account -> {
					if(game != null && !game.fullyLeft){
						game.showWaiting(game.getRoom());
					} else {
						Games.getRealTimeMultiplayerClient(context, account)
								.getSelectOpponentsIntent(1, 3, true) // create a room selector with 1 to 3 other players with auto matching enabled
								.addOnSuccessListener(result -> activity.startActivityForResult(result, RC_SELECT_PLAYERS));
					}
				}
		);
		showInbox = GoogleSignInShow.create(
				accountManager,
				account -> Games.getInvitationsClient(context, account)
						.getInvitationInboxIntent() // create a room selector with 1 to 3 other players with auto matching enabled
						.addOnSuccessListener(result -> activity.startActivityForResult(result, RC_INBOX))
		);

		activity.addAndroidEventListener(this::onActivityResult);
		activity.addLifecycleListener(new Lifecycle());
	}

	private void onActivityResult(int requestCode, int resultCode, Intent data){
		if (requestCode == RC_SELECT_PLAYERS) {
			if (resultCode != Activity.RESULT_OK) {
				if (resultCode == Activity.RESULT_CANCELED) {
					System.out.println("Back button hit while selecting players");
				}
				return;
			}
			if (game != null && !game.fullyLeft) {
				throw new IllegalStateException("Cannot join another game before the previous game has been left!");
			}
			game = new Game(data, IntentType.CREATE_ROOM);
		} else if (requestCode == RC_WAITING_ROOM) {
			final Game game = this.game;
			if (game != null) {
				final ConnectionState connectionState = getConnectionState();
				switch (resultCode) {
					case Activity.RESULT_OK:
						if (connectionState == ConnectionState.JOINING) {
							game.start();
						} else if (connectionState == ConnectionState.CONNECTED) {
							System.out.println("We are already connected!");
						} else {
							throw new IllegalStateException("Trying to open waiting room, but state is: " + connectionState);
						}
						break;
					case Activity.RESULT_CANCELED:
					case GamesActivityResultCodes.RESULT_LEFT_ROOM: // back button or explicit leave
						game.leave();
						break;
					default:
						System.err.println("Unknown result code for waiting room RC. resultCode: " + resultCode);
						break;
				}
			} else {
				System.err.println("Game is null when we got the waiting room finished! requestCode: " + requestCode + " resultCode: " + resultCode + " data: " + data);
			}
		} else if (requestCode == RC_INBOX) {
			if (resultCode != Activity.RESULT_OK) {
				return;
			}
			game = new Game(data, IntentType.RECEIVED_INVITE);
		}
	}

	private final class Lifecycle implements LifecycleListener {
		@Override
		public void pause() {
		}
		@Override
		public void resume() {
		}
		@Override
		public void dispose() {
			final Game game = AndroidMultiplayer.this.game;
			if(game != null && !game.fullyLeft){
				game.leave();
			}
		}
	}


	@Override
	public Show getShowRoomConfig() {
		return showInvitePlayers;
	}

	@Override
	public Show getShowInbox() {
		return showInbox;
	}

	@Override
	public ConnectionState getConnectionState() {
		Game game = this.game;
		if(game == null){
			return ConnectionState.DISCONNECTED;
		}
		if(game.fullyLeft){
			return ConnectionState.DISCONNECTED;
		}
		if(game.gameEnded){
			return ConnectionState.LEAVING;
		}
		if(game.getRoom() != null && game.gameStarted){
			return ConnectionState.CONNECTED;
		}
		return ConnectionState.JOINING;
	}
	private enum IntentType {
		CREATE_ROOM,
		RECEIVED_INVITE
	}
	private class Game {
		private final RoomConfig config;
		private Room _roomCache = null;
		private boolean gameStarted = false;
		private boolean gameEnded = false;
		private boolean fullyLeft = false;
		Game(Intent data, IntentType intentType){
			requireNonNull(data);
			requireNonNull(intentType);

			if(intentType == IntentType.CREATE_ROOM) {
				ArrayList<String> invited = data.getStringArrayListExtra(Games.EXTRA_PLAYER_IDS);

				RoomConfig.Builder builder = RoomConfig.builder(updateCallback)
						.setOnMessageReceivedListener(this::onRealTimeMessageReceived)
						.setRoomStatusUpdateCallback(statusUpdateCallback)
						.addPlayersToInvite(invited);

				final int minPlayers = data.getIntExtra(com.google.android.gms.games.multiplayer.Multiplayer.EXTRA_MIN_AUTOMATCH_PLAYERS, 0);
				final int maxPlayers = data.getIntExtra(com.google.android.gms.games.multiplayer.Multiplayer.EXTRA_MAX_AUTOMATCH_PLAYERS, 0);
				if(minPlayers != 0 || maxPlayers != 0){
					builder.setAutoMatchCriteria(RoomConfig.createAutoMatchCriteria(minPlayers, maxPlayers, 0));
				}

				config = builder.build();
				RealTimeMultiplayerClient client = Games.getRealTimeMultiplayerClient(context, requireNonNull(accountManager.getLastAccount()));
				client.create(config);
			} else if(intentType == IntentType.RECEIVED_INVITE){
				Invitation invitation = requireNonNull(data.getExtras()).getParcelable(com.google.android.gms.games.multiplayer.Multiplayer.EXTRA_INVITATION);
				if(invitation == null){
					throw new IllegalArgumentException("data's extras had a null Invitation!");
				}
				config = RoomConfig.builder(updateCallback)
						.setInvitationIdToAccept(invitation.getInvitationId())
						.setOnMessageReceivedListener(this::onRealTimeMessageReceived)
						.setRoomStatusUpdateCallback(statusUpdateCallback)
						.build();
				RealTimeMultiplayerClient client = Games.getRealTimeMultiplayerClient(context, requireNonNull(accountManager.getLastAccount()));
				client.join(config);
			} else throw new UnsupportedOperationException("Unsupported IntentType: " + intentType);
		}
		private void updateRoom(Room room){
			_roomCache = room;
		}
		private Room getRoom(){
			return _roomCache;
		}
		private int getMinPlayers(){
			final Bundle autoMatch = config.getAutoMatchCriteria();
			if(autoMatch == null){
				return config.getInvitedPlayerIds().length;
			}
			return autoMatch.getInt(com.google.android.gms.games.multiplayer.Multiplayer.EXTRA_MIN_AUTOMATCH_PLAYERS);
		}
		private int getMaxPlayers(){
			final Bundle autoMatch = config.getAutoMatchCriteria();
			if(autoMatch == null){
				return config.getInvitedPlayerIds().length;
			}
			return autoMatch.getInt(com.google.android.gms.games.multiplayer.Multiplayer.EXTRA_MAX_AUTOMATCH_PLAYERS);
		}

		private void showWaiting(Room room){
			RealTimeMultiplayerClient client = Games.getRealTimeMultiplayerClient(context, requireNonNull(accountManager.getLastAccount()));
			client.getWaitingRoomIntent(room, getMinPlayers())
					.addOnSuccessListener(result -> activity.startActivityForResult(result, RC_WAITING_ROOM));
		}
		private void start(){
			System.out.println("Starting game");
			gameStarted = true;
			activity.finishActivity(RC_WAITING_ROOM);
//			Games.getRealTimeMultiplayerClient(context, requireNonNull(accountManager.getLastAccount()))
//					.sendReliableMessage(new byte[]{0}, "", "", (statusCode, tokenId, recipientId) -> {
//				System.out.println("reliable message send. Status code: " + statusCode + " token id: " + tokenId + " recipient: " + recipientId);
//			});
			sendMessage(true, null, new byte[] {0});
		}
		private void leave(){
			if(gameEnded){
				throw new IllegalStateException("The game has already ended!");
			}
			Room room = getRoom();
			if(room == null){
				throw new IllegalStateException("Cannot leave when room is null!");
			}
			GoogleSignInAccount account = accountManager.getLastAccount();
			if(account != null) {
				Games.getRealTimeMultiplayerClient(context, account).leave(config, room.getRoomId());
			}
			updateRoom(null);
			gameEnded = true;
			System.out.println("Left game");
		}
		// region Messages

		/**
		 * NOTE: If a recipient is not connected to the room, they will not be sent the message. You can use the return
		 * value to see how many recipients it was sent to.
		 * @param reliable true if reliable, false otherwise
		 * @param recipients The list of participation ids of each recipient or null to send to everyone in the room.
		 * @param message The message to send
		 * @return The number of recipients the message was sent to
		 */
		private int sendMessage(boolean reliable, @Nullable List<String> recipients, byte[] message){
			if(recipients != null && recipients.isEmpty()){
				throw new IllegalArgumentException("recipients is empty!");
			}
			Room room = requireNonNull(getRoom());
			if(recipients == null){
				recipients = room.getParticipantIds();
			}
			requireNonNull(recipients);
			RealTimeMultiplayerClient client = Games.getRealTimeMultiplayerClient(context, requireNonNull(accountManager.getLastAccount()));

			if(reliable){
				int r = 0;
				for(String recipient : recipients) {
					if(room.getParticipant(recipient).isConnectedToRoom()) {
						client.sendReliableMessage(message, room.getRoomId(), recipient, null);
						r++;
					}
				}
				return r;
			} else {
				if(recipients.size() == 1){
					String recipient = recipients.get(0);
					if(room.getParticipant(recipient).isConnectedToRoom()) {
						client.sendUnreliableMessage(message, room.getRoomId(), recipients.get(0));
						return 1;
					}
					return 0;
				} else {
					List<String> connectedRecipients = new ArrayList<>(recipients.size());
					for(String recipient : recipients){
						if(room.getParticipant(recipient).isConnectedToRoom()){
							connectedRecipients.add(recipient);
						}
					}
					client.sendUnreliableMessage(message, room.getRoomId(), connectedRecipients);
					return connectedRecipients.size();
				}
			}
		}

		private void onRealTimeMessageReceived(@NonNull RealTimeMessage realTimeMessage) {
			System.out.println("got real time message: " + realTimeMessage + " sender: " + realTimeMessage.getSenderParticipantId() + " reliable: " + realTimeMessage.isReliable());
		}
		// endregion

		// region Callback Methods
		/**
		 * Handles all callbacks related to the connection/disconnection of the player. NOT other players
		 */
		private final RoomUpdateCallback updateCallback = new RoomUpdateCallback() {
			private final Handler handler = new Handler();
			@Override
			public void onRoomCreated(int code, @Nullable Room room) {
				updateRoom(room);
				if(code == GamesCallbackStatusCodes.OK && room != null){
					System.out.println("Created room " + room.getRoomId());
					showWaiting(room);
				} else {
					scheduleFailMessage(code);
					System.err.println("Couldn't connect to room (onRoomCreated)! status code: " + code);
					fullyLeft = true;
				}
			}

			@Override
			public void onJoinedRoom(int code, @Nullable Room room) {
				updateRoom(room);
				if(code == GamesCallbackStatusCodes.OK && room != null){
					System.out.println("Joined room " + room.getRoomId());
					showWaiting(room);
				} else {
					scheduleFailMessage(code);
					System.err.println("Couldn't connect to room (onJoinedRoom)! status code: " + code);
					fullyLeft = true;
				}
			}
			private void scheduleFailMessage(int code){
				handler.postDelayed(() -> {
					Toast.makeText(context, "Error: " + GamesCallbackStatusCodes.getStatusCodeString(code), Toast.LENGTH_LONG).show();
				}, 100);
			}

			@Override
			public void onLeftRoom(int code, @NonNull String roomId) {
				updateRoom(null);
				fullyLeft = true;
			}

			@Override
			public void onRoomConnected(int code, @Nullable Room room) {
				updateRoom(room);
			}
		};
		private final RoomStatusUpdateCallback statusUpdateCallback = new RoomStatusUpdateCallback() {
			@Override
			public void onRoomConnecting(@Nullable Room room) {
				updateRoom(room);
			}

			@Override
			public void onRoomAutoMatching(@Nullable Room room) {
				updateRoom(room);
			}

			@Override
			public void onPeerInvitedToRoom(@Nullable Room room, @NonNull List<String> list) {
				updateRoom(room);
			}

			@Override
			public void onPeerDeclined(@Nullable Room room, @NonNull List<String> list) {
				updateRoom(room);
			}

			@Override
			public void onPeerJoined(@Nullable Room room, @NonNull List<String> list) {
				updateRoom(room);
			}

			@Override
			public void onPeerLeft(@Nullable Room room, @NonNull List<String> list) {
				updateRoom(room);
			}
			@Override
			public void onConnectedToRoom(@Nullable Room room) {
				updateRoom(room);
			}

			@Override
			public void onDisconnectedFromRoom(@Nullable Room room) {
				updateRoom(room);
			}

			@Override
			public void onPeersConnected(@Nullable Room room, @NonNull List<String> list) {
				updateRoom(room);
			}

			@Override
			public void onPeersDisconnected(@Nullable Room room, @NonNull List<String> list) {
				updateRoom(room);
			}

			@Override
			public void onP2PConnected(@NonNull String participantId) { }

			@Override
			public void onP2PDisconnected(@NonNull String participantId) { }
		};
		// endregion
	}

}
