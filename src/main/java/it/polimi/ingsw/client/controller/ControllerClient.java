package it.polimi.ingsw.client.controller;

import it.polimi.ingsw.client.GameClientListened;
import it.polimi.ingsw.client.model.GameClient;
import it.polimi.ingsw.client.model.PlayerClient;
import it.polimi.ingsw.client.model.TeamClient;
import it.polimi.ingsw.client.view.AbstractView;
import it.polimi.ingsw.network.GameDelta;
import it.polimi.ingsw.network.PingMessage;
import it.polimi.ingsw.network.PingPong;
import it.polimi.ingsw.network.PingPongInterface;
import it.polimi.ingsw.network.toServerMessage.Quit;
import it.polimi.ingsw.network.toServerMessage.ToServerMessage;
import it.polimi.ingsw.server.controller.Controller;
import it.polimi.ingsw.server.controller.Server;
import it.polimi.ingsw.server.model.Player;
import it.polimi.ingsw.server.model.Team;
import it.polimi.ingsw.server.model.gameComponents.GameComponent;
import it.polimi.ingsw.utils.*;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * ControllerClient class represents the controller used by the client to interact with its game. <br>
 * It contains the game's instance, with all the info necessary and also contains instances of the client's view and
 * the server listener and sender. <br>
 * Similarly to the server class {@link Controller}, this class contains the client's game phase logic and
 * also updates the game based on the info received through delta update.
 */
public class ControllerClient extends GameClientListened implements PingPongInterface {
    private ServerSender serverSender;
    private GameClient model;
    private MatchType matchType;
    private MatchConstants matchConstants;
    private List<TeamClient> teamsClient;
    private Wizard myWizard;
    private ServerListener serverListener;
    private AbstractView abstractView;
    private boolean isInMatch;
    private final LimitedChat<String> chat;
    private PingPong pingPong;

    /**
     * Constructor ControllerClient creates a new instance of ControllerClient.
     */
    public ControllerClient() {
        this.chat = new LimitedChat<>(15);
    }

    /**
     * Method connect is used to open a socket connection between the client and the server with the IP address provided.
     *
     * @param ipAddress of type {@code byte[]} - array of bytes equivalent to the IP address.
     * @return {@code boolean} - true if the connection was established correctly, false else.
     */
    public boolean connect(byte[] ipAddress) {
        Socket socket;
        try {
            socket = new Socket();
            socket.connect(new InetSocketAddress(InetAddress.getByAddress(ipAddress), Server.serverPort), 5000);
            pingPong = new PingPong(this);
        } catch (IOException | NumberFormatException e) {
            return false;
        }
        serverListener = new ServerListener(socket, this);
        new Thread(serverListener).start();
        serverSender = new ServerSender(socket);
        return true;
    }

    /**
     * Method addMessage adds a message to the client's chat and notifies it.
     *
     * @param message of type {@code String} - text message to add.
     */
    public void addMessage(String message) {
        chat.add(new String(message.getBytes(StandardCharsets.UTF_8), StandardCharsets.UTF_8));
        notifyMessage(message);
    }

    /**
     * Method getChat returns the client's chat.
     *
     * @return {@code List}<{@code String}> - copy of the chat saved in a list.
     */
    public List<String> getChat() {
        return new ArrayList<>(chat);
    }

    /**
     * Method setNextClientPhase updates the client's phase to the next one in the view.
     */
    //this is called when is received an ack, it set the next phase in order
    public void setNextClientPhase() {
        abstractView.setNextClientPhase();
    }

    /**
     * Method changePhase updates the client's phase in the view to the provided one, also specifying if the
     * old phase must be saved and if the phase change must happen immediately.
     *
     * @param newGamePhase            of type {@link GamePhase} - new game phase to set in the client's view.
     * @param forceImmediateExecution of type {@code boolean} - boolean to check if the phase must change immediately.
     */
    public synchronized void changePhase(GamePhase newGamePhase, boolean forceImmediateExecution) {
        abstractView.setPhaseInView(newGamePhase, forceImmediateExecution);
    }

