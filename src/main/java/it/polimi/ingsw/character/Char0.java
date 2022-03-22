package it.polimi.ingsw.character;

import it.polimi.ingsw.Bag;
import it.polimi.ingsw.GameComponent;

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
