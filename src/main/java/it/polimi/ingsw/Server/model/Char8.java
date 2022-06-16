package it.polimi.ingsw.Server.model;

import it.polimi.ingsw.Util.Color;
import it.polimi.ingsw.exceptions.serverExceptions.NotAllowedException;

/**
 * Char8 class represents the <b>"Mushroom man"</b> character card. <br>
 * <b>Effect</b>: Choose a color of Student: during the influence
 * calculation this turn, that color adds no influence. <br>
 * <b>Inputs required</b>: Student color.
 */
public class Char8 implements CharacterCard {

    /**
     * Method play sets the selected color as ignoredColor in the game.
     *
     * @param game of type {@link CharacterCardGame} - the game instance that the card modifies with its effect.
     * @throws NotAllowedException if the color is not valid.
     */
    @Override
    public void play(CharacterCardGame game) throws NotAllowedException {
        int color = game.getCharacterInputs().get(0);
        if (color < 0 || color >= Color.values().length)
            throw new NotAllowedException("Set wrong input for color");
        game.setIgnoredColorInfluence(Color.values()[color]);
    }

    @Override
    public byte getCost() {
        return 3;
    }

    @Override
    public byte getCharId() {
        return 8;
    }

    @Override
    public boolean canPlay(int nInput) {
        return nInput == 1;
    }

}
