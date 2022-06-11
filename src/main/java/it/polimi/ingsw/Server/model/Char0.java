package it.polimi.ingsw.Server.model;

import it.polimi.ingsw.Util.Color;
import it.polimi.ingsw.Server.model.GameComponents.GameComponent;
import it.polimi.ingsw.exceptions.serverExceptions.EndGameException;
import it.polimi.ingsw.exceptions.serverExceptions.GameException;
import it.polimi.ingsw.exceptions.serverExceptions.NotAllowedException;

import java.util.List;

/**
 * Char0 class represents the <b>"Monk"</b> character card. <br>
 * <b>Effect</b>: Take 1 Student from this card and place it on an Island of your choice.
 * Then, draw a new Student from the Bag and place it on this card. <br>
 * <b>Inputs required</b>: 1 student color from this card. <br>
 * This is one of the 3 character cards that contain students and therefore extends the GameComponent class.
 */
public class Char0 extends GameComponent implements CharacterCard {

    /**
     * Constructor Char0 creates a new instance of Char0
     *
     * @param idGameComponent of type byte - unique ID to assign to the component.
     */
    public Char0(byte idGameComponent) {
        super(4, idGameComponent);
    }

    /**
     * Method play moves a student of choice from the card to an island of choice and then draws another student from
     * the bag to the card.
     *
     * @param game of type CharacterCardGame - the game instance that the card modifies with its effect.
     * @throws GameException if the student's color or island's id are not valid or there is not a student of the
     * selected color on the card.
     * @throws EndGameException if after drawing a student, the bag has none left.
     */
    @Override
    public void play(CharacterCardGame game) throws GameException, EndGameException {
        List<Integer> inputs = game.getCharacterInputs();
        // input 0 is the color chosen, input 1 is the island id chosen
        int color = inputs.get(0), idIsland = inputs.get(1);
        //TODO Check this idIsland
        if (color < 0 || color >= Color.values().length)
            throw new NotAllowedException("Set wrong inputs for color");
        if (idIsland > 2 * game.getPlayerSize() + 12 || idIsland < 2 * game.getPlayerSize())
            throw new NotAllowedException("Set wrong input for id Island");
        GameComponent islandDestination = game.getComponentById(idIsland);
        this.moveStudents(Color.values()[color], (byte) 1, islandDestination);
        game.drawStudents(this, (byte) 1);
        game.getGameDelta().addUpdatedGC(this);
        game.getGameDelta().addUpdatedGC(islandDestination);
    }

    @Override
    public byte getCost() {
        return 1;
    }

    @Override
    public byte getCharId() {
        return 0;
    }

    @Override
    public boolean canPlay(int nInput) {
        return nInput == 2;
    }

}
