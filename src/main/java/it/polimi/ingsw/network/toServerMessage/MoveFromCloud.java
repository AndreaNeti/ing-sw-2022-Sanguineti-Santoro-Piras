package it.polimi.ingsw.network.toServerMessage;

import it.polimi.ingsw.Server.controller.ClientHandler;
import it.polimi.ingsw.Server.controller.Controller;
import it.polimi.ingsw.exceptions.serverExceptions.EndGameException;
import it.polimi.ingsw.exceptions.serverExceptions.GameException;
import it.polimi.ingsw.exceptions.serverExceptions.NotAllowedException;

public class MoveFromCloud implements ToServerMessage {
    private final int idGameComponent;

    public MoveFromCloud(int idGameComponent) {
        this.idGameComponent = idGameComponent;
    }

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