package it.polimi.ingsw.Server.controller;


import java.io.Serializable;

public record MatchType(byte nPlayers, boolean isExpert) implements Serializable {
    public static byte MAX_PLAYERS = 4;

    public MatchType {
        if (nPlayers < 2 || nPlayers > MAX_PLAYERS)
            throw new IllegalArgumentException("Only 2, 3 or 4 players matches are allowed");
    }

    @Override
    public String toString() {
        return nPlayers + " players, " + ((isExpert) ? "expert mode" : "normal mode");
    }
}