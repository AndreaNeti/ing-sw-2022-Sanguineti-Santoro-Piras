package it.polimi.ingsw.Client.View;

import it.polimi.ingsw.Client.Controller.ControllerClient;
import it.polimi.ingsw.Client.PhaseAndComand.Commands.GameCommand;
import it.polimi.ingsw.Client.PhaseAndComand.Phases.*;
import it.polimi.ingsw.Client.model.CharacterCardClient;
import it.polimi.ingsw.Client.model.GameClientView;
import it.polimi.ingsw.Enum.GamePhase;
import it.polimi.ingsw.network.toServerMessage.ToServerMessage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static java.util.Map.entry;

public abstract class AbstractView {
    private final ControllerClient controllerClient;
    private GameClientView model;
    private volatile boolean quit;

    private boolean alreadyAttachedExpert = false;
    private Map<GamePhase, ClientPhase> phases;
    private GamePhase oldPhase, currentPhase;

    public AbstractView(ControllerClient controllerClient) {
        instantiateAllPhases();
        attachCommandToPhase();
        setPhaseInView(GamePhase.INIT_PHASE, true, false);
        this.controllerClient = controllerClient;
        quit = false;
    }

    private void instantiateAllPhases() {
        phases = Map.ofEntries(entry(GamePhase.INIT_PHASE, new InitPhase()), entry(GamePhase.NICK_PHASE, new NicknamePhase()), entry(GamePhase.SELECT_MATCH_PHASE, new SelectMatchPhase()), entry(GamePhase.WAIT_PHASE, new WaitPhase()), entry(GamePhase.PLANIFICATION_PHASE, new PlanificationPhase()), entry(GamePhase.MOVE_ST_PHASE, new MoveStudentsPhase()), entry(GamePhase.MOVE_MN_PHASE, new MoveMotherNaturePhase()), entry(GamePhase.MOVE_CL_PHASE, new MoveCloudPhase()), entry(GamePhase.PLAY_CH_CARD_PHASE, new PlayCharacterCardPhase()));
    }

    private void attachCommandToPhase() {
        GameCommand.CONNECT_SERVER.attachToAPhase(List.of(phases.get(GamePhase.INIT_PHASE)));
        GameCommand.SET_NICKNAME.attachToAPhase(List.of(phases.get(GamePhase.NICK_PHASE)));
        GameCommand.CREATE_MATCH.attachToAPhase(List.of(phases.get(GamePhase.SELECT_MATCH_PHASE)));
        GameCommand.JOIN_MATCH_BY_TYPE.attachToAPhase(List.of(phases.get(GamePhase.SELECT_MATCH_PHASE)));
        GameCommand.JOIN_MATCH_BY_ID.attachToAPhase(List.of(phases.get(GamePhase.SELECT_MATCH_PHASE)));
        GameCommand.PLAY_CARD.attachToAPhase(List.of(phases.get(GamePhase.PLANIFICATION_PHASE)));
        GameCommand.MOVE_STUDENT.attachToAPhase(List.of(phases.get(GamePhase.MOVE_ST_PHASE)));
        GameCommand.MOVE_MOTHER_NATURE.attachToAPhase(List.of(phases.get(GamePhase.MOVE_MN_PHASE)));
        GameCommand.MOVE_FROM_CLOUD.attachToAPhase(List.of(phases.get(GamePhase.MOVE_CL_PHASE)));
        GameCommand.TEXT_MESSAGE.attachToAPhase(List.of(phases.get(GamePhase.WAIT_PHASE), phases.get(GamePhase.PLANIFICATION_PHASE), phases.get(GamePhase.MOVE_ST_PHASE), phases.get(GamePhase.MOVE_MN_PHASE), phases.get(GamePhase.MOVE_CL_PHASE)));
        GameCommand.QUIT.attachToAPhase(new ArrayList<>(phases.values()));
    }

    private void attachExpertCommand() {
        //this is needed, so it doesn't attach commands multiple times
        if (!alreadyAttachedExpert) {
            GameCommand.CHOOSE_CHARACTER.attachToAPhase(Arrays.asList(phases.get(GamePhase.MOVE_ST_PHASE), phases.get(GamePhase.MOVE_CL_PHASE), phases.get(GamePhase.MOVE_MN_PHASE)));
            GameCommand.SET_CHARACTER_INPUT.attachToAPhase(List.of(phases.get(GamePhase.PLAY_CH_CARD_PHASE)));
            GameCommand.PLAY_CHARACTER.attachToAPhase(List.of(phases.get(GamePhase.PLAY_CH_CARD_PHASE)));
            GameCommand.UNDO.attachToAPhase(List.of(phases.get(GamePhase.PLAY_CH_CARD_PHASE)));
            alreadyAttachedExpert = true;
        }
    }

    public void repeatPhase(boolean forceImmediateExecution) {
        setPhaseInView(phases.get(currentPhase), forceImmediateExecution);
    }

    public void goToOldPhase() {
        setPhaseInView(phases.get(oldPhase), false);
    }

    public GamePhase getCurrentPhase() {
        return currentPhase;
    }

    public void setNextClientPhase() {
        GamePhase newPhase = GamePhase.values()[currentPhase.ordinal() + 1];
        currentPhase = newPhase;
        oldPhase = currentPhase;
        setPhaseInView(phases.get(newPhase), false);
    }

    public abstract void setPhaseInView(ClientPhase clientPhase, boolean forceImmediateExecution);

    public void setPhaseInView(GamePhase gamePhase, boolean setOldPhase, boolean forceImmediateExecution) {
        currentPhase = gamePhase;
        if (setOldPhase)
            oldPhase = currentPhase;
        setPhaseInView(phases.get(gamePhase), forceImmediateExecution);

    }

    public GameClientView getModel() {
        if (model == null)
            return null;
        synchronized (model) {
            return model;
        }
    }

    public void setModel(GameClientView model) {
        this.model = model;
        if (model != null && model.isExpert())
            attachExpertCommand();
    }

    public void sendToServer(ToServerMessage toServerMessage) {
        controllerClient.sendMessage(toServerMessage);
    }

    public boolean connectToServer(byte[] ipAddress) {
        return controllerClient.connect(ipAddress);
    }

    public abstract void start() throws InterruptedException;

    public void setQuit(boolean forceImmediateExecution) {
        // true quits all, only if called during init phase
        quit = controllerClient.setQuit(forceImmediateExecution);
    }

    public boolean canQuit() {
        return quit;
    }

    public void setCurrentCharacterCard(int currentCharacterCardIndex) {
        controllerClient.setCurrentCharacterCard(currentCharacterCardIndex);
    }

    public void unsetCurrentCharacterCard() {
        controllerClient.unsetCurrentCharacterCard();
    }

    public CharacterCardClient getCurrentCharacterCard() {
        if (getModel() == null) return null;
        return getModel().getCurrentCharacterCard();
    }

    public void addMessage(String message) {
        controllerClient.addMessage(message);
    }

    public ArrayList<String> getChat() {
        return controllerClient.getChat();
    }

}
