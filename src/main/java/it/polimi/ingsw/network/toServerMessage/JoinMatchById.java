package it.polimi.ingsw.network.toServerMessage;

import it.polimi.ingsw.Server.controller.PlayerHandler;
import it.polimi.ingsw.exceptions.GameException;
import it.polimi.ingsw.exceptions.NotAllowedException;

public class JoinMatchById implements ToServerMessage {
    Long matchId;

    public JoinMatchById(Long matchId) {
        this.matchId = matchId;
    }

    @Override
    public void execute(PlayerHandler playerHandler) throws GameException {
        if (playerHandler.getNickName() == null) throw new NotAllowedException("Nickname not set");
        if (playerHandler.getController() != null) throw new NotAllowedException("Already joined a match");
        playerHandler.joinMatchId(matchId);
    }
}
