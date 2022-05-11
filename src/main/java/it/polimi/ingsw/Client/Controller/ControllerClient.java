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
import it.polimi.ingsw.Server.model.*;
import it.polimi.ingsw.network.toServerMessage.*;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ControllerClient extends GameClientListened {
    private Socket socket;
    private ServerListener serverListener;
    private ServerSender serverSender;
    private Thread t;
    private GameClient gameClient;
    private MatchType matchType;


    public boolean connect(int port) {
        try {
            socket = new Socket("localhost", port);
        } catch (IOException|NumberFormatException e) {
            return false;
        }
        t=new Thread(new ServerListener(socket,this));
        t.start();
        serverSender=new ServerSender(socket);

        System.out.println("connesso con server " + socket.getPort());
        return true;
    }

    public synchronized void sendMessage(ToServerMessage command){
        if(serverSender==null)
            notify("Must connect to a Server Before");
        else{
            serverSender.sendServerMessage(command);
        }
    }
    public void error(String e) {
        notify(e);
    }
    public void ok(){
        notifyOk();
    }
    public synchronized void changeGame(GameDelta gameDelta){
        if(gameClient==null) {
            //TODO set matchType when using the function createMatch in the view
            MatchConstants matchConstants = Server.getMatchConstants(matchType);
            ArrayList<PlayerClient> playerClients=null;
            ArrayList<Player> players;
            HashMap<Player, HouseColor> members = gameDelta.getMembers();
            if (members != null) {
                playerClients = new ArrayList<>();
                for (Player p : members.keySet()) {
                    //TODO add constructor in playerClient that receive a player
                    playerClients.add(new PlayerClient(p.getNickName(), p.getWizard(), (byte) matchConstants.numOfCards(), (byte) matchConstants.towersForTeam(), members.get(p)));
                }
            }
            gameClient=new GameClient(playerClients,matchConstants);
            gameClient.addListener(this);
        }
        if(gameDelta.getUpdatedGC()!=null){
            for (Map.Entry<Byte,GameComponent> entry:gameDelta.getUpdatedGC().entrySet())
            {
                gameClient.setGameComponent(entry.getKey(), entry.getValue());
            }
        }
        if(gameDelta.getUpdatedProfessors()!=null){
            for (Map.Entry<Color, Wizard> entry:gameDelta.getUpdatedProfessors().entrySet())
            {
                gameClient.setProfessors(entry.getKey(), entry.getValue());
            }
        }
        if(gameDelta.getDeletedIslands()!=null){
            for (Byte b:gameDelta.getDeletedIslands()) {
                gameClient.removeIsland(b);
            }
        }
        if(gameDelta.getNewMotherNaturePosition()!=null){
            gameClient.setMotherNaturePosition(gameDelta.getNewMotherNaturePosition());
        }
        if(gameDelta.getNewCurrentPlayer()!=null){
            gameClient.setCurrentPlayer(gameDelta.getNewCurrentPlayer());
        }
        if(gameDelta.getNewTeamTowersLeft()!=null){
            for (Map.Entry<HouseColor,Byte> entry:gameDelta.getNewTeamTowersLeft().entrySet())
            {
                gameClient.setTowerLeft(entry.getKey(), entry.getValue());
            }
        }
        if(gameDelta.getPlayedCard()!=null){
            gameClient.playCard(gameDelta.getPlayedCard());
        }
    }
    public void setMatchType(MatchType matchType) {
        this.matchType = matchType;
    }
}
