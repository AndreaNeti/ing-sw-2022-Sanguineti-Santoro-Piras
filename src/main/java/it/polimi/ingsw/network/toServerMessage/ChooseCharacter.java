package it.polimi.ingsw.network.toServerMessage;

import it.polimi.ingsw.Server.controller.ClientHandler;
import it.polimi.ingsw.Server.controller.Controller;
import it.polimi.ingsw.exceptions.serverExceptions.GameException;
import it.polimi.ingsw.exceptions.serverExceptions.NotAllowedException;

public class ChooseCharacter implements ToServerMessage {
    private final byte charId;
    private final String charName;

    public ChooseCharacter(byte charId, String charName) {
        this.charId = charId;
        this.charName = charName;
    }

    @Override
    public void execute(ClientHandler clientHandler) throws GameException {
        Controller c = clientHandler.getController();
        if (c.isGameFinished()) {
            throw new NotAllowedException("Game is already finished");
        }
        if (c.isMyTurn(clientHandler)) {
            c.chooseCharacter(charId);
            c.sendMessage(clientHandler, "chose " + charName + " card");
        } else throw new NotAllowedException("It's not your turn");
    }
}
