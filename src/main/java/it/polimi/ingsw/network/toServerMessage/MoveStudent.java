package it.polimi.ingsw.network.toServerMessage;

import it.polimi.ingsw.Util.Color;
import it.polimi.ingsw.Server.controller.ClientHandler;
import it.polimi.ingsw.Server.controller.Controller;
import it.polimi.ingsw.exceptions.serverExceptions.EndGameException;
import it.polimi.ingsw.exceptions.serverExceptions.GameException;
import it.polimi.ingsw.exceptions.serverExceptions.NotAllowedException;

public class MoveStudent implements ToServerMessage {
    private final Color color;
    private final int idGameComponent;
    private final String gameComponentName;

    public MoveStudent(Color color, int idGameComponent, String gameComponentName) {
        this.color = color;
        this.idGameComponent = idGameComponent;
        this.gameComponentName = gameComponentName;
    }

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
