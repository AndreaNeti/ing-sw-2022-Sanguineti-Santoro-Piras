package it.polimi.ingsw.Server.model;

import it.polimi.ingsw.Server.model.GameComponents.Island;
import it.polimi.ingsw.exceptions.serverExceptions.GameException;
import it.polimi.ingsw.exceptions.serverExceptions.NotAllowedException;

/**
 * Char4 class represents the <b>"Grandma weeds"</b> character card. <br>
 * <b>Effect</b>: Place a No Entry tile on an Island of your choice. The first time Mother
 * Nature ends her movement there, put the No Entry tile back onto this card
 * DO NOT calculate influence on that Island, or place any Towers. <br>
 * <b>Inputs required</b>: Island ID.
 */
public class Char4 implements CharacterCard {

    /**
     * Method play adds a prohibition to the selected island
     *
     * @param game of type {@link CharacterCardGame} - the game instance that the card modifies with its effect.
     * @throws GameException if the island ID is not valid.
     */
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