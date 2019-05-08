package me.retrodaredevil.game.trackshooter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.GamesActivityResultCodes;
import com.google.android.gms.games.GamesCallbackStatusCodes;
import com.google.android.gms.games.RealTimeMultiplayerClient;
import com.google.android.gms.games.multiplayer.realtime.*;
import me.retrodaredevil.game.trackshooter.account.Show;
import me.retrodaredevil.game.trackshooter.account.multiplayer.Multiplayer;

import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.requireNonNull;

public class AndroidMultiplayer implements Multiplayer {
	private static final int RC_SELECT_PLAYERS = 9006;
	private static final int RC_WAITING_ROOM = 9007;

	private final AndroidApplication activity;
	private final GoogleAccountManager accountManager;

	private final Context context;

	private final Show showInvitePlayers;

	private Game game = null;

	public AndroidMultiplayer(AndroidApplication activity, GoogleAccountManager accountManager) {
		this.activity = activity;
		this.accountManager = accountManager;

		this.context = activity.getContext();

		showInvitePlayers = GoogleSignInShow.create(
				accountManager,
				account -> Games.getRealTimeMultiplayerClient(context, account)
						.getSelectOpponentsIntent(1, 3, true) // create a room selector with 1 to 3 other players with auto matching enabled
						.addOnSuccessListener(result -> activity.startActivityForResult(result, RC_SELECT_PLAYERS))
		);

		activity.addAndroidEventListener(this::onActivityResult);
	}

	private void onActivityResult(int requestCode, int resultCode, Intent data){
		switch(requestCode){
			case RC_SELECT_PLAYERS:
				if(resultCode != Activity.RESULT_OK){
					return;
				}
				if(game != null && !game.fullyLeft){
					throw new IllegalStateException("Cannot join another game before the previous game has been left!");
				}
				game = new Game(data);
				break;
			case RC_WAITING_ROOM:
				final Game game = this.game;
				if(game != null){
					if(game.gameStarted){
						if(resultCode != Activity.RESULT_CANCELED){
							throw new AssertionError("The only reason onActivityResult would be called after the game started would be if we called finishActivity. Code resultCode: " + resultCode);
						}
						return;
					}
					switch(resultCode){
						case Activity.RESULT_OK:
							game.start();
							break;
						case Activity.RESULT_CANCELED: case GamesActivityResultCodes.RESULT_LEFT_ROOM:
							game.leave();
							break;
						default:
							System.err.println("Unknown result code for waiting room RC. resultCode: " + resultCode);
							break;
					}
				} else {
					System.err.println("Game is null when we got the waiting room finished! requestCode: " + requestCode + " resultCode: " + resultCode + " data: " + data);
				}
				break;
			default:
				break;
		}
	}


	@Override
	public Show getShowInvitePlayers() {
		return showInvitePlayers;
	}

	@Override
	public State getState() {
		Game game = this.game;
		if(game == null){
			return State.DISCONNECTED;
		}
		if(game.getRoom() != null){
			return State.CONNECTED;
		}
		return State.JOINING;
	}
	private class Game {
		private final RoomConfig config;
		private Room _roomCache = null;
		private boolean gameStarted = false;
		private boolean gameEnded = false;
		private boolean fullyLeft = false;
		Game(Intent data){
			// TODO this code will have to be modified for invitations
			ArrayList<String> invited = data.getStringArrayListExtra(Games.EXTRA_PLAYER_IDS);
			int minPlayers = data.getIntExtra(com.google.android.gms.games.multiplayer.Multiplayer.EXTRA_MIN_AUTOMATCH_PLAYERS, 0);
			int maxPlayers = data.getIntExtra(com.google.android.gms.games.multiplayer.Multiplayer.EXTRA_MAX_AUTOMATCH_PLAYERS, 0);
			if(minPlayers <= 0){
				throw new IllegalStateException("minPlayers is " + minPlayers);
			}
			if(maxPlayers <= 0){
				throw new IllegalStateException("maxPlayers is " + maxPlayers);
			}

			RoomConfig.Builder builder = RoomConfig.builder(updateCallback)
					.setOnMessageReceivedListener(this::onRealTimeMessageReceived)
					.setRoomStatusUpdateCallback(statusUpdateCallback)
					.addPlayersToInvite(invited)
					.setAutoMatchCriteria(RoomConfig.createAutoMatchCriteria(minPlayers, maxPlayers, 0));

			config = builder.build();
			RealTimeMultiplayerClient client = Games.getRealTimeMultiplayerClient(context, requireNonNull(accountManager.getLastAccount()));
			client.create(config);
		}
		private void updateRoom(Room room){
			_roomCache = room;
		}
		private Room getRoom(){
			return _roomCache;
		}
		private int getMinPlayers(){
			return config.getAutoMatchCriteria().getInt(com.google.android.gms.games.multiplayer.Multiplayer.EXTRA_MIN_AUTOMATCH_PLAYERS);
		}
		private int getMaxPlayers(){
			return config.getAutoMatchCriteria().getInt(com.google.android.gms.games.multiplayer.Multiplayer.EXTRA_MAX_AUTOMATCH_PLAYERS);
		}

		private void showWaiting(Room room){
			RealTimeMultiplayerClient client = Games.getRealTimeMultiplayerClient(context, requireNonNull(accountManager.getLastAccount()));
			client.getWaitingRoomIntent(room, getMinPlayers())
					.addOnSuccessListener(result -> activity.startActivityForResult(result, RC_WAITING_ROOM));
		}
		private void start(){
			gameStarted = true;
			activity.finishActivity(RC_WAITING_ROOM);
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
		}

		// region Callback Methods
		private void onRealTimeMessageReceived(@NonNull RealTimeMessage realTimeMessage) {

		}
		private final RoomUpdateCallback updateCallback = new RoomUpdateCallback() {
			@Override
			public void onRoomCreated(int code, @Nullable Room room) {
				updateRoom(room);
				if(code == GamesCallbackStatusCodes.OK && room != null){
					System.out.println("Created room " + room.getRoomId());
				} else {
					System.err.println("Couldn't connect to room! status code: " + code);
				}
			}

			@Override
			public void onJoinedRoom(int code, @Nullable Room room) {
				updateRoom(room);
				if(code == GamesCallbackStatusCodes.OK && room != null){
					System.out.println("Joined room " + room.getRoomId());
				} else {
					System.err.println("Couldn't connect to room! status code: " + code);
				}
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
