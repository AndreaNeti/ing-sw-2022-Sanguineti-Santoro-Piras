package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.EndGameException;
import it.polimi.ingsw.exceptions.UnexpectedValueException;

public class Char2 implements CharacterCard {

    @Override
    public void play(ExpertGame game) throws UnexpectedValueException, EndGameException {
        int idIsland = game.getCharacterInputs().get(0);
        if (idIsland < 0 || idIsland >= game.getIslands().size())
            throw new UnexpectedValueException();
        game.calculateInfluence(game.getIslands().get(idIsland));
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

//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (!(o instanceof Char2)) return false;
//        CharacterCard c = (CharacterCard) o;
//        return getCharId() == c.getCharId();
//    }
}