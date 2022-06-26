package it.polimi.ingsw.Client.Controller;

import it.polimi.ingsw.Client.GameClientListened;
import it.polimi.ingsw.Client.LimitedChat;
import it.polimi.ingsw.Client.View.AbstractView;
import it.polimi.ingsw.Client.model.GameClient;
import it.polimi.ingsw.Client.model.PlayerClient;
import it.polimi.ingsw.Client.model.TeamClient;
import it.polimi.ingsw.Server.controller.GameDelta;
import it.polimi.ingsw.Server.controller.MatchConstants;
import it.polimi.ingsw.Server.controller.MatchType;
import it.polimi.ingsw.Server.controller.Server;
import it.polimi.ingsw.Server.model.GameComponents.GameComponent;
import it.polimi.ingsw.Server.model.Player;
import it.polimi.ingsw.Server.model.Team;
import it.polimi.ingsw.Server.controller.Controller;
import it.polimi.ingsw.Util.Color;
import it.polimi.ingsw.Util.GamePhase;
import it.polimi.ingsw.Util.HouseColor;
import it.polimi.ingsw.Util.Wizard;
import it.polimi.ingsw.network.toServerMessage.Quit;
import it.polimi.ingsw.network.toServerMessage.ToServerMessage;

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
public class ControllerClient extends GameClientListened {
    private ServerSender serverSender;
    private GameClient model;
    private MatchType matchType;
    private MatchConstants matchConstants;
    private ArrayList<TeamClient> teamsClient;
    private Wizard myWizard;
    private ServerListener serverListener;
    private AbstractView abstractView;
    private boolean isInMatch;
    private final LimitedChat<String> chat;

