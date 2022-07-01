package it.polimi.ingsw.network.toServerMessage;

import it.polimi.ingsw.exceptions.serverExceptions.GameException;
import it.polimi.ingsw.exceptions.serverExceptions.NotAllowedException;
import it.polimi.ingsw.server.controller.ClientHandler;

/**
 * JoinMatchById class is used by the client to join a match based on its unique ID.
 */
public class JoinMatchById implements ToServerMessage {
    private final Long matchId;

    /**
     * Constructor JoinMatchById creates a new instance of JoinMatchById.
     *
     * @param matchId of type {@code Long} - unique ID of the game the client wants to join.
     */
    public JoinMatchById(Long matchId) {
        this.matchId = matchId;
    }

    /**
     * Method execute calls the {@link ClientHandler#joinByMatchId(Long)} method to join the match
     * with the selected ID.
     *
     * @param clientHandler of type {@link ClientHandler} - instance of the client handler that sends the message.
     * @throws GameException if the client's player doesn't have a nickname yet or if the client is already in a game
     * or if the client cannot be added to the game.
     */
    @Override
    public void execute(ClientHandler clientHandler) throws GameException {
        if (clientHandler.getNickName() == null) throw new NotAllowedException("Nickname not set");
        if (clientHandler.getController() != null) throw new NotAllowedException("Already joined a match");
        clientHandler.joinByMatchId(matchId);
    }
}
