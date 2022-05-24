package it.polimi.ingsw.Client.View;

import it.polimi.ingsw.Client.Controller.ControllerClient;
import it.polimi.ingsw.Client.LimitedChat;
import it.polimi.ingsw.Client.model.CharacterCardClient;
import it.polimi.ingsw.Client.model.GameClientView;
import it.polimi.ingsw.Enum.GamePhase;
import it.polimi.ingsw.network.toServerMessage.ToServerMessage;

import java.util.ArrayList;

public abstract class AbstractView {
    private final ControllerClient controllerClient;
    private GameClientView model;
    private volatile boolean quit;
    private final LimitedChat<String> chat;

    public AbstractView(ControllerClient controllerClient) {
        this.controllerClient = controllerClient;

        this.chat = new LimitedChat<>(15);
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
        chat.clear();
        // true quits all, only if called during init phase
        quit = controllerClient.setQuit(forceScannerSkip);
    }

    public boolean canQuit() {
        return quit;
    }

    public void setCurrentCharacterCard(int currentCharacterCardIndex) {
        controllerClient.setCurrentCharacterCard(currentCharacterCardIndex);
    }
    public void unsetCurrentCharacterCard(){
        controllerClient.unsetCurrentCharacterCard();
    }

    public CharacterCardClient getCurrentCharacterCard() {
        return getModel().getCurrentCharacterCard();
    }

    public void addMessage(String message) {
        chat.add(message);
        //controllerClient.repeatPhase(false);
    }

    public ArrayList<String> getChat() {
        return new ArrayList<>(chat);
    }

    public void clearChat() {
        chat.clear();
    }
}
