package it.polimi.ingsw.network;

import it.polimi.ingsw.controller.MatchType;
import it.polimi.ingsw.controller.PlayerHandler;
import it.polimi.ingsw.exceptions.GameException;

public class CreateMatch implements ToServerMessage{
    MatchType matchType;

    public CreateMatch(MatchType matchType) {
        this.matchType = matchType;
    }

    @Override
    public void execute(PlayerHandler playerHandler) throws GameException {
        playerHandler.createMatch(matchType);
    }
}
