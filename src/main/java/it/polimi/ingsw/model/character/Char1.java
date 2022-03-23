package it.polimi.ingsw.model.character;

import it.polimi.ingsw.model.ExpertGame;

public class Char1 implements CharacterCard {

    private final ExpertGame game;

    public Char1(ExpertGame game) {
        this.game = game;
    }

    @Override
    public void play() {

    }

    @Override
    public byte getCost() {
        return 2;
    }

    @Override
    public void reset() {}

    @Override
    public void setInput(int input) {

    }

    @Override
    public int getId() {
        return 1;
    }

    @Override
    public boolean canPlay() {
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