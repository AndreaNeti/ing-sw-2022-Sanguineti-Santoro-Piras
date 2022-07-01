package it.polimi.ingsw.network.toServerMessage;

import it.polimi.ingsw.server.controller.ClientHandler;
import it.polimi.ingsw.server.controller.Controller;
import it.polimi.ingsw.util.AssistantCard;
import it.polimi.ingsw.exceptions.serverExceptions.EndGameException;
import it.polimi.ingsw.exceptions.serverExceptions.GameException;
import it.polimi.ingsw.exceptions.serverExceptions.NotAllowedException;

/**
 * PlayCard class is used by the client to play a selected assistant card.
 */
public class PlayCard implements ToServerMessage {
    private final AssistantCard playedCard;

    /**
     * Constructor PlayCard creates a new instance of PlayCard.
     *
     * @param playedCard of type {@link AssistantCard} - instance of the assistant card to play.
     */
    public PlayCard(AssistantCard playedCard) {
        this.playedCard = playedCard;
    }

    /**
     * Method execute uses the game controller to play the selected assistant card.
     *
     * @param clientHandler of type {@link ClientHandler} - instance of the client handler that sends the message.
     * @throws GameException if the game is finished or if it's not the client's turn or if the selected card
     * isn't available to play.
     * @throws EndGameException if
     */
    @Override
    public void execute(ClientHandler clientHandler) throws GameException, EndGameException {
        Controller c = clientHandler.getController();
        if (c.isGameFinished()) {
            throw new NotAllowedException("Game is already finished");
        }
        if (c.isMyTurn(clientHandler)) {
            c.playCard(playedCard);
            c.sendMessage(clientHandler, "played card n° " + playedCard.value());
        } else throw new NotAllowedException("It's not your turn");
    }
}
