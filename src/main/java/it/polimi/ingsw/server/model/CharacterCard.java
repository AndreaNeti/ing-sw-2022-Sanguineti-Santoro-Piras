package it.polimi.ingsw.server.model;

import it.polimi.ingsw.exceptions.serverExceptions.EndGameException;
import it.polimi.ingsw.exceptions.serverExceptions.GameException;
import it.polimi.ingsw.server.model.characterServerLogic.CharacterServerLogicInterface;
import it.polimi.ingsw.utils.CharacterCardDataInterface;

import java.util.Objects;

/**
 * CharacterCard class represent a generic character card in "Eriantys", server side. <br>
 * This class contains both the data and the logic of a specific character card, of which it contains the instance. <br>
 * This class therefore contains all the methods required to use the character card in the server's game.
 */
public final class CharacterCard implements CharacterCardDataInterface, CharacterServerLogicInterface {
    private final CharacterCardDataInterface characterData;
    private final transient CharacterServerLogicInterface characterCard;

    /**
     * Constructor CharacterCard creates a new instance of CharacterCard.
     *
     * @param characterCard of type {@link CharacterServerLogicInterface} - instance of the specific character card logic.
     * @param data of type {@link CharacterCardDataInterface} - info about the character card data.
     */
    public CharacterCard(CharacterServerLogicInterface characterCard, CharacterCardDataInterface data) {
        this.characterCard = characterCard;
        this.characterData = data;
    }

    @Override
    public byte getCost() {
        return characterData.getCost();
    }

    @Override
    public byte getCharId() {
        return characterData.getCharId();
    }

    @Override
    public boolean isUsed() {
        return characterData.isUsed();
    }

    @Override
    public boolean hasStudents() {
        return characterData.hasStudents();
    }

    /**
     * Method play applies the card's effect to the game through the instance of the specific character card,
     * using the CharacterCardGame interface's functions.
     *
     * @param game of type {@link GameInterfaceForCharacter} - the game instance that the card modifies with its effect.
     * @throws GameException    if the inputs provided are wrong or invalid.
     * @throws EndGameException if the character card's effect trigger an endgame event (no more students in the bag,
     *                          no more towers in a team's board or less than 3 islands left)
     */
    @Override
    public void play(GameInterfaceForCharacter game) throws GameException, EndGameException {
        characterCard.play(game);
    }

    @Override
    public boolean canPlay(int nInput) {
        return characterCard.canPlay(nInput);
    }

    @Override
    public void setUsed() {
        characterData.setUsed();
    }

    @Override
    public boolean hasProhibitions() {
        return characterData.hasProhibitions();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CharacterCard that)) return false;
        return characterData.getCharId() == that.characterData.getCharId();
    }

    @Override
    public int hashCode() {
        return Objects.hash(characterData.getCharId());
    }

    @Override
    public String toString() {
        return getCharId() + ": used = " + isUsed() + ". cost = " + getCost();
    }

    /* ONLY FOR TESTS */
    CharacterServerLogicInterface getLogicCard() {
        return characterCard;
    }
}