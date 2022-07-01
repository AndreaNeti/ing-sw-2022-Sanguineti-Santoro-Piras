package it.polimi.ingsw.server.model.characterServerLogic;

import it.polimi.ingsw.exceptions.serverExceptions.GameException;
import it.polimi.ingsw.exceptions.serverExceptions.NotAllowedException;
import it.polimi.ingsw.exceptions.serverExceptions.NotEnoughStudentsException;
import it.polimi.ingsw.server.model.GameInterfaceForCharacter;
import it.polimi.ingsw.utils.Color;

import java.util.List;

/**
 * Char9 class represents the <b>"Minstrel"</b> character card. <br>
 * <b>Effect</b>:You may exchange up to 2 Students between your Dining and your Entrance Room. <br>
 * <b>Inputs required</b>: 1 student color from the lunch hall and 1 student color from the entrance hall, up to 2 pairs.
 */
public class Char9 implements CharacterServerLogicInterface {


    /**
     * Method play swaps each student pair between the entrance hall and the lunch hall.
     *
     * @param game of type {@link GameInterfaceForCharacter} - the game instance that the card modifies with its effect.
     * @throws GameException if the students colors are not valid or there is not a student of the
     *                       selected color either the lunch hall or the entrance hall.
     */
    // switch the students between the lunch hall and the entrance hall one pair at a time
    @Override
    public void play(GameInterfaceForCharacter game) throws GameException {
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
        }
        game.calculateProfessor();
        game.getGameDelta().addUpdatedGC(game.getCurrentPlayer().getEntranceHall());
        game.getGameDelta().addUpdatedGC(game.getCurrentPlayer().getLunchHall());
    }

    @Override
    public boolean canPlay(int nInput) {
        return (nInput == 2 || nInput == 4);
    }

}

