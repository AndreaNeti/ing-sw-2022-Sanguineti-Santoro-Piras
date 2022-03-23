package it.polimi.ingsw.model.character;

import it.polimi.ingsw.model.GameComponent;

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

