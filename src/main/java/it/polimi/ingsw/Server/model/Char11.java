package it.polimi.ingsw.Server.model;

import it.polimi.ingsw.Util.Color;
import it.polimi.ingsw.exceptions.serverExceptions.GameException;
import it.polimi.ingsw.exceptions.serverExceptions.NotAllowedException;
import it.polimi.ingsw.exceptions.serverExceptions.NotEnoughStudentsException;

/**
 * Char11 class represents the <b>"Thief"</b> character card. <br>
 * <b>Effect</b>: Choose a type of Student every player (including yourself) must return 3
 * Students of that type from their Dining Room to the bag. If any player has fewer than 3
 * Students of that type, return as many Students as they have. <br>
 * <b>Inputs required</b>: Student color.
 */
public class Char11 implements CharacterCard {

    /**
     * Method play automatically moves up to 3 students (if available) of the selected color from
     * each player's lunch hall to the bag.
     *
     * @param game of type CharacterCardGame - the game instance that the card modifies with its effect.
     * @throws GameException if the student color is not valid.
     */
    @Override
    public void play(CharacterCardGame game) throws GameException {
        //TODO: calculate influence after moving students?
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

}

