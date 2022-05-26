package it.polimi.ingsw.network.toServerMessage;

import it.polimi.ingsw.Server.controller.ClientHandler;
import it.polimi.ingsw.exceptions.serverExceptions.EndGameException;
import it.polimi.ingsw.exceptions.serverExceptions.GameException;
import it.polimi.ingsw.exceptions.serverExceptions.NotAllowedException;

public class JoinMatchById implements ToServerMessage {
    Long matchId;

    public JoinMatchById(Long matchId) {
        this.matchId = matchId;
    }

    @Override
    public void execute(ClientHandler clientHandler) throws GameException, EndGameException {
        if (clientHandler.getNickName() == null) throw new NotAllowedException("Nickname not set");
        if (clientHandler.getController() != null) throw new NotAllowedException("Already joined a match");
        clientHandler.joinByMatchId(matchId);
    }
}
