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
    public byte getCharId() {
        return 7;
    }

    @Override
    public boolean canPlay(int nInput) {
        return nInput == 0;
    }

//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (!(o instanceof Char7)) return false;
//        CharacterCard c = (CharacterCard) o;
//        return getCharId() == c.getCharId();
//    }
}
