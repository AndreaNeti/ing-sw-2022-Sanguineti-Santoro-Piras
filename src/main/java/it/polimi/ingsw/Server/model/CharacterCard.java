package it.polimi.ingsw.Server.model;

import it.polimi.ingsw.exceptions.serverExceptions.EndGameException;
import it.polimi.ingsw.exceptions.serverExceptions.GameException;

import java.util.Objects;

public class CharacterCard implements CharacterCardDataInterface, CharacterCardLogicInterface {
    private final CharacterCardDataInterface characterData;
    private final transient CharacterCardLogicInterface characterCard;

    public CharacterCard(CharacterCardLogicInterface characterCard, CharacterCardDataInterface data) {
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
    CharacterCardLogicInterface getLogicCard() {
        return characterCard;
    }
}