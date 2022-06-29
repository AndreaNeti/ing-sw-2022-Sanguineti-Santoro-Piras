package it.polimi.ingsw.network;

import it.polimi.ingsw.Client.Controller.ControllerClient;
import it.polimi.ingsw.Server.controller.ClientHandler;
import it.polimi.ingsw.exceptions.serverExceptions.EndGameException;
import it.polimi.ingsw.exceptions.serverExceptions.GameException;
import it.polimi.ingsw.network.toClientMessage.ToClientMessage;
import it.polimi.ingsw.network.toServerMessage.ToServerMessage;

public class PingMessage implements ToClientMessage, ToServerMessage {
    private void resetPing(PingPongInterface pingPongInterface) {
        pingPongInterface.resetPing();
    }

    @Override
    public void execute(ControllerClient controllerClient) {
        resetPing(controllerClient);
    }

    @Override
    public void execute(ClientHandler clientHandler) throws GameException, EndGameException {
        resetPing(clientHandler);
    }
}
