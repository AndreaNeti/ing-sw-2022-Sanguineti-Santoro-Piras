package it.polimi.ingsw.controller;

import org.junit.jupiter.api.Test;

import java.net.Socket;

import static org.junit.jupiter.api.Assertions.*;

class ControllerTest {
    Controller controllerExpert;

    @Test
    void controllerTest() {
        controllerExpert = new Controller(4, true, (byte) 4);
        assertEquals(4, controllerExpert.getMatchId());
        controllerExpert.addPlayer(new Socket(), "Franco");
        controllerExpert.addPlayer(new Socket(), "Paolo");
        controllerExpert.addPlayer(new Socket(), "Giulia");
        controllerExpert.addPlayer(new Socket(), "Natalina");
        controllerExpert.playCard((byte) 4);
        controllerExpert.playCard((byte) 10);
        controllerExpert.playCard((byte) 2);
        controllerExpert.playCard((byte) 3);
    }
}