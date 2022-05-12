package it.polimi.ingsw.Server.model;

public class Char5 implements CharacterCard {
    @Override
    public void play(CharacterCardGame game) {
        game.removeTowerInfluence();
    }

    @Override
    public byte getCost() {
        return 3;
    }

    @Override
    public byte getCharId() {
        return 5;
    }

    @Override
    public boolean canPlay(int nInput) {
        return nInput == 0;
    }

//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (!(o instanceof Char5)) return false;
//        CharacterCard c = (CharacterCard) o;
//        return getCharId() == c.getCharId();
//    }
}
