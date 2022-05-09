package it.polimi.ingsw.Server.model;

import it.polimi.ingsw.exceptions.GameException;
import it.polimi.ingsw.exceptions.NotEnoughStudentsException;
import it.polimi.ingsw.exceptions.UnexpectedValueException;

public class Char11 implements CharacterCard {

    @Override
    public void play(ExpertGame game) throws GameException {
        int color = game.getCharacterInputs().get(0);
        if (color < 0 || color >= Color.values().length) throw new UnexpectedValueException();
        for (Player p : game.getPlayers()) {
            byte s = (byte) Math.min(3, p.getLunchHall().howManyStudents(Color.values()[color]));
            try {
                p.getLunchHall().moveStudents(Color.values()[color], s, game.getBag());
            } catch (NotEnoughStudentsException ignored) {

                //it shouldn't happen because we calculate the minimum value of the students of that color
            }
            //TODO check if there is a better way
            game.getGameDelta().addUpdatedGC(p.getLunchHall());
        }

    }

    @Override
    public byte getCost() {
        return 3;
    }

    @Override
    public byte getCharId() {
        return 11;
    }

    @Override
    public boolean canPlay(int nInput) {
        return nInput == 1;
    }

//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (!(o instanceof Char11)) return false;
//        CharacterCard c = (CharacterCard) o;
//        return getCharId() == c.getCharId();
//    }
}

