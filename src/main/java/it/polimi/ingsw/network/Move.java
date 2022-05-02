package it.polimi.ingsw.network;

import it.polimi.ingsw.controller.PlayerHandler;
import it.polimi.ingsw.exceptions.GameException;
import it.polimi.ingsw.model.Color;

public class Move implements ToServerMessage {
    Color color;
    int idGameComponent;

    public Move(Color color, int idGameComponent) {
        this.color = color;
        this.idGameComponent = idGameComponent;
    }

    @Override
    public void execute(PlayerHandler playerHandler) throws GameException {
        playerHandler.getController().move(color, idGameComponent);
    }
}
