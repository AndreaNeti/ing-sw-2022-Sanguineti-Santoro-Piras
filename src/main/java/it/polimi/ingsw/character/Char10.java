package it.polimi.ingsw.character;

import it.polimi.ingsw.Bag;
import it.polimi.ingsw.GameComponent;

public class Char10 extends GameComponent implements Character {
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
}

