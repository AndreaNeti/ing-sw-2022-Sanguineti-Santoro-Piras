package it.polimi.ingsw.network;

import it.polimi.ingsw.util.CharacterCardDataInterface;
import it.polimi.ingsw.util.Color;

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
    private Set<CharacterCardDataInterface> characters;
    // player id, new playerCoinsLeft
    private Map<Byte, Integer> updatedCoinPlayer;
    private Byte newProhibitionsLeft;
    private Integer newCoinsLeft;
    private Boolean extraSteps;
    private Color ignoredColorInfluence;


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
    }

    /**
     * Method addCharacterCard adds a character card to the expert game delta.
     *
     * @param characterCardData of type {@code CharacterCardData} - the record containing the card's data.
     */
    @Override
    public void addCharacterCard(CharacterCardDataInterface characterCardData) {
        if (characters == null)
            characters = new HashSet<>();
        characters.add(characterCardData);
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
     * @return {@code Set}<{@code CharacterCardData}> - list of all the character cards data.
     */
    @Override
    public Set<CharacterCardDataInterface> getCharacters() {
        if (characters == null) return Collections.emptySet();
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
        return super.toString() + ",isExtraSteps " + extraSteps + ",ignoredColor " + ignoredColorInfluence + ", updateCharacters " +
                characters + ", updatedCoinPlayer " + updatedCoinPlayer + " ,newProhibitionLeft " + newProhibitionsLeft +
                "newCoinsLeft " + newCoinsLeft;


    }
}
