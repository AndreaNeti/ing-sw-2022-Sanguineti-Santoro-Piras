package it.polimi.ingsw.Client.Controller;

import it.polimi.ingsw.Client.GameClientListened;
import it.polimi.ingsw.Client.model.GameClient;
import it.polimi.ingsw.Client.model.PlayerClient;
import it.polimi.ingsw.Enum.Color;
import it.polimi.ingsw.Enum.GamePhase;
import it.polimi.ingsw.Enum.HouseColor;
import it.polimi.ingsw.Enum.Wizard;
import it.polimi.ingsw.Server.controller.GameDelta;
import it.polimi.ingsw.Server.controller.MatchConstants;
import it.polimi.ingsw.Server.controller.MatchType;
import it.polimi.ingsw.Server.controller.Server;
import it.polimi.ingsw.Server.model.GameComponent;
import it.polimi.ingsw.Server.model.Player;
import it.polimi.ingsw.network.toServerMessage.ToServerMessage;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ControllerClient extends GameClientListened {
    private final Socket socket;
    private ServerSender serverSender;
    private GameClient gameClient;
    private MatchType matchType;
    private ArrayList<PlayerClient> playerClients;
    private GamePhase oldPhase;
    private Wizard wizardLocal;

    public ControllerClient() {
        socket = new Socket();
        playerClients = new ArrayList<>();
        this.oldPhase = GamePhase.INIT_PHASE;
        wizardLocal = null;
    }

    public boolean connect(byte[] ipAddress, int port) {
        try {
            socket.connect(new InetSocketAddress(InetAddress.getByAddress(ipAddress), port));
        } catch (IOException | NumberFormatException e) {
            return false;
        }
        new Thread(new ServerListener(socket, this)).start();
        serverSender = new ServerSender(socket);

        System.out.println("connesso con server " + socket.getPort());
        return true;
    }

    public void setNewGamePhase() {
        //TODO check if this work with quit
        super.notify(GamePhase.values()[oldPhase.ordinal() + 1]);
        oldPhase = GamePhase.values()[oldPhase.ordinal() + 1];
    }

    public void changePhaseAndCurrentPlayer(GamePhase gamePhase, Byte currentPlayer) {
        //se arriva il messaggio e se stesso è il current player si imposta fase del messaggio
        //currentPlayer!=se stesso-> wait Phas
        // TODO set phase in view cli
        // this.gamePhase = gamePhase;
        gameClient.setCurrentPlayer(currentPlayer);
        if (gameClient.getCurrentPlayer().getWizard() != wizardLocal) {
            super.notify(GamePhase.WAIT_PHASE);
        } else {
            oldPhase = gamePhase;
            super.notify(gamePhase);
        }
    }

    public void addMembers(HashMap<Player, HouseColor> members) {
        this.playerClients = new ArrayList<>();
        if (wizardLocal == null) {
            wizardLocal = Wizard.values()[members.size() - 1];
        }
        for (Map.Entry<Player, HouseColor> entry : members.entrySet()) {
            playerClients.add(new PlayerClient(entry.getKey(), entry.getValue(), Server.getMatchConstants(matchType)));
        }
        if (playerClients.size() == matchType.nPlayers()) {
            //create a game
            gameClient = new GameClient(playerClients, Server.getMatchConstants(matchType));

        }
        super.notifyMembers(matchType.nPlayers() - playerClients.size());
    }

    public void setCurrentPlayer(byte currentPlayer) {
        gameClient.setCurrentPlayer(currentPlayer);
    }

    public synchronized void sendMessage(ToServerMessage command) {
        if (serverSender == null)
            error("Must connect to a Server Before");
        else {
            serverSender.sendServerMessage(command);
        }
    }

    public void error(String e) {
        notifyError(e);
    }

    public void ok() {
        notifyOk();
    }

    public synchronized void changeGame(GameDelta gameDelta) {
        if (gameClient == null) {
            //TODO set matchType when using the function createMatch in the view
            MatchConstants matchConstants = Server.getMatchConstants(matchType);

            gameClient = new GameClient(playerClients, matchConstants);
            gameClient.addListener(this);
        }

        for (Map.Entry<Byte, GameComponent> entry : gameDelta.getUpdatedGC().entrySet()) {
            gameClient.setGameComponent(entry.getKey(), entry.getValue());
        }

        for (Map.Entry<Color, Wizard> entry : gameDelta.getUpdatedProfessors().entrySet()) {
            gameClient.setProfessors(entry.getKey(), entry.getValue());
        }

        for (Byte b : gameDelta.getDeletedIslands()) {
            gameClient.removeIsland(b);
        }

        gameDelta.getNewMotherNaturePosition().ifPresent(mnPosition -> gameClient.setMotherNaturePosition(mnPosition));

        gameDelta.getPlayedCard().ifPresent(playedCard -> gameClient.playCard(playedCard));

        for (Map.Entry<HouseColor, Byte> entry : gameDelta.getNewTeamTowersLeft().entrySet()) {
            gameClient.setTowerLeft(entry.getKey(), entry.getValue());
        }
    }

    public void setMatchType(MatchType matchType) {
        this.matchType = matchType;
    }
}
