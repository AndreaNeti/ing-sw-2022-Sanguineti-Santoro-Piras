package it.polimi.ingsw.Client.View;

import it.polimi.ingsw.Client.Controller.ControllerClient;
import it.polimi.ingsw.Client.model.GameClientView;
import it.polimi.ingsw.Enum.GamePhase;
import it.polimi.ingsw.Server.controller.MatchType;
import it.polimi.ingsw.network.toServerMessage.ToServerMessage;

public abstract class AbstractView implements GameClientListener {
    private final ControllerClient controllerClient;
    protected GamePhase gamePhase;
    private GameClientView model;

    public AbstractView(ControllerClient controllerClient) {
        this.controllerClient = controllerClient;
        gamePhase = GamePhase.INIT_PHASE;
    }

    protected GameClientView getModel() {
        return model;
    }

    @Override
    public void setModel(GameClientView model) {
        this.model = model;
    }

    protected ControllerClient getControllerClient() {
        return controllerClient;
    }

    public void sendToServer(ToServerMessage toServerMessage) {
        controllerClient.sendMessage(toServerMessage);
    }

    public abstract void setView(ClientPhaseView clientPhaseView);

    public boolean connectToServer(byte[] ipAddress, int port) {
        return controllerClient.connect(ipAddress, port);
    }

    public void setMatchType(MatchType mt) {
        controllerClient.setMatchType(mt);
    }
    public abstract void start();
}
