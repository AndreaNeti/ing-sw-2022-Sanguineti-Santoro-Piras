package it.polimi.ingsw.Util;

import java.io.Serializable;

/**
 * CharacterCardDataInterface interface represents the character cards data in "Eriantys". It contains the following info: <br>
 * the card's cost, <br>
 * the card's unique id, <br>
 * if the card has been already used, <br>
 * if the card can contain students. <br>
 * There are a total of 12 character cards classes that implement this interface, each with its own cost and unique ID.
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
     * @return {@code boolean} - true if the card has been used, false else.
     */
    boolean isUsed();

    /**
     * Method hasStudents returns true if the card contains students.
     *
     * @return {@code boolean} - true if the card contains students, false else.
     */
    boolean hasStudents();

    /**
     * Method setUsed set the card as used.
     */
    void setUsed();
}
