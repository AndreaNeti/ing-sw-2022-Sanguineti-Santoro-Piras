package it.polimi.ingsw.network.toClientMessage;

import it.polimi.ingsw.client.Controller.ControllerClient;
import it.polimi.ingsw.utils.GamePhase;
import it.polimi.ingsw.utils.HouseColor;

import java.util.List;

/**
 * EndGame class is used to inform the client about the end of the game and the respective winners.
 */
public class EndGame implements ToClientMessage {
    private final List<HouseColor> winners;

    /**
     * Constructor EndGame creates a new instance of EndGame.
     *
     * @param winners of type {@code List}<{@link HouseColor}> - list of winner of the game.
     */
    public EndGame(List<HouseColor> winners) {
        this.winners = winners;
    }

    /**
     * Method execute uses the client controller to quit the game, notify the client about the winners and update its phase.
     * If there is no winner it means a player disconnected, so the client adds a "player disconnected" message to its chat
     * and quits the lobby.
     *
     * @param controllerClient of type {@link ControllerClient} - instance of the client controller that receives the message.
     */
    @Override
    public void execute(ControllerClient controllerClient) {
        // someone is disconnected
        if (winners == null || winners.isEmpty()) {
            // return to select match phase, do not lose connection to server nor nickName set
            controllerClient.setQuit(true);
            controllerClient.addMessage("Someone left the match");
        } else {
            controllerClient.notifyWinners(winners);
            controllerClient.changePhase(GamePhase.WAIT_PHASE, true);
        }
    }
}
