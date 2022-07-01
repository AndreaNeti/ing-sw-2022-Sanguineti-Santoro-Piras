package it.polimi.ingsw.Server.model;

import it.polimi.ingsw.Server.model.CharacterServerLogic.CharacterServerLogicInterface;
import it.polimi.ingsw.Util.CharacterCardDataInterface;
import it.polimi.ingsw.exceptions.serverExceptions.EndGameException;
import it.polimi.ingsw.exceptions.serverExceptions.GameException;

import java.util.Objects;

/**
 * CharacterCard class represent the character cards feature in "Eriantys". It contains both the data and the
 * logic of the character card, separated in order to
 */
public final class CharacterCard implements CharacterCardDataInterface, CharacterServerLogicInterface {
    private final CharacterCardDataInterface characterData;
    private final transient CharacterServerLogicInterface characterCard;

    /**
     * Constructor CharacterCard creates a new instance of CharacterCard.
     *
     * @param characterCard of type {@link CharacterServerLogicInterface} - instance of the character card.
     * @param data of type {@link CharacterCardDataInterface} -
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