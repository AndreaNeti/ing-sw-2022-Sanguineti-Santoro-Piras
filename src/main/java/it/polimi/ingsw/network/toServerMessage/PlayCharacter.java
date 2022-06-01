package it.polimi.ingsw.network.toServerMessage;

import it.polimi.ingsw.Server.controller.ClientHandler;
import it.polimi.ingsw.Server.controller.Controller;
import it.polimi.ingsw.exceptions.serverExceptions.EndGameException;
import it.polimi.ingsw.exceptions.serverExceptions.GameException;
import it.polimi.ingsw.exceptions.serverExceptions.NotAllowedException;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class PlayCharacter implements ToServerMessage {
    private final List<Integer> inputs;

    public PlayCharacter(List<Integer> inputs) {
        this.inputs = Objects.requireNonNullElse(inputs, Collections.emptyList());
    }

    @Override
    public void execute(ClientHandler clientHandler) throws GameException, EndGameException {
        Controller c = clientHandler.getController();
        if (c.isGameFinished()) {
            throw new NotAllowedException("Game is already finished");
        }
        if (c.isMyTurn(clientHandler)) {
            // TODO unify?
            c.setCharacterInputs(inputs);
            c.playCharacter();
            c.sendMessage(clientHandler, "played the chosen character card");
        } else throw new NotAllowedException("It's not your turn");

    }
}
