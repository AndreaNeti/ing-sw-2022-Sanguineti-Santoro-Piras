package it.polimi.ingsw.Server.model;

import it.polimi.ingsw.Enum.Color;
import it.polimi.ingsw.exceptions.UnexpectedValueException;

public class Char8 implements CharacterCard {

    @Override
    public void play(ExpertGame game) throws UnexpectedValueException{
        int color = game.getCharacterInputs().get(0);
        if (color < 0 || color >= Color.values().length)
            throw new UnexpectedValueException();
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

//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (!(o instanceof Char8)) return false;
//        CharacterCard c = (CharacterCard) o;
//        return getCharId() == c.getCharId();
//    }
}
