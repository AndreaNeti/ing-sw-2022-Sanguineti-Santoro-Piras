package it.polimi.ingsw.network.toServerMessage;

import it.polimi.ingsw.server.controller.ClientHandler;
import it.polimi.ingsw.server.controller.Controller;
import it.polimi.ingsw.exceptions.serverExceptions.EndGameException;
import it.polimi.ingsw.exceptions.serverExceptions.GameException;
import it.polimi.ingsw.exceptions.serverExceptions.NotAllowedException;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * PlayCharacter class is used by the client to play the chosen character card with a provided list of inputs.
 */
public class PlayCharacter implements ToServerMessage {
    private final List<Integer> inputs;

    /**
     * Constructor PlayCharacter creates a new instance of PlayCharacter.
     *
     * @param inputs of type {@code List}<{@code Integer}> - list of inputs to play the chosen character card.
     */
    public PlayCharacter(List<Integer> inputs) {
        this.inputs = Objects.requireNonNullElse(inputs, Collections.emptyList());
    }

    /**
     * Method execute uses the game controller to play the chosen character card with the inputs selected.
     *
     * @param clientHandler of type {@link ClientHandler} - instance of the client handler that sends the message.
     * @throws GameException if the game is finished or if it's not the client's turn or if there's a problem
     * while playing the character card with the selected inputs.
     * @throws EndGameException if the card's effect triggers an endgame event
     * (no more students in the bag, no more towers in a team's board or less than 3 islands left)
     */
    @Override
    public void execute(ClientHandler clientHandler) throws GameException, EndGameException {
        Controller c = clientHandler.getController();
        if (c.isGameFinished()) {
            throw new NotAllowedException("Game is already finished");
        }
        if (c.isMyTurn(clientHandler)) {
            // next update: unify
            c.setCharacterInputs(inputs);
            c.playCharacter();
            c.sendMessage(clientHandler, "played the chosen character card");
        } else throw new NotAllowedException("It's not your turn");

    }
}
