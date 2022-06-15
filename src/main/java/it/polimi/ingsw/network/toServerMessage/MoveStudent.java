package it.polimi.ingsw.network.toServerMessage;

import it.polimi.ingsw.Util.Color;
import it.polimi.ingsw.Server.controller.ClientHandler;
import it.polimi.ingsw.Server.controller.Controller;
import it.polimi.ingsw.exceptions.serverExceptions.EndGameException;
import it.polimi.ingsw.exceptions.serverExceptions.GameException;
import it.polimi.ingsw.exceptions.serverExceptions.NotAllowedException;

/**
 * MoveStudent class is used by the client to move a student to a selected game component.
 */
public class MoveStudent implements ToServerMessage {
    private final Color color;
    private final int idGameComponent;
    private final String gameComponentName;

    /**
     * Constructor MoveStudent creates a new instance of MoveStudent.
     *
     * @param color of type {@link Color} - color of the student to move.
     * @param idGameComponent of type {@code int} - unique ID of the target game component.
     * @param gameComponentName of type {@code String} - name of the game component.
     */
    public MoveStudent(Color color, int idGameComponent, String gameComponentName) {
        this.color = color;
        this.idGameComponent = idGameComponent;
        this.gameComponentName = gameComponentName;
    }

    /**
     * Method execute uses the game controller to move the selected student to the target game component.
     *
     * @param clientHandler of type {@link ClientHandler} - instance of the client handler that sends the message.
     * @throws GameException if the game is finished or if it's not the client's turn or if the selected
     * student cannot be moved to the game component.
     * @throws EndGameException if
     */
    @Override
    public void execute(ClientHandler clientHandler) throws GameException, EndGameException {
        Controller c = clientHandler.getController();
        if (c.isGameFinished()) {
            throw new NotAllowedException("Game is already finished");
        }
        if (c.isMyTurn(clientHandler)) {
            c.move(color, idGameComponent);
            c.sendMessage(clientHandler, "moved a " + color + " student to " + gameComponentName);
        } else throw new NotAllowedException("It's not your turn");
    }
}
