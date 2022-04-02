package it.polimi.ingsw.model.character;

import it.polimi.ingsw.exceptions.UnexpectedValueException;
import it.polimi.ingsw.model.ExpertGame;
import it.polimi.ingsw.model.Island;

public class Char4 implements CharacterCard {


    @Override
    public void play(ExpertGame game) throws UnexpectedValueException {
        int idIsland = game.getCharacterInputs().get(0);
        if (idIsland < 0 || idIsland >= game.getIslands().size())
            throw new UnexpectedValueException();
        ((Island)game.getIslands().get(idIsland)).setProhibition(true);
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Char4)) return false;
        CharacterCard c = (CharacterCard) o;
        return getId() == c.getId();
    }
}