    /**
     * Method changePhase updates the game phase to the provided one. <br>
     * If the client's player is the current one playing its turn the client's phase is set to the
     * provided one as well, else it is set to wait phase.
     *
     * @param newGamePhase            of type {@link GamePhase} - new game phase to set in the game.
     * @param currentPlayerIndex      of type {@code Byte} - index of the current player.
     * @param forceImmediateExecution of type {@code boolean} - boolean to check if the phase must change immediately.
     */
    public void changePhase(GamePhase newGamePhase, Byte currentPlayerIndex, boolean forceImmediateExecution) {
        // se arriva il messaggio e se stesso è il current player si imposta fase del messaggio
        // currentPlayer!=se stesso-> wait Phase
        // this.gamePhase = gamePhase;
        if (currentPlayerIndex == null) return;
        model.setCurrentPlayer(currentPlayerIndex);
        if (model.getCurrentPlayer().getWizard() != myWizard) {
            changePhase(GamePhase.WAIT_PHASE, forceImmediateExecution);
        } else {
            changePhase(newGamePhase, forceImmediateExecution);
        }
    }

    /**
     * Method sendMessage is used by the controller to send a message with a command to be executed by the server.
     *
     * @param command of type {@link ToServerMessage} - instance of the command to execute.
     */
    public void sendMessage(ToServerMessage command) {
        if (serverSender == null) error("Can't send to server, server sender null");
        else serverSender.sendServerMessage(command);
    }

    /**
     * Method error is called when an error occurs updating that error to the view
     */
    public void error(String error) {
        notifyError(error);
    }

    /**
     * Method changeGame receives a gameDelta and updates the controller's game based on the info contained in it. <br>
     * If a specific info in the delta is not present the game won't update it.
     *
     * @param gameDelta of type {@link GameDelta} - instance of the gameDelta received by the server with the new game info.
     */
    public void changeGame(GameDelta gameDelta) {
        if (matchType.isExpert()) {
            gameDelta.getNewCoinsLeft().ifPresent(newCoinsLeft -> model.setNewCoinsLeft(newCoinsLeft));
            gameDelta.getNewProhibitionsLeft().ifPresent(newProhibitionsLeft -> model.setNewProhibitionsLeft(newProhibitionsLeft));
            gameDelta.isExtraSteps().ifPresent((extraSteps) -> model.setExtraSteps(extraSteps));
            gameDelta.getIgnoredColorInfluence().ifPresent((ignoredColorInfluence) -> model.setIgnoredColorInfluence(ignoredColorInfluence));
            for (Map.Entry<Byte, Integer> newEntry : gameDelta.getUpdatedCoinPlayer().entrySet())
                model.setUpdatedCoinsPlayer(newEntry.getKey(), newEntry.getValue());
            for (CharacterCardDataInterface updatedCharacter : gameDelta.getCharacters()) {
                model.setUpdatedCharacter(updatedCharacter);
            }
        }
        for (Map.Entry<Byte, GameComponent> entry : gameDelta.getUpdatedGC().entrySet()) {
            model.setGameComponent(entry.getKey(), entry.getValue());
        }

        for (Map.Entry<Color, Wizard> entry : gameDelta.getUpdatedProfessors().entrySet()) {
            if (entry.getValue() != null) model.setProfessors(entry.getKey(), entry.getValue());
        }

        gameDelta.getNewMotherNaturePosition().ifPresent(mnPosition -> model.setMotherNaturePosition(mnPosition));

        gameDelta.getDeletedIslands().ifPresent(deletedIsland -> model.removeIslands(deletedIsland));

        gameDelta.getPlayedCard().ifPresent(playedCard -> model.playCard(playedCard));

        for (Map.Entry<HouseColor, Byte> entry : gameDelta.getNewTeamTowersLeft().entrySet()) {
            model.setTowerLeft(entry.getKey(), entry.getValue());
        }
    }

    /**
     * Method addMember adds a player to the controller's game, notifying the client and starting the
     * game if the game has all the players required.
     *
     * @param playerJoined of type {@link Player} - instance of the player that joined the game.
     * @param teamColor    of type {@link HouseColor} - color of the player's team.
     */
    public void addMember(Player playerJoined, HouseColor teamColor) {
        PlayerClient newPlayer = new PlayerClient(playerJoined);
        teamsClient.get(teamColor.ordinal()).addPlayer(newPlayer);
        addMessage(playerJoined + " joined the match");
        addMessage("Members left: " + (matchType.nPlayers() - playersInMatch()));
        notifyMembers(matchType.nPlayers() - playersInMatch(), newPlayer);
        if (playersInMatch() == matchType.nPlayers()) startGame();
    }

