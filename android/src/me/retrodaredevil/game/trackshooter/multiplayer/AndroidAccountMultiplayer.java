package me.retrodaredevil.game.trackshooter.multiplayer;

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
import me.retrodaredevil.game.trackshooter.GoogleAccountManager;
import me.retrodaredevil.game.trackshooter.GoogleSignInShow;
import me.retrodaredevil.game.trackshooter.account.Show;
import me.retrodaredevil.game.trackshooter.account.multiplayer.AccountMultiplayer;

import java.util.*;

import static java.util.Objects.requireNonNull;

public class AndroidAccountMultiplayer implements AccountMultiplayer {
	private static final int RC_SELECT_PLAYERS = 9006;
	private static final int RC_WAITING_ROOM = 9007;
	private static final int RC_INBOX = 9008;

	private final AndroidApplication activity;
	private final GoogleAccountManager accountManager;

	private final Context context;

	private final Show showInvitePlayers;
	private final Show showInbox;

	private Game game = null;

	public AndroidAccountMultiplayer(AndroidApplication activity, GoogleAccountManager accountManager) {
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
						System.out.println("Leaving room because user pressed back button or explicitly asked to leave.");
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
			final Game game = AndroidAccountMultiplayer.this.game;
			if(game != null && !game.fullyLeft){
				System.out.println("dispose() called. Leaving game");
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
		if(game.getRoom() != null && game.isGameStarted()){
			return ConnectionState.CONNECTED;
		}
		return ConnectionState.JOINING;
	}

	@Override
	public Multiplayer getMultiplayer() {
		if(getConnectionState() != ConnectionState.CONNECTED){
			throw new IllegalStateException();
		}
		return requireNonNull(game.gameMultiplayer);
	}

	private enum IntentType {
		CREATE_ROOM,
		RECEIVED_INVITE
	}
	private class Game {
		private final RoomConfig config;
		private Room _roomCache = null;
		private boolean gameEnded = false;
		private boolean fullyLeft = false;
		private String myParticipantId = null;

		private GameMultiplayer gameMultiplayer = null;
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
			if(gameEnded){
				throw new IllegalStateException("We can't start a game that has ended!!");
			}
			System.out.println("Starting game");
			activity.finishActivity(RC_WAITING_ROOM);
//			Games.getRealTimeMultiplayerClient(context, requireNonNull(accountManager.getLastAccount()))
//					.sendReliableMessage(new byte[]{0}, "", "", (statusCode, tokenId, recipientId) -> {
//				System.out.println("reliable message send. Status code: " + statusCode + " token id: " + tokenId + " recipient: " + recipientId);
//			});
			gameMultiplayer = new GameMultiplayer(this, requireNonNull(myParticipantId));
		}
		private boolean isGameStarted(){
			return gameMultiplayer != null;
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
			gameMultiplayer = null;
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
			Packet packet = PacketHelper.parseFromBytes(realTimeMessage.getMessageData());
			if(!isGameStarted()){
				start();
			}
			try {
				gameMultiplayer.receivePacket(realTimeMessage.getSenderParticipantId(), packet);
			} catch (UnableToAgreeException e) {
				e.printStackTrace();
				Toast.makeText(context, "One of the players couldn't agree on something!", Toast.LENGTH_LONG).show();
				game.leave();
				System.out.println("We left the game because we weren't able to agree on something.");
			}
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
				requireNonNull(room);
				updateRoom(room);
				Games.getPlayersClient(context, requireNonNull(accountManager.getLastAccount())).getCurrentPlayerId().addOnSuccessListener(playerId -> {
					myParticipantId = room.getParticipantId(playerId);
				});
			}

			@Override
			public void onDisconnectedFromRoom(@Nullable Room room) {
				updateRoom(room);
				if (!game.gameEnded) {
					System.out.println("We disconnected from the room and the game wasn't ended so we will now leave");
					Toast.makeText(context, "We disconnected from the room because of an error!", Toast.LENGTH_LONG).show();
					game.leave();
				}
			}

			@Override
			public void onPeersConnected(@Nullable Room room, @NonNull List<String> list) {
				updateRoom(room);
			}

			@Override
			public void onPeersDisconnected(@Nullable Room room, @NonNull List<String> list) {
				updateRoom(room);
				GameMultiplayer multiplayer = game.gameMultiplayer;
				if(multiplayer != null){
					if(list.contains(multiplayer.host)){
						System.out.println("Leaving game because host left!");
						Toast.makeText(context, "The host left! Room closed.", Toast.LENGTH_LONG).show();
						game.leave();
					}
				}
			}

			@Override
			public void onP2PConnected(@NonNull String participantId) { }

			@Override
			public void onP2PDisconnected(@NonNull String participantId) { }
		};
		// endregion
	}

	private class GameMultiplayer implements Multiplayer {

		private final Game game;
		/** Our own participant id*/
		private final String participantId;
		private final List<String> participants;
		/** All the players*/
		private final List<GamePlayer> players;
		/** Players that we handle */
		private final List<GamePlayer> handledPlayers;
		/** Players that we don't handle */
		private final List<GamePlayer> nonHandledPlayers;
		/** The participant id of the host*/
		private final String host;

		private final Set<String> participantsReady = new HashSet<>();

		private GameMultiplayer(Game game, String participantId) {
			this.game = game;
			this.participantId = requireNonNull(participantId);
			Room room = game.getRoom();
			final List<String> participants = new ArrayList<>();
			final List<GamePlayer> players = new ArrayList<>();
			final List<GamePlayer> handledPlayers = new ArrayList<>(1);
			final List<GamePlayer> nonHandledPlayers = new ArrayList<>();
			for(String participant : room.getParticipantIds()){
				if(room.getParticipant(participant).isConnectedToRoom()){
					participants.add(participant);
					GamePlayer player = new GamePlayer(participant, this);
					players.add(player); // TODO in the future a single participant might have multiple players
					if(participant.equals(participantId)){
						handledPlayers.add(player);
					} else {
						nonHandledPlayers.add(player);
					}
				} else {
					System.out.println("Participant: " + participant + " is not connected.");
				}
			}
			this.participants = Collections.unmodifiableList(participants);
			this.players = Collections.unmodifiableList(players);
			this.handledPlayers = Collections.unmodifiableList(handledPlayers);
			this.nonHandledPlayers = Collections.unmodifiableList(nonHandledPlayers);
			host = new TreeSet<>(participants).first();
			if(isHost()){
				System.out.println("We are the host! We will wait to make sure everyone is ready!");
			} else {
				System.out.println(host + " is the host. We will send them a client ready packet");
				sendToHost(new ClientReadyPacket(host, this.participantId, this.participants));
			}
		}

		private void receivePacket(String sender, Packet packet) throws UnableToAgreeException {
			if(packet instanceof ClientReadyPacket){
				if(!isHost()){
					throw new IllegalStateException("sender: " + sender + " is trying to send us a packet meant to go to the host!");
				}
				ClientReadyPacket gameStarting = (ClientReadyPacket) packet;
				boolean fromCorrectSender = sender.equals(gameStarting.getParticipantId());
				if(!fromCorrectSender){
					throw new UnableToAgreeException("The sender sent an incorrect id! sender: " + sender + ". they thought they were: " + gameStarting.getParticipantId());
				}
				if(!participants.contains(sender)){
					throw new IllegalStateException("Received a packet from sender: " + sender);
				}
				boolean hostCorrect = gameStarting.getHostId().equals(host);
				if(!hostCorrect){
					throw new UnableToAgreeException("Cannot agree on a host! We want host: " + host + " while sender: " + sender + " wants host: " + gameStarting.getHostId());
				}
				boolean participantsCorrect = new HashSet<>(participants).equals(new HashSet<>(gameStarting.getAllParticipants()));
				if(!participantsCorrect){
					throw new UnableToAgreeException("Cannot agree on participants! We want participants: " + participants
							+ " while sender: " + sender + " wants participants: " + gameStarting.getAllParticipants());
				}
				if(!participantsReady.add(sender)){
					throw new IllegalStateException("Unable to add sender to participantsReady meaning they sent this packet twice!");
				}
				System.out.println(sender + " was able to agree on everything!");
				if(participantsReady.size() + 1 == participants.size()){
					System.out.println("It looks like everyone has sent us a ready packet! Time to begin the game!");
				}
				return;
			}
			if(participantsReady.size() + 1 != participants.size()){
				throw new IllegalStateException(sender + " is trying to send us a packet when not everyone is ready! packet: " + packet);
			}
			System.out.println("received game packet: " + packet);
		}

		@Override
		public boolean isConnected() {
			return getConnectionState() == ConnectionState.CONNECTED;
		}

		@Override
		public void leave() {
			if(!isConnected()){
				throw new IllegalStateException("Cannot leave if we aren't connected!");
			}
			System.out.println("leave() called on Multiplayer instance. Leaving game");
			game.leave();
		}

		@Override
		public boolean isHost() {
			return participantId.equals(host);
		}

		@Override
		public void sendToHost(Packet packet) {
			game.sendMessage(packet.isReliable(), Collections.singletonList(host), PacketHelper.convertToBytes(packet));
		}

		@Override
		public void sendToEveryone(Packet packet) {
			game.sendMessage(packet.isReliable(), participants, PacketHelper.convertToBytes(packet));
		}

		@Override
		public void sendToPlayer(Packet packet, Player player) {
			GamePlayer gamePlayer = (GamePlayer) player;
			game.sendMessage(packet.isReliable(), Collections.singletonList(gamePlayer.participantId), PacketHelper.convertToBytes(packet));
		}

		@Override
		public void sendToPlayers(Packet packet, List<Player> players) {
			Set<String> ids = new HashSet<>();
			for(Player player : players){
				GamePlayer gamePlayer = (GamePlayer) player;
				ids.add(gamePlayer.participantId);
			}
			game.sendMessage(packet.isReliable(), new ArrayList<>(ids), PacketHelper.convertToBytes(packet));
		}

		@Override
		public Collection<? extends Player> getPlayers() {
			return players;
		}

		@Override
		public Collection<? extends Player> getHandledPlayers() {
			return handledPlayers;
		}

		@Override
		public Collection<? extends Player> getNonHandledPlayers() {
			return nonHandledPlayers;
		}
	}
	private static class GamePlayer implements Multiplayer.Player{
		private final String participantId;
		private final GameMultiplayer multiplayer;

		private GamePlayer(String participantId, GameMultiplayer multiplayer) {
			this.participantId = participantId;
			this.multiplayer = multiplayer;
		}

		@Override
		public boolean isConnected() {
			if(multiplayer.game.gameEnded){
				return false;
			}
			if(isHandledByUs()){
				return true;
			}
			return multiplayer.game.getRoom().getParticipant(participantId).isConnectedToRoom();
		}

		@Override
		public boolean isHandledByUs() {
			return participantId.equals(multiplayer.participantId);
		}

		@Override
		public boolean isPlayerHost() {
			return multiplayer.isHost();
		}
	}

}
