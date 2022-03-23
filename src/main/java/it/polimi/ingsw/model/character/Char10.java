package it.polimi.ingsw.model.character;

import it.polimi.ingsw.model.GameComponent;

public class Char10 extends GameComponent implements Character {

    private byte nInput = 0;

    public Char10() {
        super();
        /*Bag.getBag().drawStudent(this, 4);*/
    }

    @Override
    public void play() {

    }

    @Override
    public byte getCost() {
        return 2;
    }

    @Override
    public void reset() {

    }

    @Override
    public void setInput(int input) {

    }

    @Override
    public int getId() {
        return 10;
    }

    @Override
    public boolean canPlay() {
        return false;
    }
}

