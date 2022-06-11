package it.polimi.ingsw.Server.model;

/**
 * Char5 class represents the <b>"Centaur"</b> character card. <br>
 * <b>Effect</b>: When resolving a Conquering on an Island, Towers do not count towards influence. <br>
 * <b>Inputs required</b>: None.
 */
public class Char5 implements CharacterCard {

    /**
     * Method play sets the towerInfluence boolean to false in the game.
     *
     * @param game of type CharacterCardGame - the game instance that the card modifies with its effect.
     */
    @Override
    public void play(CharacterCardGame game) {
        game.removeTowerInfluence();
    }

    @Override
    public byte getCost() {
        return 3;
    }

    @Override
    public byte getCharId() {
        return 5;
    }

    @Override
    public boolean canPlay(int nInput) {
        return nInput == 0;
    }

}
