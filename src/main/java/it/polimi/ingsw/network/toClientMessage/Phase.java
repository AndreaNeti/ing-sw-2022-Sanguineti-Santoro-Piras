package it.polimi.ingsw.network.toClientMessage;

import it.polimi.ingsw.Client.Controller.ControllerClient;
import it.polimi.ingsw.Enum.GamePhase;

public class Phase implements ToClientMessage {
    final GamePhase gamePhase;
    byte currentPlayer;

    public Phase(GamePhase gamePhase, byte currentPlayer) {
        this.gamePhase = gamePhase;
        this.currentPlayer = currentPlayer;
    }

    @Override
    public void execute(ControllerClient controllerClient) {
        controllerClient.changePhaseAndCurrentPlayer(gamePhase, currentPlayer);
    }

    @Override
    public String toString() {
        return "current player:" + currentPlayer + gamePhase.toString();
    }
}
