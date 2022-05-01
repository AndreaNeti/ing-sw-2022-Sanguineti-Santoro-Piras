package it.polimi.ingsw.network;

import it.polimi.ingsw.controller.PlayerHandler;
import it.polimi.ingsw.exceptions.GameException;

public class PlayCard implements ToServerMessage{
    byte playedCard;
    public PlayCard(byte playedCard) {
        this.playedCard = playedCard;
    }
    @Override
    public void execute(PlayerHandler playerHandler) throws GameException {
        playerHandler.getController().playCard(playedCard);
    }
}
