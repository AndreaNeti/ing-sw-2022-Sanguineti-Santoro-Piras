package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.NotAllowedException;
import it.polimi.ingsw.exceptions.UnexpectedValueException;

public class Char4 implements CharacterCard {


    @Override
    public void play(ExpertGame game) throws UnexpectedValueException, NotAllowedException {
        int idIsland = game.getCharacterInputs().get(0);
        if (idIsland < 0 || idIsland >= game.getIslands().size())
            throw new UnexpectedValueException();
        game.setProhibition(idIsland);
    }

    @Override
    public byte getCost() {
        return 2;
    }

    @Override
    public int getId() {
        return 4;
    }

    @Override
    public boolean canPlay(int nInput) {
        return nInput == 1;
    }

//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (!(o instanceof Char4)) return false;
//        CharacterCard c = (CharacterCard) o;
//        return getId() == c.getId();
//    }
}