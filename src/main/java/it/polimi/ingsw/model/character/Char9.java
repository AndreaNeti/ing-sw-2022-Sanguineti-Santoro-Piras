package it.polimi.ingsw.model.character;

import it.polimi.ingsw.model.ExpertGame;

public class Char9 implements CharacterCard {

    private byte nInput = 0;
    private final ExpertGame game;

    public Char9(ExpertGame game) {
        this.game = game;
    }

    @Override
    public void play() {

    }

    @Override
    public byte getCost() {
        return 1;
    }

    @Override
    public void reset() {
        nInput = 0;
    }

    @Override
    public void setInput(int input) {

    }

    @Override
    public int getId() {
        return 9;
    }

    @Override
    public boolean canPlay() {
        return (nInput == 2 || nInput == 4);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Char0)) return false;
        CharacterCard c = (CharacterCard) o;
        return getId() == c.getId();
    }
}

