package it.polimi.ingsw.Client.View;

import it.polimi.ingsw.Client.Controller.ControllerClient;
import it.polimi.ingsw.Client.PhaseAndComand.Commands.GameCommand;
import it.polimi.ingsw.Client.PhaseAndComand.Phases.*;
import it.polimi.ingsw.Client.model.CharacterCardClient;
import it.polimi.ingsw.Client.model.GameClientView;
import it.polimi.ingsw.Util.GamePhase;
import it.polimi.ingsw.network.toServerMessage.ToServerMessage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static java.util.Map.entry;

/**
 * AbstractView abstract class represents the client's view that observes the game and contains info about the phases. <br>
 * This class also contains the instances of both the game's model and the client's controller.
 * Most methods are indeed used to get info about the game and to modify it through the controller. <br>
 * All available client's views (GUI and CLI) extend this abstract class. <br>
 */
public abstract class AbstractView {
    private final ControllerClient controllerClient;
    private GameClientView model;
    private volatile boolean quit;
    private boolean alreadyAttachedExpert = false;
    private Map<GamePhase, ClientPhase> phases;
    private GamePhase oldPhase, currentPhase;

    /**
     * Constructor AbstractView creates a new instance of AbstractView.
     *
     * @param controllerClient of type {@link ControllerClient} - instance of the client's controller.
     */
    public AbstractView(ControllerClient controllerClient) {
        instantiateAllPhases();
        attachCommandToPhase();
        this.controllerClient = controllerClient;
        quit = false;
    }

    /**
     * Method instantiateAllPhases creates a new instance of each game phase and adds it to a map. <br>
     * <b>Map Entry</b>: (game phase enum ordinal, instance of phase).
     */
    private void instantiateAllPhases() {
        phases = Map.ofEntries(entry(GamePhase.INIT_PHASE, new InitPhase()), entry(GamePhase.NICK_PHASE, new NicknamePhase()), entry(GamePhase.SELECT_MATCH_PHASE, new SelectMatchPhase()), entry(GamePhase.WAIT_PHASE, new WaitPhase()), entry(GamePhase.PLANIFICATION_PHASE, new PlanificationPhase()), entry(GamePhase.MOVE_ST_PHASE, new MoveStudentsPhase()), entry(GamePhase.MOVE_MN_PHASE, new MoveMotherNaturePhase()), entry(GamePhase.MOVE_CL_PHASE, new MoveCloudPhase()), entry(GamePhase.PLAY_CH_CARD_PHASE, new PlayCharacterCardPhase()));
    }

