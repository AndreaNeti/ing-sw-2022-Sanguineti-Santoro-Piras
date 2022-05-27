package it.polimi.ingsw.Server.model;

import it.polimi.ingsw.Enum.Color;
import it.polimi.ingsw.exceptions.serverExceptions.GameException;
import it.polimi.ingsw.exceptions.serverExceptions.NotAllowedException;
import it.polimi.ingsw.exceptions.serverExceptions.NotEnoughStudentsException;

public class Char11 implements CharacterCard {

    @Override
    public void play(CharacterCardGame game) throws GameException {
        int color = game.getCharacterInputs().get(0);
        if (color < 0 || color >= Color.values().length)
            throw new NotAllowedException("Set wrong input for color");
        for (byte i = 0; i < game.getPlayerSize(); i++) {
            // getComponentById(2*i+1) returns the lunch hall of the player i
            byte s = (byte) Math.min(3, game.getComponentById(2 * i + 1).howManyStudents(Color.values()[color]));
            try { // nice bag bro
                game.getComponentById(2 * i + 1).moveStudents(Color.values()[color], s, game.getComponentById(69));
            } catch (NotEnoughStudentsException ignored) {
                //it shouldn't happen because we calculate the minimum value of the students of that color
            }

            //TODO check if there is a better way
            game.getGameDelta().addUpdatedGC(game.getComponentById(2 * i + 1));
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

