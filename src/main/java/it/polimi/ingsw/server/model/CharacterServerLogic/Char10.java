package it.polimi.ingsw.server.model.CharacterServerLogic;

import it.polimi.ingsw.exceptions.serverExceptions.EndGameException;
import it.polimi.ingsw.exceptions.serverExceptions.GameException;
import it.polimi.ingsw.exceptions.serverExceptions.NotAllowedException;
import it.polimi.ingsw.server.model.GameComponents.GameComponent;
import it.polimi.ingsw.server.model.GameInterfaceForCharacter;
import it.polimi.ingsw.utils.Color;

/**
 * Char10 class represents the <b>"Spoiled princess"</b> character card. <br>
 * <b>Effect</b>: Take 1 Student from this card and place it in your Dining Room.
 * Then, draw a new Student from the Bag and place it on this card. <br>
 * <b>Inputs required</b>: 1 student color from this card. <br>
 * This is one of the 3 character cards that contain students and therefore extends the GameComponent class.
 */
public class Char10 extends GameComponent implements CharacterServerLogicInterface {

    /**
     * Constructor Char10 creates a new instance of Char10.
     *
     * @param idGameComponent of type {@code byte} - unique ID to assign to the component.
     */
    public Char10(byte idGameComponent) {
        super(4, idGameComponent);
    }

    /**
     * Method play moves a student of choice from the card to the entrance hall and then draws another student from
     * the bag to the card.
     *
     * @param game of type {@link GameInterfaceForCharacter} - the game instance that the card modifies with its effect.
     * @throws GameException    if the student's color is not valid or there is not a student of the
     *                          selected color on the card.
     * @throws EndGameException if after drawing a student, the bag has none left.
     */
    @Override
    public void play(GameInterfaceForCharacter game) throws GameException, EndGameException {
        int color = game.getCharacterInputs().get(0);
        if (color < 0 || color >= Color.values().length) {
            throw new NotAllowedException("Set wrong input for color");
        }
        this.moveStudents(Color.values()[color], (byte) 1, game.getCurrentPlayer().getLunchHall());
        game.drawStudents(this, (byte) 1);
        game.calculateProfessor();

        game.getGameDelta().addUpdatedGC(this);
        game.getGameDelta().addUpdatedGC(game.getCurrentPlayer().getLunchHall());
    }

    @Override
    public boolean canPlay(int nInput) {
        return nInput == 1;
    }

}

