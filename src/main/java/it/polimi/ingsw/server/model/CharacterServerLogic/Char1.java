package it.polimi.ingsw.server.model.CharacterServerLogic;

import it.polimi.ingsw.server.model.GameInterfaceForCharacter;

/**
 * Char1 class represents the <b>"Farmer"</b> character card. <br>
 * <b>Effect</b>: During this turn, you take control of any number of Professors even if you have the
 * same number of Students as the player who currently controls them. <br>
 * <b>Inputs required</b>: None.
 */
public class Char1 implements CharacterServerLogicInterface {

    /**
     * Method play sets the equalProfessorCalculation boolean to true in the game.
     *
     * @param game of type {@link GameInterfaceForCharacter} - the game instance that the card modifies with its effect.
     */
    @Override
    public void play(GameInterfaceForCharacter game) {
        //should set the boolean to true before the calculation
        game.setEqualProfessorCalculation();
    }

    @Override
    public boolean canPlay(int nInput) {
        return nInput == 0;
    }

}