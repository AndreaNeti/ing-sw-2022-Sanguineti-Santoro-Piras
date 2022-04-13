package it.polimi.ingsw.model;

public class Char7 implements CharacterCard {

    @Override
    public void play(ExpertGame game) {
        game.setExtraInfluence();
    }

    @Override
    public byte getCost() {
        return 2;
    }

    @Override
    public int getId() {
        return 7;
    }

    @Override
    public boolean canPlay(int nInput) {
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Char7)) return false;
        CharacterCard c = (CharacterCard) o;
        return getId() == c.getId();
    }
}
