package it.polimi.ingsw.network.toServerMessage;

import it.polimi.ingsw.Server.controller.ClientHandler;
import it.polimi.ingsw.Server.controller.MatchType;
import it.polimi.ingsw.exceptions.serverExceptions.EndGameException;
import it.polimi.ingsw.exceptions.serverExceptions.GameException;
import it.polimi.ingsw.exceptions.serverExceptions.NotAllowedException;

public class CreateMatch implements ToServerMessage {
    MatchType matchType;

    public CreateMatch(MatchType matchType) {
        this.matchType = matchType;
    }

    @Override
    public void execute(ClientHandler clientHandler) throws GameException, EndGameException {
        if (clientHandler.getNickName() == null) throw new NotAllowedException("Nickname not set");
        if (clientHandler.getController() != null) throw new NotAllowedException("Already joined a match");
        clientHandler.createMatch(matchType);
    }
}
