package it.polimi.ingsw.Util;


import java.io.Serializable;

/**
 * MatchType record represent the type of games in "Eriantys".
 *
 * @param nPlayers of type {@code byte} - required number of players in the game. Ranges from 2 to 4 players.
 * @param isExpert of type {@code boolean} - boolean to describe if the game is expert (coins and character cards logic) or not.
 */
public record MatchType(byte nPlayers, boolean isExpert) implements Serializable {
    public static byte MAX_PLAYERS = 4;

    /**
     * Constructor MatchType creates a new instance of MatchType.
     *
     * @param nPlayers of type {@code byte} - required number of players in the game. Ranges from 2 to 4 players.
     * @param isExpert of type {@code boolean} - boolean to describe if the game is expert (coins and character cards logic) or not.
     */
    public MatchType {
        if (nPlayers < 2 || nPlayers > MAX_PLAYERS)
            throw new IllegalArgumentException("Only 2, 3 or 4 players matches are allowed");
    }

    /**
     * Method toString returns the attributes of the match.
     *
     * @return {@code String} - "(nPlayers) players, (expert/normal) mode".
     */
    @Override
    public String toString() {
        return nPlayers + " players, " + ((isExpert) ? "Expert mode" : "Normal mode");
    }
}