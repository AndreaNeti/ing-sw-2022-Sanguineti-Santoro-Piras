package it.polimi.ingsw.Client.View;

import it.polimi.ingsw.Client.Controller.ControllerClient;
import it.polimi.ingsw.Server.controller.MatchType;
import it.polimi.ingsw.network.toServerMessage.ToServerMessage;

public abstract class AbstractView implements GameClientListener {
    protected ControllerClient controllerClient;
    public void sendToServer(ToServerMessage toServerMessage){
        controllerClient.sendMessage(toServerMessage);
    }
    public abstract void setView(GamePhaseView gamePhaseView);
    public boolean connectToServer(byte[] ipAddress, int port){
        return controllerClient.connect(ipAddress,port);
    }
    public void setMatchType(MatchType mt){
        controllerClient.setMatchType(mt);
    }
}
