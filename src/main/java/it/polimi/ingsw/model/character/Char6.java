package it.polimi.ingsw.model.character;

import it.polimi.ingsw.model.ExpertGame;
import it.polimi.ingsw.model.GameComponent;

public class Char6 extends GameComponent implements CharacterCard {

    @Override
    public void play(ExpertGame game) {

    }

    @Override
    public byte getCost() {
        return 1;
    }

    @Override
    public int getId() {
        return 6;
    }

    @Override
    public boolean canPlay(int nInput) {
        return (nInput == 6 || nInput == 4 || nInput == 2);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Char6)) return false;
        CharacterCard c = (CharacterCard) o;
        return getId() == c.getId();
    }
}
