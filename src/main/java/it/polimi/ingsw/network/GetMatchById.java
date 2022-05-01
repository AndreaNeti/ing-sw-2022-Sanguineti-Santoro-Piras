package it.polimi.ingsw.network;

import it.polimi.ingsw.controller.PlayerHandler;
import it.polimi.ingsw.exceptions.GameException;

public class GetMatchById implements ToServerMessage {
    Long matchId;

    public GetMatchById(Long matchId) {
        this.matchId = matchId;
    }
    @Override
    public void execute(PlayerHandler playerHandler) throws GameException {
        playerHandler.joinMatchId(matchId);
    }
}
