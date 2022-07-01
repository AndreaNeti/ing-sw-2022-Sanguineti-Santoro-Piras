package it.polimi.ingsw.network.toServerMessage;

import it.polimi.ingsw.server.controller.ClientHandler;
import it.polimi.ingsw.server.controller.Controller;
import it.polimi.ingsw.exceptions.serverExceptions.EndGameException;
import it.polimi.ingsw.exceptions.serverExceptions.GameException;
import it.polimi.ingsw.exceptions.serverExceptions.NotAllowedException;

/**
 * MoveMotherNature class is used by the client to move mother nature a selected amount of moves.
 */
public class MoveMotherNature implements ToServerMessage {
    private final int moves;

    /**
     * Constructor MoveMotherNature creates a new instance of MoveMotherNature.
     *
     * @param moves of type {@code int} - number of moves.
     */
    public MoveMotherNature(int moves) {
        this.moves = moves;
    }

    /**
     * Method execute uses the game controller to move mother nature the selected amount of moves.
     *
     * @param clientHandler of type {@link ClientHandler} - instance of the client handler that sends the message.
     * @throws GameException if the game is finished or if it's not the client's turn or if mother nature cannot
     * be moved the selected amount of moves.
     * @throws EndGameException if after moving mother nature there are less than 3 islands left in the game.
     */
    @Override
    public void execute(ClientHandler clientHandler) throws GameException, EndGameException {
        Controller c = clientHandler.getController();
        if (c.isGameFinished()) {
            throw new NotAllowedException("Game is already finished");
        }
        if (c.isMyTurn(clientHandler)) {
            c.moveMotherNature(moves);
            c.sendMessage(clientHandler, "moved Mother Nature by " + moves + " moves");
        } else throw new NotAllowedException("It's not your turn");
    }

}
