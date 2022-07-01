package it.polimi.ingsw.network.toClientMessage;

import it.polimi.ingsw.client.controller.ControllerClient;
import it.polimi.ingsw.utils.GamePhase;

/**
 * Phase class is used to send info about the current player and the game phase he is currently playing to the client.
 * Phase is send also every time there is an error
 */
public class Phase implements ToClientMessage {
    private final GamePhase gamePhase;
    private final byte currentPlayer;

    /**
     * Constructor Phase creates a new instance of Phase.
     *
     * @param gamePhase of type {@link GamePhase} - current game phase.
     * @param currentPlayer of type {@code byte} - index of the current player.
     */
    public Phase(GamePhase gamePhase, byte currentPlayer) {
        this.gamePhase = gamePhase;
        this.currentPlayer = currentPlayer;
    }

    /**
     * Method execute uses the client controller to change the current phase in the client.
     *
     * @param controllerClient of type {@link ControllerClient} - instance of the client controller that receives the message.
     */
    @Override
    public void execute(ControllerClient controllerClient) {
        controllerClient.changePhase(gamePhase, currentPlayer, false);
    }

    /**
     * Method toString returns the current player and the game phase he is playing.
     *
     * @return {@code String} - "current player: (player index), (game phase)".
     */
    @Override
    public String toString() {
        return "current player:" + currentPlayer + ", " +gamePhase.toString();
    }
}
