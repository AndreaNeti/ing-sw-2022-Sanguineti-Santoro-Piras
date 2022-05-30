package it.polimi.ingsw.Server.model;

import it.polimi.ingsw.Server.model.GameComponents.Island;
import it.polimi.ingsw.exceptions.serverExceptions.GameException;
import it.polimi.ingsw.exceptions.serverExceptions.NotAllowedException;

public class Char4 implements CharacterCard {


    @Override
    public void play(CharacterCardGame game) throws GameException {
        int idIsland = game.getCharacterInputs().get(0);
        if (idIsland > 2 * game.getPlayerSize() + 12 || idIsland < 2 * game.getPlayerSize())
            throw new NotAllowedException("Set wrong input for idIsland");
        Island chosenIsland = (Island) game.getComponentById(idIsland);
        game.setProhibition(chosenIsland);
        game.getGameDelta().addUpdatedGC(chosenIsland);
    }

    @Override
    public byte getCost() {
        return 2;
    }

    @Override
    public byte getCharId() {
        return 4;
    }

    @Override
    public boolean canPlay(int nInput) {
        return nInput == 1;
    }

}