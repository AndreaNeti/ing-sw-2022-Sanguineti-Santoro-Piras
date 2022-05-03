package it.polimi.ingsw.network;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.controller.PlayerHandler;
import it.polimi.ingsw.exceptions.GameException;
import it.polimi.ingsw.exceptions.NotAllowedException;

public class PlayCard implements ToServerMessage {
    byte playedCard;

    public PlayCard(byte playedCard) {
        this.playedCard = playedCard;
    }

    @Override
    public void execute(PlayerHandler playerHandler) throws GameException {
        Controller c = playerHandler.getController();
        if (c.isMyTurn(playerHandler))
            c.playCard(playedCard);
        else throw new NotAllowedException("It's not your turn");
    }
}
