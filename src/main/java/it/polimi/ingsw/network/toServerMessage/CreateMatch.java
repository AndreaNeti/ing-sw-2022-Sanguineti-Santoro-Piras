package it.polimi.ingsw.network.toServerMessage;

import it.polimi.ingsw.controller.MatchType;
import it.polimi.ingsw.controller.PlayerHandler;
import it.polimi.ingsw.exceptions.GameException;
import it.polimi.ingsw.exceptions.NotAllowedException;

public class CreateMatch implements ToServerMessage {
    MatchType matchType;

    public CreateMatch(MatchType matchType) {
        this.matchType = matchType;
    }

    @Override
    public void execute(PlayerHandler playerHandler) throws GameException {
        if (playerHandler.getNickName() == null) throw new NotAllowedException("Nickname not set");
        if (playerHandler.getController() != null) throw new NotAllowedException("Already joined a match");
        playerHandler.createMatch(matchType);
    }
}
