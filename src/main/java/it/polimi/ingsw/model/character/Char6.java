package it.polimi.ingsw.model.character;

import it.polimi.ingsw.model.GameComponent;

public class Char6 extends GameComponent implements CharacterCard {

    private byte nInput = 0;

    public Char6() {
        super();
        //Bag.getBag().drawStudent(this, 6);
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
        return 6;
    }

    @Override
    public boolean canPlay() {
        return (nInput >0 && nInput < 4);
    }
}
