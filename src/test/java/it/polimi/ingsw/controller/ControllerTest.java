package it.polimi.ingsw.controller;

import it.polimi.ingsw.exceptions.GameException;
import it.polimi.ingsw.exceptions.NotEnoughStudentsException;
import it.polimi.ingsw.model.Color;
import it.polimi.ingsw.model.Game;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;


class ControllerTest {
    Controller controllerExpert, controllerNormal;

    public ControllerTest() {
        controllerExpert = new Controller(new MatchType((byte) 4, true));
        controllerNormal = new Controller(new MatchType((byte) 3, false));

        PlayerHandler p1 = Mockito.mock(PlayerHandler.class);
        PlayerHandler p2 = Mockito.mock(PlayerHandler.class);
        PlayerHandler p3 = Mockito.mock(PlayerHandler.class);
        PlayerHandler p4 = Mockito.mock(PlayerHandler.class);

        try {
            controllerExpert.addPlayer(p1, "Gigi");
            controllerExpert.addPlayer(p2, "Franco");
            controllerExpert.addPlayer(p3, "Carola");
            controllerExpert.addPlayer(p4, "Filomena");
        } catch (GameException e) {
            fail();
        }

        try {
            controllerNormal.addPlayer(p1, "Gigi");
            controllerNormal.addPlayer(p2, "Franco");
            controllerNormal.addPlayer(p3, "Carola");
        } catch (GameException e) {
            fail();
        }
    }

    @Test
    void addPlayerTest() {
        PlayerHandler p = Mockito.mock(PlayerHandler.class);
        assertThrows(GameException.class, () -> controllerExpert.addPlayer(p, "Impostor"), "Match is full");
        assertThrows(GameException.class, () -> controllerNormal.addPlayer(p, "Impostor"), "Match is full");
    }

    @Test
    void playCardTest() {
        try {
            controllerExpert.playCard((byte) 1);
        } catch (GameException | NullPointerException e) {
            fail();
        }
        assertThrows(GameException.class, () -> controllerExpert.playCard((byte) 1), "Cannot play this card");
        try {
            for (int i = 2; i < 5; i++)
                controllerExpert.playCard((byte) i);
        } catch (GameException | NullPointerException e) {
            fail();
        }
        assertThrows(GameException.class, () -> controllerExpert.playCard((byte) 6), "Not in planification phase");
    }

    @Test
    void moveTest() {
        assertThrows(GameException.class, () -> controllerExpert.move(Color.RED, 0), "Not in action phase");
        try {
            for (int i = 3; i < 7; i++)
                controllerExpert.playCard((byte) i);
        } catch (GameException | NullPointerException e) {
            fail();
        }
        assertThrows(GameException.class, () -> controllerExpert.move(Color.RED, 0), "Can't move to the selected GameComponent");
        assertThrows(GameException.class, () -> controllerExpert.move(Color.RED, -1), "Can't move to the selected GameComponent");
        int moved = 0;
        for (int i = 0; i < 3; i++) {
            for (Color c : Color.values()) {
                try {
                    controllerExpert.move(c, 1);
                    moved++;
                } catch (GameException ignored) {
                }
            }
        }
        if (moved != 3) fail();
        assertThrows(GameException.class, () -> controllerExpert.move(Color.RED, 0), "Wrong phase");
    }

    @Test
    void moveMotherNatureTest() {
        assertThrows(GameException.class, () -> controllerExpert.moveMotherNature(1), "Not in action phase");
        try {
            for (int i = 3; i < 7; i++)
                controllerExpert.playCard((byte) i);
        } catch (GameException | NullPointerException e) {
            fail();
        }
        assertThrows(GameException.class, () -> controllerExpert.moveMotherNature(1), "Wrong phase");
        int moved = 0;
        for (int i = 0; i < 3; i++) {
            for (Color c : Color.values()) {
                try {
                    controllerExpert.move(c, 1);
                    moved++;
                } catch (GameException ignored) {
                }
            }
        }
        if (moved != 3) fail();
        try {
            controllerExpert.moveMotherNature(1);
        } catch (GameException e) {
            fail();
        }
        assertThrows(GameException.class, () -> controllerExpert.moveMotherNature(1), "Wrong phase");
    }

    @Test
    void moveFromCloudTest() {
        assertThrows(GameException.class, () -> controllerExpert.moveFromCloud(0), "Not in action phase");
        try {
            for (int i = 3; i < 7; i++)
                controllerExpert.playCard((byte) i);
        } catch (GameException | NullPointerException e) {
            fail();
        }
        assertThrows(GameException.class, () -> controllerExpert.moveFromCloud(0), "Wrong phase");
        int moved = 0;
        for (int i = 0; i < 3; i++) {
            for (Color c : Color.values()) {
                try {
                    controllerExpert.move(c, 1);
                    moved++;
                } catch (GameException ignored) {
                }
            }
        }
        if (moved != 3) fail();
        assertThrows(GameException.class, () -> controllerExpert.moveFromCloud(0), "Wrong phase");
        try {
            controllerExpert.moveMotherNature(1);
        } catch (GameException e) {
            fail();
        }
        assertThrows(GameException.class, () -> controllerExpert.moveFromCloud(1), "Component is not a cloud");
        assertThrows(GameException.class, () -> controllerExpert.moveFromCloud(-5), "Component is not a cloud");
        try {
            controllerExpert.moveFromCloud(-1);
        } catch (GameException e) {
            fail();
        }
        assertThrows(GameException.class, () -> controllerExpert.moveFromCloud(0), "Wrong phase");
    }


}