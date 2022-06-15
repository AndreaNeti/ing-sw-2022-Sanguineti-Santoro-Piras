package it.polimi.ingsw.network.toServerMessage;

import it.polimi.ingsw.Server.controller.ClientHandler;
import it.polimi.ingsw.Server.controller.Controller;
import it.polimi.ingsw.exceptions.serverExceptions.EndGameException;
import it.polimi.ingsw.exceptions.serverExceptions.GameException;
import it.polimi.ingsw.exceptions.serverExceptions.NotAllowedException;

/**
 * MoveFromCloud class is used by the client to move students from a selected cloud to the entrance hall.
 */
public class MoveFromCloud implements ToServerMessage {
    private final int idGameComponent;

    /**
     * Constructor MoveFromCloud creates a new instance of MoveFromCloud.
     *
     * @param idGameComponent of type {@code int} - unique ID of the cloud.
     */
    public MoveFromCloud(int idGameComponent) {
        this.idGameComponent = idGameComponent;
    }

    /**
     * Method execute used the game controller to move students from the selected clouds.
     *
     * @param clientHandler of type {@link ClientHandler} - instance of the client handler that sends the message.
     * @throws GameException if the game is finished or if it's not the client's turn or if the selected cloud
     * has no students.
     * @throws EndGameException if it is the last turn and after moving students from the cloud the game ends.
     */
    @Override
    public void execute(ClientHandler clientHandler) throws GameException, EndGameException {
        Controller c = clientHandler.getController();
        if (c.isGameFinished()) {
            throw new NotAllowedException("Game is already finished");
        }
        if (c.isMyTurn(clientHandler)) {
            c.moveFromCloud(idGameComponent);
            c.sendMessage(clientHandler, "took students from Cloud " + -idGameComponent);
        } else throw new NotAllowedException("It's not your turn");
    }
}