    /**
     * Constructor ControllerClient creates a new isntance of ControllerClient.
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
     * @return {@code ArrayList}<{@code String}> - copy of the chat saved in a list.
     */
    public ArrayList<String> getChat() {
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
     * @param newGamePhase of type {@link GamePhase} - new game phase to set in the client's view.
     * @param setOldPhase of type {@code boolean} - boolean to check if old phase must be saved.
     * @param forceImmediateExecution of type {@code boolean} - boolean to check if the phase must change immediately.
     */
    public synchronized void changePhase(GamePhase newGamePhase, boolean setOldPhase, boolean forceImmediateExecution) {
        abstractView.setPhaseInView(newGamePhase, setOldPhase, forceImmediateExecution);
    }

    /**
     * Method changePhase updates the game phase to the provided one. <br>
     * If the client's player is the current one playing its turn the client's phase is set to the
     * provided one as well, else it is set to wait phase.
     *
     * @param newGamePhase of type {@link GamePhase} - new game phase to set in the game.
     * @param currentPlayerIndex of type {@code Byte} - index of the current player.
     * @param forceImmediateExecution of type {@code boolean} - boolean to check if the phase must change immediately.
     */
    public void changePhase(GamePhase newGamePhase, Byte currentPlayerIndex, boolean forceImmediateExecution) {
        // se arriva il messaggio e se stesso è il current player si imposta fase del messaggio
        // currentPlayer!=se stesso-> wait Phase
        // this.gamePhase = gamePhase;
        if (currentPlayerIndex == null) return;
        model.setCurrentPlayer(currentPlayerIndex);
        if (model.getCurrentPlayer().getWizard() != myWizard) {
            changePhase(GamePhase.WAIT_PHASE, true, forceImmediateExecution);
        } else {
            changePhase(newGamePhase, true, forceImmediateExecution);
        }
    }

    /**
     * Method sendMessage is used by the controller to send a message with a command to be executed by the server.
     *
     * @param command of type {@link ToServerMessage} - instance of the command to execute.
     */
    public void sendMessage(ToServerMessage command) {
        if (serverSender == null) error();
        else serverSender.sendServerMessage(command);
    }

    /**
     * Method error is called when an error occurs, resetting the current character card and returning to the old game phase.
     */
    public void error() {
        unsetCurrentCharacterCard();
        abstractView.goToOldPhase();
    }

    /**
     * Method changeGame receives a gameDelta and updates the controller's game based on the info contained in it. <br>
     * If a specific info in the delta is not present the game won't update it.
     *
     * @param gameDelta of type {@link GameDelta} - instance of the gameDelta received by the server with the new game info.
     */
    public void changeGame(GameDelta gameDelta) {
        if (matchType.isExpert()) {
            if (gameDelta.getCharacters().size() != 0) model.setCharacters(gameDelta.getCharacters());
            gameDelta.getNewCoinsLeft().ifPresent(newCoinsLeft -> model.setNewCoinsLeft(newCoinsLeft));
            gameDelta.getNewProhibitionsLeft().ifPresent(newProhibitionsLeft -> model.setNewProhibitionsLeft(newProhibitionsLeft));
            gameDelta.isExtraSteps().ifPresent((extraSteps) -> model.setExtraSteps(extraSteps));
            gameDelta.getIgnoredColorInfluence().ifPresent((ignoredColorInfluence) -> model.setIgnoredColorInfluence(ignoredColorInfluence));
            for (Map.Entry<Byte, Byte> newEntry : gameDelta.getUpdatedCoinPlayer().entrySet())
                model.setUpdatedCoinPlayer(newEntry.getKey(), newEntry.getValue());
            for (Map.Entry<Byte, Boolean> entry : gameDelta.getUsedCharacter().entrySet()) {
                model.setUpdatedCharacter(entry.getKey());
            }
        }

        for (Map.Entry<Byte, GameComponent> entry : gameDelta.getUpdatedGC().entrySet()) {
            model.setGameComponent(entry.getKey(), entry.getValue());
        }

        for (Map.Entry<Color, Wizard> entry : gameDelta.getUpdatedProfessors().entrySet()) {
            if (entry.getValue() != null) model.setProfessors(entry.getKey(), entry.getValue());
        }

        gameDelta.getNewMotherNaturePosition().ifPresent(mnPosition -> model.setMotherNaturePosition(mnPosition));

        for (Byte b : gameDelta.getDeletedIslands()) {
            model.removeIsland(b);
        }

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
     * @param teamColor of type {@link HouseColor} - color of the player's team.
     */
    public void addMember(Player playerJoined, HouseColor teamColor) {
        teamsClient.get(teamColor.ordinal()).addPlayer(new PlayerClient(playerJoined));
        super.notifyMembers(matchType.nPlayers() - playersInMatch(), playerJoined.toString());
        if (playersInMatch() == matchType.nPlayers()) startGame();
    }

    /**
     * Method setMatchInfo updates the controller info about the game.
     *
     * @param matchType of type {@link MatchType} - type of the controller's game.
     * @param teams of type {@code List}<{@link Team}> - list of instance of the game's teams.
     * @param myWizard of type {@link Wizard} - wizard associated with the client's player.
     */
    public void setMatchInfo(MatchType matchType, List<Team> teams, Wizard myWizard) {
        this.myWizard = myWizard;
        isInMatch = true;
        this.matchType = matchType;
        this.matchConstants = Server.getMatchConstants(matchType);
        teamsClient = new ArrayList<>();
        for (Team t : teams)
            teamsClient.add(new TeamClient(t.getHouseColor(), t.getPlayers(), matchConstants));
        notifyMembers(matchType.nPlayers() - playersInMatch(), "You");
        if (playersInMatch() == matchType.nPlayers()) startGame();
    }

    /**
     * Method startGame creates starts the game, creating a client game instance for the client's view to observe.
     */
    private void startGame() {
        model = new GameClient(teamsClient, myWizard, matchType);
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
            changePhase(GamePhase.SELECT_MATCH_PHASE, true, forceImmediateExecution);
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
}
