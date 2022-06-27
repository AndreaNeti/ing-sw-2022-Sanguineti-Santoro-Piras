package it.polimi.ingsw.Server.controller;

import it.polimi.ingsw.Util.Color;

import java.util.*;

/**
 * ExpertGameDelta class is used to send all the server's game changes to the clients, instead of sending
 * the whole game each time an update happens. <br>
 * It extends the GameDelta class and adds info about the game's characters and if they've been used or not,
 * coins left for each player, coins and prohibitions left in the game, values of extraSteps and ignoredColorInfluence
 * attributes of ExpertGame.
 */
public class ExpertGameDelta extends GameDelta {
    //index of the character, id of the character
    private List<Byte> characters;
    // player id, new playerCoinsLeft
    private Map<Byte, Integer> updatedCoinPlayer;
    private Byte newProhibitionsLeft;
    private Integer newCoinsLeft;
    private Boolean extraSteps;
    private Color ignoredColorInfluence;
    //charId, used
    private Map<Byte, Boolean> usedCharacter;


    /**
     * Constructor ExpertGameDelta creates a new instance of ExpertGameDelta
     */
    public ExpertGameDelta() {
        super();
    }

    /**
     * Method clear sets the ExpertGameDelta attributes to their default values.
     */
    @Override
    public void clear() {
        super.clear();
        characters = null;
        updatedCoinPlayer = null;
        newCoinsLeft = null;
        newProhibitionsLeft = null;
        extraSteps = null;
        ignoredColorInfluence = null;
        usedCharacter = null;
    }

    /**
     * Method addCharacterCard adds a character card to the expert game delta.
     *
     * @param index of type {@code byte} - position on the list on which the card is added.
     * @param id of type {@code byte} - unique ID of the character card.
     */
    public void addCharacterCard(byte index, byte id) {
        if (characters == null)
            characters = new ArrayList<>();
        characters.add(index, id);
    }

    /**
     * Method setUpdatedCoinPlayer adds the player and their new amount of coins to the expert game delta.
     *
     * @param playerId     of type {@code byte} - ID of the player.
     * @param newCoinsLeft of type {@code int} - new amount of coins for the player.
     */
    @Override
    public void setUpdatedCoinPlayer(byte playerId, int newCoinsLeft) {
        if (updatedCoinPlayer == null)
            updatedCoinPlayer = new HashMap<>();
        updatedCoinPlayer.put(playerId, newCoinsLeft);
    }

    /**
     * Method setUsedCharacter adds a character card and info about if it has been already used to the expert game delta.
     *
     * @param charId of type {@code byte} - ID of the used character card.
     * @param used   of type {@code boolean} - boolean used to know if the character card has been already used.
     */
    @Override
    public void setUsedCharacter(byte charId, boolean used) {
        if (usedCharacter == null)
            usedCharacter = new HashMap<>();
        usedCharacter.put(charId, used);
    }

    /**
     * Method setNewCoinsLeft adds the new amount of coins left in the game to the expert game delta.
     *
     * @param newCoinsLeft of type {@code int} - new amount of coins left in the game.
     */
    @Override
    public void setNewCoinsLeft(int newCoinsLeft) {
        this.newCoinsLeft = newCoinsLeft;
    }

    /**
     * Method setNewProhibitionsLeft adds the new amount of prohibitions left in the game to the expert game delta.
     *
     * @param newProhibitionsLeft of type {@code byte} - new amount of prohibitions left in the game.
     */
    @Override
    public void setNewProhibitionsLeft(byte newProhibitionsLeft) {
        this.newProhibitionsLeft = newProhibitionsLeft;
    }

    /**
     * Method setExtraSteps adds the new value of the extraSteps boolean in the game to the expert game delta.
     *
     * @param extraSteps of type {@code Boolean} - new value of the extraSteps boolean in the game.
     */
    @Override
    public void setExtraSteps(Boolean extraSteps) {
        this.extraSteps = extraSteps;
    }

    /**
     * Method setIgnoredColorInfluence adds the color that will be ignored during influence
     * calculation in the game to the expert game delta.
     *
     * @param ignoredColorInfluence of type {@link Color} - color that will be ignored.
     */
    @Override
    public void setIgnoredColorInfluence(Color ignoredColorInfluence) {
        this.ignoredColorInfluence = ignoredColorInfluence;
    }

    /**
     * Method getCharacters returns all the character cards in the expert game delta.
     *
     * @return {@code List}<{@code Byte}> - list of all the character cards.
     */
    @Override
    public List<Byte> getCharacters() {
        if (characters == null) return new ArrayList<>();
        return characters;
    }

    /**
     * Method getUpdatedCoinPlayer returns all the players with a new amount of coins left. <br>
     * <b>Map entry</b>: (Player's ID - new coins amount).
     *
     * @return {@code Map}<{@code Byte, Byte}> - map of all the players with their new coins left.
     */
    @Override
    public Map<Byte, Integer> getUpdatedCoinPlayer() {
        if (updatedCoinPlayer == null) return Collections.emptyMap();
        return updatedCoinPlayer;
    }

    /**
     * Method getUsedCharacter returns all the character cards with a new "used" boolean value. <br>
     * <b>Map entry</b>: (Character card's ID - true/false).
     *
     * @return {@code Map}<{@code Byte, Boolean}> - map of the character cards with their new "used" value.
     */
    @Override
    public Map<Byte, Boolean> getUsedCharacter() {
        if (usedCharacter == null) return Collections.emptyMap();
        return usedCharacter;
    }

    /**
     * Method getNewCoinsLeft returns the new amounts of coins left in the game.
     *
     * @return {@code Optional}<{@code Integer}> - if present the new amount of coins left in the game, Empty Optional else.
     */
    @Override
    public Optional<Integer> getNewCoinsLeft() {
        return Optional.ofNullable(newCoinsLeft);
    }

    /**
     * Method getNewProhibitionsLeft returns the new amounts of prohibitions left in the game.
     *
     * @return {@code Optional}<{@code Byte}> - if present the new amount of prohibitions left in the game, Empty Optional else.
     */
    @Override
    public Optional<Byte> getNewProhibitionsLeft() {
        return Optional.ofNullable(newProhibitionsLeft);
    }

    /**
     * Method isExtraSteps returns the new value of extraSteps boolean in the game.
     *
     * @return {@code Optional}<{@code Boolean}> - if present the new value of extraSteps boolean in the game, Empty Optional else.
     */
    @Override
    public Optional<Boolean> isExtraSteps() {
        return Optional.ofNullable(extraSteps);
    }

    /**
     * Method getIgnoredColorInfluence returns the new color that will be ignored in the influence calculation in the game.
     *
     * @return {@code Optional}<{@link Color}> - if present the new ignored color in the game, Empty Optional else.
     */
    @Override
    public Optional<Color> getIgnoredColorInfluence() {
        return Optional.ofNullable(ignoredColorInfluence);
    }

    @Override
    public String toString() {
        return super.toString()+",isExtraSteps "+extraSteps+",ignoredColor "+ignoredColorInfluence+", updateCharacters "+
                usedCharacter+", updatedCoinPlayer "+ updatedCoinPlayer+" ,newProhibitionLeft " + newProhibitionsLeft+
                "newCoinsLeft " +newCoinsLeft;


    }
}
