package it.polimi.ingsw.network.toServerMessage;

import it.polimi.ingsw.Server.controller.Controller;
import it.polimi.ingsw.Server.controller.ClientHandler;
import it.polimi.ingsw.exceptions.GameException;
import it.polimi.ingsw.exceptions.NotAllowedException;

public class MoveFromCloud implements ToServerMessage {
    int idGameComponent;

    public MoveFromCloud(int idGameComponent) {
        this.idGameComponent = idGameComponent;
    }

    @Override
    public void execute(ClientHandler clientHandler) throws GameException {
        Controller c = clientHandler.getController();
        if(c.isGameFinished()){
            throw new NotAllowedException("Game is already finished");
        }
        if (c.isMyTurn(clientHandler))
            c.moveFromCloud(idGameComponent);
        else throw new NotAllowedException("It's not your turn");
    }
}