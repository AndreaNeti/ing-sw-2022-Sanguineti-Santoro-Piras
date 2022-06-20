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

    public ControllerClient() {
        this.chat = new LimitedChat<>(15);
    }

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

    public void addMessage(String message) {
        chat.add(new String(message.getBytes(StandardCharsets.UTF_8), StandardCharsets.UTF_8));
        notifyMessage(message);
    }

    public ArrayList<String> getChat() {
        return new ArrayList<>(chat);
    }

    //this is called when is received an ack, it set the next phase in order
    public void setNextClientPhase() {
        abstractView.setNextClientPhase();
    }

    public synchronized void changePhase(GamePhase newGamePhase, boolean setOldPhase, boolean forceImmediateExecution) {
        abstractView.setPhaseInView(newGamePhase, setOldPhase, forceImmediateExecution);
    }

    public void changePhase(GamePhase gamePhase, Byte currentPlayer, boolean forceImmediateExecution) {
        // se arriva il messaggio e se stesso è il current player si imposta fase del messaggio
        // currentPlayer!=se stesso-> wait Phase
        // this.gamePhase = gamePhase;
        if (currentPlayer == null) return;
        model.setCurrentPlayer(currentPlayer);
        if (model.getCurrentPlayer().getWizard() != myWizard) {
            changePhase(GamePhase.WAIT_PHASE, true, forceImmediateExecution);
        } else {
            changePhase(gamePhase, true, forceImmediateExecution);
        }
    }

    public void sendMessage(ToServerMessage command) {
        if (serverSender == null) error();
        else serverSender.sendServerMessage(command);
    }

    public void error() {
        unsetCurrentCharacterCard();
        abstractView.goToOldPhase();
    }

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
                model.setUpdatedCharacter(entry.getKey(), entry.getValue());
            }
        }

        for (Map.Entry<Byte, GameComponent> entry : gameDelta.getUpdatedGC().entrySet()) {
            model.setGameComponent(entry.getKey(), entry.getValue());
        }

        for (Map.Entry<Color, Wizard> entry : gameDelta.getUpdatedProfessors().entrySet()) {
            if (entry.getValue() != null) model.setProfessors(entry.getKey(), entry.getValue());
        }

        for (Byte b : gameDelta.getDeletedIslands()) {
            model.removeIsland(b);
        }
        // TODO may have broken everything
        gameDelta.getNewMotherNaturePosition().ifPresent(mnPosition -> model.setMotherNaturePosition(mnPosition));

        gameDelta.getPlayedCard().ifPresent(playedCard -> model.playCard(playedCard));

        for (Map.Entry<HouseColor, Byte> entry : gameDelta.getNewTeamTowersLeft().entrySet()) {
            model.setTowerLeft(entry.getKey(), entry.getValue());
        }
    }

    public void addMember(Player playerJoined, HouseColor teamColor) {
        teamsClient.get(teamColor.ordinal()).addPlayer(new PlayerClient(playerJoined));
        super.notifyMembers(matchType.nPlayers() - playersInMatch(), playerJoined.toString());
        if (playersInMatch() == matchType.nPlayers()) startGame();
    }

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

    private void startGame() {
        model = new GameClient(teamsClient, myWizard, matchType);
        //model.addListener(this);
        abstractView.setModel(model);
    }

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

    protected void closeConnection() {
        // quit connection to server
        serverListener.quit();
        serverSender.closeStream();
    }

    protected void unsetModel() {
        chat.clear();
        isInMatch = false;
        if (model != null) {
            model.deleteModel();
        }
        model = null;
        abstractView.setModel(null);
    }


    public void attachView(AbstractView view) {
        this.abstractView = view;
    }

    public void setCurrentCharacterCard(int currentCharacterCardIndex) {
        model.setCurrentCharacterCard(currentCharacterCardIndex);
    }

    private int playersInMatch() {
        int sum = 0;
        for (TeamClient t : teamsClient)
            sum += t.getPlayers().size();
        return sum;
    }

    public void unsetCurrentCharacterCard() {
        if (model != null) model.unsetCurrentCharacterCard();
    }
}
