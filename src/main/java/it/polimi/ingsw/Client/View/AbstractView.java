package it.polimi.ingsw.Client.View;

import it.polimi.ingsw.Client.Controller.ControllerClient;
import it.polimi.ingsw.Client.model.GameClientView;
import it.polimi.ingsw.Enum.GamePhase;
import it.polimi.ingsw.Server.controller.MatchType;
import it.polimi.ingsw.network.toServerMessage.ToServerMessage;

public abstract class AbstractView implements GameClientListener {
    private final ControllerClient controllerClient;
    private GameClientView model;
    private volatile boolean quit;

    public AbstractView(ControllerClient controllerClient) {
        this.controllerClient = controllerClient;
        quit = false;
    }

    public GameClientView getModel() {
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

    public abstract void setPhaseInView(ClientPhaseView clientPhaseView, boolean notifyScanner);

    public void repeatPhase(boolean notifyScanner) {
        controllerClient.repeatPhase(notifyScanner);
    }

    public boolean connectToServer(byte[] ipAddress, int port) {
        return controllerClient.connect(ipAddress, port);
    }

    public void setMatchType(MatchType mt) {
        controllerClient.setMatchType(mt);
    }

    public abstract void start() throws InterruptedException;

    public void setQuit() {
        // true quits all, only if called during init phase
        quit = controllerClient.setQuit();
    }

    public boolean canQuit() {
        return quit;
    }
}
