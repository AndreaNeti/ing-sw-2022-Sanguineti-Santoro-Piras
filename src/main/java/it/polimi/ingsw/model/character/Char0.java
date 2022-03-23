package it.polimi.ingsw.model.character;

import it.polimi.ingsw.model.GameComponent;

public class Char0 extends GameComponent implements Character{

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
}
