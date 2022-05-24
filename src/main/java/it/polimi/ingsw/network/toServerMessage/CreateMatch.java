package it.polimi.ingsw.network.toServerMessage;

import it.polimi.ingsw.Server.controller.ClientHandler;
import it.polimi.ingsw.Server.controller.MatchType;
import it.polimi.ingsw.exceptions.GameException;
import it.polimi.ingsw.exceptions.NotAllowedException;

public class CreateMatch implements ToServerMessage {
    MatchType matchType;

    public CreateMatch(MatchType matchType) {
        this.matchType = matchType;
    }

    @Override
    public void execute(ClientHandler clientHandler) throws GameException {
        if (clientHandler.getNickName() == null) throw new NotAllowedException("Nickname not set");
        if (clientHandler.getController() != null) throw new NotAllowedException("Already joined a match");
        clientHandler.createMatch(matchType);
    }
}
