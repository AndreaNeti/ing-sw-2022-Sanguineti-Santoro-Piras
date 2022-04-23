package it.polimi.ingsw.controller;


public class MatchType {
    private final byte nPlayers;
    private final boolean isExpert;

    public MatchType(byte nPlayers, boolean isExpert) {
        this.nPlayers = nPlayers;
        this.isExpert = isExpert;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MatchType)) return false;
        MatchType matchType = (MatchType) o;
        return nPlayers == matchType.nPlayers && isExpert == matchType.isExpert;
    }
}
