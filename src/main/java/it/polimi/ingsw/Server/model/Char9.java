package it.polimi.ingsw.Server.model;

import it.polimi.ingsw.Util.Color;
import it.polimi.ingsw.exceptions.serverExceptions.GameException;
import it.polimi.ingsw.exceptions.serverExceptions.NotAllowedException;
import it.polimi.ingsw.exceptions.serverExceptions.NotEnoughStudentsException;

import java.util.List;

public class Char9 implements CharacterCard {


    // switch the students between the lunch hall and the entrance hall one pair at a time
    @Override
    public void play(CharacterCardGame game) throws GameException {
        List<Integer> inputs = game.getCharacterInputs();
        // input 0 and 2 are the colors of lunch hall students, input 1 and 3 are the colors of entrance hall students
        int lunchHallColor, entranceHallColor;
        for (int i = 0; i < inputs.size(); i += 2) {
            lunchHallColor = inputs.get(i);
            entranceHallColor = inputs.get(i + 1);
            if (lunchHallColor < 0 || lunchHallColor >= Color.values().length) {
                throw new NotAllowedException("Set wrong input " + i + " for lunchHallColor");
            }
            if (entranceHallColor < 0 || entranceHallColor >= Color.values().length) {
                throw new NotAllowedException("Set wrong input " + i + " for entranceHallColor");
            }
            if (game.getCurrentPlayer().getEntranceHall().howManyStudents(Color.values()[entranceHallColor]) == 0 ||
                    game.getCurrentPlayer().getLunchHall().howManyStudents(Color.values()[lunchHallColor]) == 0) {
                throw new NotEnoughStudentsException();
            }
        }

        for (int i = 0; i < inputs.size(); i += 2) {
            lunchHallColor = inputs.get(i);
            entranceHallColor = inputs.get(i + 1);
            game.getCurrentPlayer().getEntranceHall().swapStudents(Color.values()[entranceHallColor], Color.values()[lunchHallColor], game.getCurrentPlayer().getLunchHall());
            // TODO find a way to add coins
        }
        game.calculateProfessor();
        game.getGameDelta().addUpdatedGC(game.getCurrentPlayer().getEntranceHall());
        game.getGameDelta().addUpdatedGC(game.getCurrentPlayer().getLunchHall());
    }

    @Override
    public byte getCost() {
        return 1;
    }

    @Override
    public byte getCharId() {
        return 9;
    }

    @Override
    public boolean canPlay(int nInput) {
        return (nInput == 2 || nInput == 4);
    }

}

