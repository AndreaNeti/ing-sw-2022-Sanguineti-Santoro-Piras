package it.polimi.ingsw.model.character;

import it.polimi.ingsw.model.ExpertGame;
import it.polimi.ingsw.model.GameComponent;

public class Char6 extends GameComponent implements CharacterCard {

    private byte nInput = 0;
    private ExpertGame game;


    public Char6(ExpertGame game) {
        this.game = game;
    }

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
        return (nInput > 0 && nInput < 4);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Char0)) return false;
        CharacterCard c = (CharacterCard) o;
        return getId() == c.getId();
    }
}
