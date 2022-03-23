package it.polimi.ingsw.model.character;

import it.polimi.ingsw.model.GameComponent;

public class Char0 extends GameComponent implements CharacterCard {

    private byte nInput = 0;

    public Char0() {
        super();
        /*Bag.getBag().drawStudent(this, 4);*/
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
        return 0;
    }

    @Override
    public boolean canPlay() {
        return nInput == 1;
    }
}
