package it.polimi.ingsw.model;

public class Char1 implements CharacterCard {

    @Override
    public void play(ExpertGame game) {
        game.setEqualProfessorCalculation(true);
    }

    @Override
    public byte getCost() {
        return 2;
    }

    @Override
    public int getId() {
        return 1;
    }

    @Override
    public boolean canPlay(int nInput) {
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Char1)) return false;
        CharacterCard c = (CharacterCard) o;
        return getId() == c.getId();
    }
}