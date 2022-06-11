package it.polimi.ingsw.Server.model;

/**
 * Char7 class represents the <b>"Knight"</b> character card. <br>
 * <b>Effect</b>: During the influence calculation this turn, you count as having 2 more influence. <br>
 * <b>Inputs required</b>: None.
 */
public class Char7 implements CharacterCard {

    /**
     * Method play sets the extraInfluence boolean to true in the game.
     * @param game of type CharacterCardGame - the game instance that the card modifies with its effect.
     */
    @Override
    public void play(CharacterCardGame game) {
        game.setExtraInfluence();
    }

    @Override
    public byte getCost() {
        return 2;
    }

    @Override
    public byte getCharId() {
        return 7;
    }

    @Override
    public boolean canPlay(int nInput) {
        return nInput == 0;
    }

}
