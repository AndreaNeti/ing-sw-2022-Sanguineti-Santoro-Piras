package it.polimi.ingsw.Server.model;

import it.polimi.ingsw.Enum.Color;
import it.polimi.ingsw.exceptions.serverExceptions.NotAllowedException;

public class Char8 implements CharacterCard {

    @Override
    public void play(CharacterCardGame game) throws NotAllowedException {
        int color = game.getCharacterInputs().get(0);
        if (color < 0 || color >= Color.values().length)
            throw new NotAllowedException("Set wrong input for color");
        game.setIgnoredColorInfluence(Color.values()[color]);
    }

    @Override
    public byte getCost() {
        return 3;
    }

    @Override
    public byte getCharId() {
        return 8;
    }

    @Override
    public boolean canPlay(int nInput) {
        return nInput == 1;
    }

}
