package it.polimi.ingsw.network;

import it.polimi.ingsw.controller.MatchType;
import it.polimi.ingsw.controller.Message;
import it.polimi.ingsw.controller.PlayerHandler;
import it.polimi.ingsw.controller.Server;
import it.polimi.ingsw.exceptions.GameException;

public class GetOldestMatchId implements ToServerMessage {
    MatchType matchType;

    public GetOldestMatchId(MatchType matchType) {
        this.matchType = matchType;
    }

    @Override
    public void execute(PlayerHandler playerHandler) throws GameException {
        playerHandler.joinOldestMatchId(matchType);
    }
}