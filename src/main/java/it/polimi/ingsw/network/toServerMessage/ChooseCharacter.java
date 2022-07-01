package it.polimi.ingsw.network.toServerMessage;

import it.polimi.ingsw.exceptions.serverExceptions.GameException;
import it.polimi.ingsw.exceptions.serverExceptions.NotAllowedException;
import it.polimi.ingsw.server.controller.ClientHandler;
import it.polimi.ingsw.server.controller.Controller;

/**
 * ChooseCharacter class is used by the client to select a character card to play.
 */
public class ChooseCharacter implements ToServerMessage {
    private final Byte charId;
    private final String charName;

    /**
     * Constructor ChooseCharacter creates a new instance of ChooseCharacter.
     *
     * @param charId   of type {@code Byte} - unique ID of the character card the client wants to choose.
     * @param charName of type {@code String} - name of the character card.
     */
    public ChooseCharacter(Byte charId, String charName) {
        this.charId = charId;
        this.charName = charName;
    }

    /**
     * Method execute uses the game controller to choose the selected character card.
     *
     * @param clientHandler of type {@link ClientHandler} - instance of the client handler that sends the message.
     * @throws GameException if the game is finished or if it's not the client's turn or if the selected character
     *                       card cannot be chosen.
     */
    @Override
    public void execute(ClientHandler clientHandler) throws GameException {
        Controller c = clientHandler.getController();
        if (c.isGameFinished()) {
            throw new NotAllowedException("Game is already finished");
        }
        if (c.isMyTurn(clientHandler)) {
            c.chooseCharacter(charId);
            if (charId != null)
                c.sendMessage(clientHandler, "chose " + charName + " card");
            else
                c.sendMessage(clientHandler, " deselected character card");
        } else throw new NotAllowedException("It's not your turn");
    }
}
