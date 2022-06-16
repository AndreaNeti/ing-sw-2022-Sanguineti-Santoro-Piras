package it.polimi.ingsw.Server.model;

/**
 * Char3 class represents the <b>"Magic postman"</b> character card. <br>
 * <b>Effect</b>: Choose an Island and resolve the Island as if Mother Nature had ended her movement there.
 * Mother Nature will still move and the Island where she ends her movement will also be resolved.<br>
 * <b>Inputs required</b>: None.
 */
public class Char3 implements CharacterCard {

    /**
     * Method play sets the extraSteps boolean to true in the game.
     *
     * @param game of type {@link CharacterCardGame} - the game instance that the card modifies with its effect.
     */
    @Override
    public void play(CharacterCardGame game) {
        game.setExtraSteps();
    }

    @Override
    public byte getCost() {
        return 1;
    }

    @Override
    public byte getCharId() {
        return 3;
    }

    @Override
    public boolean canPlay(int nInput) {
        return nInput == 0;
    }

}