    /**
     * Method attachCommandToPhase adds each command to the phase it should be available to be executed. <br>
     * <b>List of phases for each command:</b> <br>
     * <b>CONNECT_SERVER</b> => INIT_PHASE <br>
     * <b>SET_NICKNAME</b> => NICK_PHASE <br>
     * <b>CREATE_MATCH</b> => SELECT_MATCH_PHASE <br>
     * <b>JOIN_MATCH_BY_TYPE</b> => SELECT_MATCH_PHASE <br>
     * <b>JOIN_MATCH_BY_ID</b> => SELECT_MATCH_PHASE <br>
     * <b>PLAY_CARD</b> => PLANIFICATION_PHASE <br>
     * <b>MOVE_STUDENT</b> => MOVE_ST_PHASE <br>
     * <b>MOVE_MOTHER_NATURE</b> => MOVE_MN_PHASE <br>
     * <b>MOVE_FROM_CLOUD</b> => MOVE_CL_PHASE <br>
     * <b>TEXT_MESSAGE</b> => WAIT_PHASE, PLANIFICATION_PHASE, MOVE_ST_PHASE, MOVE_MN_PHASE, MOVE_CL_PHASE <br>
     * <b>QUIT</b> => All game phases <br>
     */
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
        GameCommand.TEXT_MESSAGE.attachToAPhase(List.of(phases.get(GamePhase.WAIT_PHASE), phases.get(GamePhase.PLANIFICATION_PHASE),
                phases.get(GamePhase.MOVE_ST_PHASE), phases.get(GamePhase.MOVE_MN_PHASE), phases.get(GamePhase.MOVE_CL_PHASE)));
        GameCommand.QUIT.attachToAPhase(new ArrayList<>(phases.values()));
    }

    /**
     * Method attachExpertCommand adds each expert game command to the phase it should be available to be executed. <br>
     * <b>List of phases for each command:</b> <br>
     * <b>CHOOSE_CHARACTER</b> => MOVE_ST_PHASE, MOVE_CL_PHASE, MOVE_MN_PHASE <br>
     * <b>SET_CHARACTER_INPUT</b> => PLAY_CH_CARD_PHASE <br>
     * <b>PLAY_CHARACTER</b> => PLAY_CH_CARD_PHASE <br>
     * <b>UNDO</b> => PLAY_CH_CARD_PHASE <br>
     */
    private void attachExpertCommand() {
        //this is needed, so it doesn't attach commands multiple times
        if (!alreadyAttachedExpert) {
            GameCommand.CHOOSE_CHARACTER.attachToAPhase(Arrays.asList(phases.get(GamePhase.MOVE_ST_PHASE),
                    phases.get(GamePhase.MOVE_CL_PHASE), phases.get(GamePhase.MOVE_MN_PHASE)));
            GameCommand.SET_CHARACTER_INPUT.attachToAPhase(List.of(phases.get(GamePhase.PLAY_CH_CARD_PHASE)));
            GameCommand.PLAY_CHARACTER.attachToAPhase(List.of(phases.get(GamePhase.PLAY_CH_CARD_PHASE)));
            GameCommand.UNDO.attachToAPhase(List.of(phases.get(GamePhase.PLAY_CH_CARD_PHASE)));
            alreadyAttachedExpert = true;
        }
    }

    /**
     * Method repeatPhase sets the phase in the view to the current one, effectively repeating the phase.
     *
     * @param forceImmediateExecution of type {@code boolean} - boolean to check if the phase must change immediately.
     */
    public void repeatPhase(boolean forceImmediateExecution) {
        setPhaseInView(phases.get(currentPhase), forceImmediateExecution);
    }

    /**
     * Method goToOldPhase sets the phase in the view to the old one.
     */
    public void goToOldPhase() {
        setPhaseInView(phases.get(oldPhase), false);
        currentPhase = oldPhase;
    }

    /**
     * Method getCurrentPhase returns the current phase in which the client is.
     *
     * @return {@link GamePhase} - current game phase.
     */
    public GamePhase getCurrentPhase() {
        return currentPhase;
    }

    /**
     * Method setNextClientPhase updates the game phase to the next one.
     */
    public void setNextClientPhase() {
        GamePhase newPhase = GamePhase.values()[currentPhase.ordinal() + 1];
        currentPhase = newPhase;
        oldPhase = currentPhase;
        setPhaseInView(phases.get(newPhase), false);
    }

    /**
     * Method setPhaseInView updates the phase to the provided one, also specifying if the phase change must happen immediately.
     *
     * @param clientPhase of type {@link ClientPhase} - instance of the new client phase to set.
     * @param forceImmediateExecution of type {@code boolean} - boolean to check if the phase must change immediately.
     */
    public abstract void setPhaseInView(ClientPhase clientPhase, boolean forceImmediateExecution);

    /**
     * Method setPhaseInView updates the phase to the provided one, also specifying if the
     * old phase must be saved and if the phase change must happen immediately.
     *
     * @param newGamePhase of type {@link GamePhase} - new game phase to set in the client's view.
     * @param setOldPhase of type {@code boolean} - boolean to check if old phase must be saved.
     * @param forceImmediateExecution of type {@code boolean} - boolean to check if the phase must change immediately.
     */
    public void setPhaseInView(GamePhase newGamePhase, boolean setOldPhase, boolean forceImmediateExecution) {
        currentPhase = newGamePhase;
        if (setOldPhase)
            oldPhase = currentPhase;
        setPhaseInView(phases.get(newGamePhase), forceImmediateExecution);
    }

    /**
     * Method getModel returns the client's game model that the view is observing.
     *
     * @return {@link GameClientView} - instance of the client's game.
     */
    public GameClientView getModel() {
        if (model == null)
            return null;
        synchronized (model) {
            return model;
        }
    }

    /**
     * Method setModel sets the view's model to a provided one and if it is expert also attaches the expert commands to its phases.
     *
     * @param model of type {@link GameClientView} - instance of the new model observed by the view.
     */
    public void setModel(GameClientView model) {
        this.model = model;
        if (model != null && model.isExpert())
            attachExpertCommand();
    }

    /**
     * Method sendToServer is used by the view to send a message with a command, through the client's controller, to be executed by the server.
     *
     * @param toServerMessage of type {@link ToServerMessage} - instance of the command to execute.
     */
    public void sendToServer(ToServerMessage toServerMessage) {
        controllerClient.sendMessage(toServerMessage);
    }

    /**
     * Method connectToServer is used by the view to connect to a server through the client's controller.
     *
     * @param ipAddress of type {@code byte[]} - array of bytes equivalent to the IP address.
     * @return {@code boolean} - true if the connection was established correctly, false else.
     */
    public boolean connectToServer(byte[] ipAddress) {
        return controllerClient.connect(ipAddress);
    }

    /**
     * Method setQuit quits is used by the view to quit a game through the client's controller. br>
     * If the method is called during the init phase then the client's view (and therefore the client application) will stop.
     *
     * @param forceImmediateExecution of type {@code boolean} - boolean to check if the phase change after quitting the game should happen instantly.
     */
    public void setQuit(boolean forceImmediateExecution) {
        // true quits all, only if called during init phase
        quit = controllerClient.setQuit(forceImmediateExecution);
    }


    /**
     * Method canQuit checks if the client can quit the game.
     *
     * @return {@code boolean} - true if the client can quit the game, false else.
     */
    public boolean canQuit() {
        return quit;
    }

    /**
     * Method setCurrentCharacterCard is used by the view to update, through the client's controller, the character card currently
     * being played in the game, based on the index provided.
     *
     * @param currentCharacterCardIndex of type {@code int} - index of the character card being played.
     */
    public void setCurrentCharacterCard(int currentCharacterCardIndex) {
        controllerClient.setCurrentCharacterCard(currentCharacterCardIndex);
    }

    /**
     * Method unsetCurrentCharacterCard is used by the view to set, through the client's controller, the character
     * card currently being played in the game to null.
     */
    public void unsetCurrentCharacterCard() {
        controllerClient.unsetCurrentCharacterCard();
    }

    /**
     * Method getCurrentCharacterCard is used by the view to return the character card currently being played in the game.
     *
     * @return {@link CharacterCardClient} - null if the view's model is null, instance of the character card currently
     * being played else.
     */
    public CharacterCardClient getCurrentCharacterCard() {
        if (getModel() == null) return null;
        return getModel().getCurrentCharacterCard();
    }

    /**
     * Method addMessage is used by the view to add, through the client's controller, a message to the client's chat.
     *
     * @param message of type {@code String} - text message to add
     */
    public void addMessage(String message) {
        controllerClient.addMessage(message);
    }

    /**
     * Method getChat is used by the view to get the game's chat through the client's controller.
     *
     * @return {@code ArrayList}<{@code String}> - copy of the chat saved in a list.
     */
    public ArrayList<String> getChat() {
        return controllerClient.getChat();
    }

}
