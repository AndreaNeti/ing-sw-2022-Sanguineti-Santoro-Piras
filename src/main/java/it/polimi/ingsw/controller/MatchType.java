package it.polimi.ingsw.controller;


import java.io.Serializable;

public record MatchType(byte nPlayers, boolean isExpert) implements Serializable {

}