    /**
     * Method setMatchInfo updates the controller info about the game.
     *
     * @param matchType of type {@link MatchType} - type of the controller's game.
     * @param teams     of type {@code List}<{@link Team}> - list of instance of the game's teams.
     * @param myWizard  of type {@link Wizard} - wizard associated with the client's player.
     */
    public void setMatchInfo(MatchType matchType, MatchConstants constants, List<Team> teams, Wizard myWizard) {
        this.myWizard = myWizard;
        isInMatch = true;
        this.matchType = matchType;
        this.matchConstants = constants;
        teamsClient = new ArrayList<>();
        for (Team t : teams)
            teamsClient.add(new TeamClient(t.getHouseColor(), t.getPlayers(), matchConstants));
        notifyMatchInfo(matchType, constants, teamsClient);
        if (playersInMatch() == matchType.nPlayers()) startGame();
    }

    /**
     * Method startGame creates starts the game, creating a client game instance for the client's view to observe.
     */
    private void startGame() {
        model = new GameClient(teamsClient, myWizard, matchType, matchConstants);
        //model.addListener(this);
        abstractView.setModel(model);
    }

    /**
     * Method setQuit quits the client from the game if already in one, else will close the connection returning to the init
     * phase.
     *
     * @param forceImmediateExecution of type {@code boolean} - boolean to check if the phase change after quitting the game
     *                                should happen instantly.
     * @return {@code boolean} - true if the client process has to quit the application (already in init phase)
     * , false if the client quits from the game or closes the connection.
     */
    // returns true if the client process has to quit
    public synchronized boolean setQuit(boolean forceImmediateExecution) {
        if (isInMatch) {
            sendMessage(new Quit());
            unsetModel();
            changePhase(GamePhase.SELECT_MATCH_PHASE, forceImmediateExecution);
            return false;
        } else if (abstractView.getCurrentPhase() != GamePhase.INIT_PHASE) {
            chat.clear();
            sendMessage(new Quit());
            closeConnection();
            return false;
        } else return true;
    }

    /**
     * Method closeConnection closes the connection between the client and the server, closing the server listener's thread
     * and the server sender output stream.
     */
    protected void closeConnection() {
        // quit connection to server
        pingPong.quit();
        serverListener.quit();
        serverSender.closeStream();
    }

    /**
     * Method unsetModel deletes the model of the client, effectively quitting from the game it's currently in.
     */
    protected void unsetModel() {
        chat.clear();
        isInMatch = false;
        if (model != null) {
            model.deleteModel();
        }
        model = null;
        abstractView.setModel(null);
    }


    /**
     * Method attachView adds a view (CLI or GUI) to the client's controller.
     *
     * @param view of type {@link AbstractView} - instance of the view to attach.
     */
    public void attachView(AbstractView view) {
        this.abstractView = view;
    }

    /**
     * Method setCurrentCharacterCard updates the character card currently being played in the game, based on the index provided.
     *
     * @param currentCharacterCardIndex of type {@code int} - index of the character card being played.
     */
    public void setCurrentCharacterCard(int currentCharacterCardIndex) {
        model.setCurrentCharacterCard(currentCharacterCardIndex);
    }

    /**
     * Method playersInMatch returns the number of players currently in the game.
     *
     * @return {@code int} - number of players currently in the game.
     */
    private int playersInMatch() {
        int sum = 0;
        for (TeamClient t : teamsClient)
            sum += t.getPlayers().size();
        return sum;
    }

    /**
     * Method unsetCurrentCharacterCard sets to null the character card currently being played in the game.
     */
    public void unsetCurrentCharacterCard() {
        if (model != null) model.unsetCurrentCharacterCard();
    }


    /**
     * Method sendPingPong sends a {@link PingMessage} to the server.
     */
    @Override
    public void sendPingPong() {
        sendMessage(new PingMessage());
    }

    /**
     * Method quit closes the connection between the client and the server.
     */
    @Override
    public void quit() {
        closeConnection();
    }

    @Override
    public void resetPing() {
        pingPong.resetTime();
    }
}
