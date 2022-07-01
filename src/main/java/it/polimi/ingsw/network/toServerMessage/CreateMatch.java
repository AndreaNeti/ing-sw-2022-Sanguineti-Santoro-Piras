package it.polimi.ingsw.network.toServerMessage;

import it.polimi.ingsw.server.controller.ClientHandler;
import it.polimi.ingsw.util.MatchType;
import it.polimi.ingsw.exceptions.serverExceptions.GameException;
import it.polimi.ingsw.exceptions.serverExceptions.NotAllowedException;

/**
 * CreateMatch class is used by the client to create and join a match of the selected type.
 */
public class CreateMatch implements ToServerMessage {
    private final MatchType matchType;

    /**
     * Constructor CreateMatch creates a new instance of CreateMatch.
     *
     * @param matchType of type {@link MatchType} - type of game the client wants to create.
     */
    public CreateMatch(MatchType matchType) {
        this.matchType = matchType;
    }

    /**
     * Method execute calls the {@link ClientHandler#createMatch(MatchType)} method to create a match
     * of the selected type.
     *
     * @param clientHandler of type {@link ClientHandler} - instance of the client handler that sends the message.
     * @throws GameException if the client's player doesn't have a nickname yet or if the client is already in a game
     * or if the client cannot be added to the game created.
     */
    @Override
    public void execute(ClientHandler clientHandler) throws GameException {
        if (clientHandler.getNickName() == null) throw new NotAllowedException("Nickname not set");
        if (clientHandler.getController() != null) throw new NotAllowedException("Already joined a match");
        clientHandler.createMatch(matchType);
    }
}
