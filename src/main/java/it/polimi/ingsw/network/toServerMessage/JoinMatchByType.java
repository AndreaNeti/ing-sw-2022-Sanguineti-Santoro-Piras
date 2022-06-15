package it.polimi.ingsw.network.toServerMessage;

import it.polimi.ingsw.Server.controller.ClientHandler;
import it.polimi.ingsw.Server.controller.MatchType;
import it.polimi.ingsw.exceptions.serverExceptions.GameException;
import it.polimi.ingsw.exceptions.serverExceptions.NotAllowedException;

/**
 * JoinMatchByType class is used by the client to join a match based on its type.
 */
public class JoinMatchByType implements ToServerMessage {
    MatchType matchType;

    /**
     * Constructor JoinMatchByType creates a new instance of JoinMatchByType.
     *
     * @param matchType of type {@link MatchType} - type of game the client wants to join.
     */
    public JoinMatchByType(MatchType matchType) {
        this.matchType = matchType;
    }

    /**
     * Method execute calls the {@link ClientHandler#joinByMatchType(MatchType)} method to join a match
     * with the selected type.
     *
     * @param clientHandler of type {@link ClientHandler} - instance of the client handler that sends the message.
     * @throws GameException if the client's player doesn't have a nickname yet or if the client is already in a game
     * or if the client cannot be added to the game.
     */
    @Override
    public void execute(ClientHandler clientHandler) throws GameException {
        if (clientHandler.getNickName() == null) throw new NotAllowedException("Nickname not set");
        if (clientHandler.getController() != null) throw new NotAllowedException("Already joined a match");
        clientHandler.joinByMatchType(matchType);
    }
}