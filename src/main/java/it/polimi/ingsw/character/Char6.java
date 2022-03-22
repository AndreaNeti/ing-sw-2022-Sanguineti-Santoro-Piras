package it.polimi.ingsw.character;

import it.polimi.ingsw.Bag;
import it.polimi.ingsw.GameComponent;

public class Char6 extends GameComponent implements Character {
    public Char6(int id) {
        super(id);
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
