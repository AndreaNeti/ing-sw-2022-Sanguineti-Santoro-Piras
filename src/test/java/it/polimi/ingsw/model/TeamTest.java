package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.EndGameException;
import it.polimi.ingsw.exceptions.NotAllowedException;
import org.junit.jupiter.api.Test;

import java.net.Socket;

import static org.junit.jupiter.api.Assertions.*;

class TeamTest {
    Team t = new Team(HouseColor.GREY, (byte) 2, (byte) 6);
    Socket socket = new Socket();
    Player p = new Player(socket, t, Wizard.AIRMAGE, "scemo", 7);

    @Test
    void constructorAndEqualsTest() {
        Team t1 = new Team(HouseColor.GREY, (byte) 2, (byte) 6);
        assertEquals(t, t1);
        assertEquals(t1.getHouseColor(), HouseColor.GREY);
    }

    @Test
    void addAndGetPlayerTest() {
        assertTrue(t.getPlayers().isEmpty());
        try {
            t.addPlayer(p);
            t.addPlayer(p);
        } catch (NotAllowedException ex) {
            fail();
        }
        assertEquals(t.getPlayers().get(0), p);

        assertThrows(NotAllowedException.class, () -> t.addPlayer(p),
                "team is full,should launch exception");

    }

    @Test
    void removePlayer() {
        try {
            t.addPlayer(p);
        } catch (NotAllowedException ex) {
            fail();
        }
        try {
            t.removePlayer(p);
        } catch (NotAllowedException e) {
            fail();
        }
        assertTrue(t.getPlayers().isEmpty());

        assertThrows(NotAllowedException.class, () -> t.removePlayer(p),
                "player not present");
    }


    @Test
    void movePlayer() {
        Team t1 = new Team(HouseColor.WHITE, (byte) 2, (byte) 6);
        Player p1 = new Player(socket, t, Wizard.ELECTROMAGE, "scemo1", 7);

        try {
            t.addPlayer(p);
            t1.addPlayer(p1);
        } catch (NotAllowedException ex) {
            fail();
        }
        assertThrows(NotAllowedException.class, () -> t.movePlayer(p1, t1),
                "player not present");
        try {
            t.movePlayer(p, t1);
            assertTrue(t1.getPlayers().contains(p));
            assertTrue(t.getPlayers().isEmpty());
        } catch (NotAllowedException ex) {
            fail();
        }

    }


    @Test
    void removeAndAddTowersTest() {
        try {
            t.removeTowers((byte) 3);
            t.addTowers((byte) 2);
        } catch (NotAllowedException | EndGameException ex1) {
            fail();
        }
        assertEquals(t.getTowersLeft(), 5);
        assertThrows(EndGameException.class, () -> t.removeTowers((byte) 10),
                "tower <=0, should launch winner exception");
    }
}