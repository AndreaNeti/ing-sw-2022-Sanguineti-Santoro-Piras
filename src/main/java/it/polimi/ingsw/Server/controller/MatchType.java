package it.polimi.ingsw.Server.controller;


import java.io.Serializable;

public record MatchType(byte nPlayers, boolean isExpert) implements Serializable {

}