package it.polimi.ingsw.model.character;

import it.polimi.ingsw.model.ExpertGame;

public class Char8 implements CharacterCard {

    @Override
    public void play(ExpertGame game) {

    }

    @Override
    public byte getCost() {
        return 3;
    }

    @Override
    public int getId() {
        return 8;
    }

    @Override
    public boolean canPlay(int nInput) {
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Char0)) return false;
        CharacterCard c = (CharacterCard) o;
        return getId() == c.getId();
    }
}
