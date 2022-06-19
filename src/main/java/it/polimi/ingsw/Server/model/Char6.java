package it.polimi.ingsw.Server.model;

import it.polimi.ingsw.Server.model.GameComponents.GameComponent;
import it.polimi.ingsw.Util.Color;
import it.polimi.ingsw.exceptions.serverExceptions.GameException;
import it.polimi.ingsw.exceptions.serverExceptions.NotAllowedException;
import it.polimi.ingsw.exceptions.serverExceptions.NotEnoughStudentsException;

import java.util.List;

/**
 * Char6 class represents the <b>"Jester"</b> character card. <br>
 * <b>Effect</b>: You may take up to 3 Students from this card and replace them
 * with the same number of Students from your Entrance. <br>
 * <b>Inputs required</b>: 1 student color from this card and 1 student color from the entrance hall, up to 3 pairs. <br>
 * This is one of the 3 character cards that contain students and therefore extends the GameComponent class.
 */
public class Char6 extends GameComponent implements CharacterCard {

    /**
     * Constructor Char6 creates a new instance of Char6.
     *
     * @param idGameComponent of type {@code byte} - unique ID to assign to the component.
     */
    public Char6(byte idGameComponent) {
        super(6, idGameComponent);
    }

    /**
     * Method play swaps each student pair between this card and the entrance hall.
     *
     * @param game of type {@link CharacterCardGame} - the game instance that the card modifies with its effect.
     * @throws GameException if the students colors are not valid or there is not a student of the
     *                       selected color either the card or the entrance hall.
     */
    @Override
    public void play(CharacterCardGame game) throws GameException {
        List<Integer> inputs = game.getCharacterInputs();
        // input 0 and 2 and 4 are the colors of this character, input 1 and 3 and 5 are the colors of entrance hall students
        int characterColor, entranceHallColor;
        for (int i = 0; i < inputs.size(); i += 2) {
            characterColor = inputs.get(i);
            entranceHallColor = inputs.get(i + 1);
            if (characterColor < 0 || characterColor >= Color.values().length) {
                throw new NotAllowedException("Set wrong input "+i+" for characterColor");
            }
            if(entranceHallColor < 0 || entranceHallColor > Color.values().length){
                throw new NotAllowedException("Set wrong input " +i+" for entranceHallColor");
            }

            if (game.getCurrentPlayer().getEntranceHall().howManyStudents(Color.values()[entranceHallColor]) == 0 ||
                    howManyStudents(Color.values()[characterColor]) == 0) {
                throw new NotEnoughStudentsException();
            }

        }
        for (int i = 0; i < inputs.size(); i += 2) {
            characterColor = inputs.get(i);
            entranceHallColor = inputs.get(i + 1);
            swapStudents(Color.values()[characterColor], Color.values()[entranceHallColor], game.getCurrentPlayer().getEntranceHall());
        }
        //TODO check if there is another way
        game.getGameDelta().addUpdatedGC(this);
        game.getGameDelta().addUpdatedGC(game.getCurrentPlayer().getEntranceHall());
    }

    @Override
    public byte getCost() {
        return 1;
    }

    @Override
    public byte getCharId() {
        return 6;
    }

    @Override
    public boolean canPlay(int nInput) {
        return (nInput == 6 || nInput == 4 || nInput == 2);
    }

}
