package it.polimi.ingsw.model;

public class Char3 implements CharacterCard {

    @Override
    public void play(ExpertGame game) {
        game.setExtraSteps();
    }

    @Override
    public byte getCost() {
        return 1;
    }

    @Override
    public int getId() {
        return 3;
    }

    @Override
    public boolean canPlay(int nInput) {
        return true;
    }

//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (!(o instanceof Char3)) return false;
//        CharacterCard c = (CharacterCard) o;
//        return getId() == c.getId();
//    }
}