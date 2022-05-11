package it.polimi.ingsw.Server.model;

import it.polimi.ingsw.Enum.Color;
import it.polimi.ingsw.exceptions.EndGameException;
import it.polimi.ingsw.exceptions.GameException;
import it.polimi.ingsw.exceptions.UnexpectedValueException;

public class Char10 extends GameComponent implements CharacterCard {

    public Char10(byte idGameComponent){
        super(4, idGameComponent);
    }
    @Override
    public void play(ExpertGame game) throws GameException, EndGameException {
        int color=game.getCharacterInputs().get(0);
        if (color < 0 || color >= Color.values().length){
            throw new UnexpectedValueException();
        }
        this.moveStudents(Color.values()[color],(byte) 1,game.getCurrentPlayer().getLunchHall());
        game.getGameDelta().addUpdatedGC(this);
        game.getGameDelta().addUpdatedGC(game.getCurrentPlayer().getLunchHall());
        game.drawStudents(this, (byte) 1);
    }

    @Override
    public byte getCost() {
        return 2;
    }


    @Override
    public byte getCharId() {
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
//        return getCharId() == c.getCharId();
//    }
}

