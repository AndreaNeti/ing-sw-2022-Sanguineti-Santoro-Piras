package it.polimi.ingsw.controller;

import it.polimi.ingsw.exceptions.GameException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ControllerTest {
    Controller controllerExpert;

    @Test
    void controllerTest() {
        controllerExpert = new Controller(new MatchType((byte) 4, true));
        try {
            controllerExpert.addPlayer(new PlayerHandler("Franco"));
            controllerExpert.addPlayer(new PlayerHandler("Gigi"));
            controllerExpert.addPlayer(new PlayerHandler("Carola"));
            controllerExpert.addPlayer(new PlayerHandler("Filomena"));
            controllerExpert.playCard("4");
            controllerExpert.playCard("10");
            controllerExpert.playCard("2");
            controllerExpert.playCard("3");
        } catch (GameException ex) {
            fail();
        }

    }
}