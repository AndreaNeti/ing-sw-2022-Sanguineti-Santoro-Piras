package it.polimi.ingsw.model.character;

import it.polimi.ingsw.model.ExpertGame;

public class Char5 implements CharacterCard {
    @Override
    public void play(ExpertGame game) {
        game.setTowerInfluence(false);
    }

    @Override
    public byte getCost() {
        return 3;
    }

    @Override
    public int getId() {
        return 5;
    }

    @Override
    public boolean canPlay(int nInput) {
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Char5)) return false;
        CharacterCard c = (CharacterCard) o;
        return getId() == c.getId();
    }
}
