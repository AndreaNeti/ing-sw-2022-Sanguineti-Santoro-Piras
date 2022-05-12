package it.polimi.ingsw.Server.controller;


import java.io.Serializable;

public record MatchType(byte nPlayers, boolean isExpert) implements Serializable {
    public MatchType {
        if (nPlayers < 2 || nPlayers > 4)
            throw new IllegalArgumentException("Only 2,3 or 4 players matches are allowed");
    }
}