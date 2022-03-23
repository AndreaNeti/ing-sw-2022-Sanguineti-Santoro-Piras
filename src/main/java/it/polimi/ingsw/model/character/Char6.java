package it.polimi.ingsw.model.character;

import it.polimi.ingsw.model.GameComponent;

public class Char6 extends GameComponent implements Character {
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
}
