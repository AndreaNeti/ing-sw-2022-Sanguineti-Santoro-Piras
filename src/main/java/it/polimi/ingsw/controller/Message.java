package it.polimi.ingsw.controller;

import it.polimi.ingsw.exceptions.GameException;

import java.io.Serializable;

public interface Message extends Serializable {
    void execute() throws GameException;
}
