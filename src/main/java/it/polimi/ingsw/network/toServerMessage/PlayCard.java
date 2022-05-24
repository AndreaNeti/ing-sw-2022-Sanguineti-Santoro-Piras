package it.polimi.ingsw.network.toServerMessage;

import it.polimi.ingsw.Server.controller.ClientHandler;
import it.polimi.ingsw.Server.controller.Controller;
import it.polimi.ingsw.exceptions.GameException;
import it.polimi.ingsw.exceptions.NotAllowedException;

public class PlayCard implements ToServerMessage {
    byte playedCard;

    public PlayCard(byte playedCard) {
        this.playedCard = playedCard;
    }

    @Override
    public void execute(ClientHandler clientHandler) throws GameException {
        Controller c = clientHandler.getController();
        if (c.isGameFinished()) {
            throw new NotAllowedException("Game is already finished");
        }
        if (c.isMyTurn(clientHandler))
            c.playCard(playedCard);
        else throw new NotAllowedException("It's not your turn");
    }
}
