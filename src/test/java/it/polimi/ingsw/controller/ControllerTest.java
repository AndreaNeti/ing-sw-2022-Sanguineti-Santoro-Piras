package it.polimi.ingsw.controller;

import org.junit.jupiter.api.Test;

class ControllerTest {
    Controller controllerExpert;

    @Test
    void controllerTest() {
        controllerExpert = new Controller(new MatchType((byte) 4, true));
        /*try {
            controllerExpert.addPlayer("Franco");
            controllerExpert.addPlayer("Gigi");
            controllerExpert.addPlayer("Carola");
            controllerExpert.addPlayer("Filomena");
            controllerExpert.playCard("4");
            controllerExpert.playCard("10");
            controllerExpert.playCard("2");
            controllerExpert.playCard("3");
        } catch (GameException ex) {
            fail();
        }*/

    }
}