package it.polimi.ingsw.Client.Controller;

import it.polimi.ingsw.Client.GameClientListened;
import it.polimi.ingsw.Client.model.GameClient;
import it.polimi.ingsw.Client.model.PlayerClient;
import it.polimi.ingsw.Enum.Color;
import it.polimi.ingsw.Enum.HouseColor;
import it.polimi.ingsw.Enum.Wizard;
import it.polimi.ingsw.Server.controller.GameDelta;
import it.polimi.ingsw.Server.controller.MatchConstants;
import it.polimi.ingsw.Server.controller.MatchType;
import it.polimi.ingsw.Server.controller.Server;
import it.polimi.ingsw.Server.model.GameComponent;
import it.polimi.ingsw.network.toServerMessage.ToServerMessage;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Map;

public class ControllerClient extends GameClientListened {
    private final Socket socket;
    private ServerSender serverSender;
    private GameClient gameClient;
    private MatchType matchType;

    private final ArrayList<PlayerClient> playerClients;

    public ControllerClient() {
        socket = new Socket();
        playerClients = new ArrayList<>();
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

    public synchronized void sendMessage(ToServerMessage command) {
        if (serverSender == null)
            notify("Must connect to a Server Before");
        else {
            serverSender.sendServerMessage(command);
        }
    }

    public void error(String e) {
        notify(e);
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
