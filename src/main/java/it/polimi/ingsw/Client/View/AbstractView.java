package it.polimi.ingsw.Client.View;

import it.polimi.ingsw.Client.Controller.ControllerClient;
import it.polimi.ingsw.Client.model.CharacterCardClient;
import it.polimi.ingsw.Client.model.GameClientView;
import it.polimi.ingsw.Enum.GamePhase;
import it.polimi.ingsw.network.toServerMessage.Quit;
import it.polimi.ingsw.network.toServerMessage.ToServerMessage;

public abstract class AbstractView {
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

    public void setModel(GameClientView model) {
        this.model = model;
    }

    public void sendToServer(ToServerMessage toServerMessage) {
        controllerClient.sendMessage(toServerMessage);
    }

    public abstract void setPhaseInView(ClientPhaseView clientPhaseView, boolean forceScannerSkip);

    public void setPhaseInView(GamePhase gamePhase, boolean setOldPhase, boolean forceScannerSkip) {
        controllerClient.changePhase(gamePhase, setOldPhase, forceScannerSkip);
    }

    public void goToOldPhase(boolean forceScannerSkip) {
        controllerClient.repeatPhase(forceScannerSkip);
    }

    public boolean connectToServer(byte[] ipAddress) {
        return controllerClient.connect(ipAddress);
    }

    public abstract void start() throws InterruptedException;

    public void setQuit(boolean forceScannerSkip) {
        // true quits all, only if called during init phase
        quit = controllerClient.setQuit(forceScannerSkip);
    }

    public boolean canQuit() {
        return quit;
    }

    public void setCurrentCharacterCard(int currentCharacterCardIndex) {
        controllerClient.setCurrentCharacterCard(currentCharacterCardIndex);
    }

    public CharacterCardClient getCurrentCharacterCard() {
        return getModel().getCurrentCharacterCard();
    }
}
