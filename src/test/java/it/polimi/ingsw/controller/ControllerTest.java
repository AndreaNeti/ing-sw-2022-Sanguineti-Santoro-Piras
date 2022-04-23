package it.polimi.ingsw.controller;

import it.polimi.ingsw.exceptions.GameException;
import it.polimi.ingsw.exceptions.NotAllowedException;
import org.junit.jupiter.api.Test;

import java.net.Socket;

import static org.junit.Assert.fail;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ControllerTest {
    Controller controllerExpert;

    @Test
    void controllerTest() {
        controllerExpert = new Controller(new MatchType((byte) 4, true));
        try {
            controllerExpert.addPlayer(new Socket(), "Franco");
            controllerExpert.addPlayer(new Socket(), "Paolo");
            controllerExpert.addPlayer(new Socket(), "Giulia");
            controllerExpert.addPlayer(new Socket(), "Natalina");
            controllerExpert.playCard("4");
            controllerExpert.playCard("10");
            controllerExpert.playCard("2");
            controllerExpert.playCard("3");
        } catch (GameException ex) {
            fail();
        }

    }
}