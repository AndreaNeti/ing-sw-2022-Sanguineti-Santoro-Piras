package it.polimi.ingsw.Server.model;

import it.polimi.ingsw.Server.model.GameComponents.Island;
import it.polimi.ingsw.exceptions.serverExceptions.EndGameException;
import it.polimi.ingsw.exceptions.serverExceptions.GameException;
import it.polimi.ingsw.exceptions.serverExceptions.NotAllowedException;

public class Char2 implements CharacterCard {

    @Override
    public void play(CharacterCardGame game) throws GameException, EndGameException {
        int idIsland = game.getCharacterInputs().get(0);
        if (idIsland > 2 * game.getPlayerSize() + 12 || idIsland < 2 * game.getPlayerSize())
            throw new NotAllowedException("Set wrong inputs for idIsland");
        game.calculateInfluence((Island) game.getComponentById(idIsland));
    }

    @Override
    public byte getCost() {
        return 3;
    }

    @Override
    public byte getCharId() {
        return 2;
    }

    @Override
    public boolean canPlay(int nInput) {
        return nInput == 1;
    }

}