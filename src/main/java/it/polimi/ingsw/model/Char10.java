package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.EndGameException;
import it.polimi.ingsw.exceptions.GameException;

public class Char10 extends GameComponent implements CharacterCard {

    public Char10(){
        super(4);
    }
    @Override
    public void play(ExpertGame game) throws GameException, EndGameException {
        moveStudents(Color.values()[game.getCharacterInputs().get(0)], (byte) 1, game.getCurrentPlayer().getLunchHall());
        game.drawStudents(this, (byte) 1);
    }

    @Override
    public byte getCost() {
        return 2;
    }


    @Override
    public int getId() {
        return 10;
    }

    @Override
    public boolean canPlay(int nInput) {
        return nInput == 1;
    }

//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (!(o instanceof Char10)) return false;
//        CharacterCard c = (CharacterCard) o;
//        return getId() == c.getId();
//    }
}

