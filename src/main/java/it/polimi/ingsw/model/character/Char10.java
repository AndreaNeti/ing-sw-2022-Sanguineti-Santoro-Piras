package it.polimi.ingsw.model.character;

import it.polimi.ingsw.model.ExpertGame;
import it.polimi.ingsw.model.GameComponent;

public class Char10 extends GameComponent implements CharacterCard {

    @Override
    public void play(ExpertGame game) {

    }

    @Override
    public byte getCost() {
        return 2;
    }


    @Override
    public int getId() {
        return 10;
    }

    @Override
    public boolean canPlay(int nInput) {
        return nInput == 1;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Char0)) return false;
        CharacterCard c = (CharacterCard) o;
        return getId() == c.getId();
    }
}

