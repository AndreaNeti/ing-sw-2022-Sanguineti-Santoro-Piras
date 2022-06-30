package it.polimi.ingsw.Server.model;

import java.io.Serializable;

/**
 * CharacterCardDataInterface interface represents the character cards data in "Eriantys", available in the expert game mode. <br>
 * There are a total of 12 character cards classes that implement this interface, each one with its own effect on the game and
 * with its own unique ID.
 */
public interface CharacterCardDataInterface extends Serializable {
    /**
     * Method getCost returns the card's cost to play.
     *
     * @return {@code byte} - the card's cost.
     */
    byte getCost();

    /**
     * Method getCharId returns the card's unique ID.
     *
     * @return {@code byte} - the card's ID.
     */
    byte getCharId();

    /**
     * Method isUsed returns true if the card has been used.
     *
     * @return {@code boolean} - is card used.
     */
    boolean isUsed();

    /**
     * Method hasStudents returns true if the card contains students.
     *
     * @return {@code boolean} - does card contains students.
     */
    boolean hasStudents();

    /**
     * Method setUsed set the card as used.
     */
    void setUsed();

    /**
     * Method hasStudents returns true if the card contains prohibitions.
     *
     * @return {@code boolean} - does card contains students.
     */
    boolean hasProhibitions();
}
