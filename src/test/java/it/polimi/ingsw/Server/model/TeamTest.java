package it.polimi.ingsw.Server.model;

import it.polimi.ingsw.Util.HouseColor;
import it.polimi.ingsw.Util.Wizard;
import it.polimi.ingsw.Server.controller.MatchConstants;
import it.polimi.ingsw.Server.controller.MatchType;
import it.polimi.ingsw.Server.controller.Server;
import it.polimi.ingsw.exceptions.serverExceptions.EndGameException;
import it.polimi.ingsw.exceptions.serverExceptions.GameException;
import it.polimi.ingsw.exceptions.serverExceptions.NotAllowedException;
import org.junit.jupiter.api.Test;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

class TeamTest {
    Team t = new Team(HouseColor.GREY, (byte) 2, (byte) 6);
    MatchConstants matchConstants = Server.getMatchConstants(new MatchType((byte) 2, true));
    Player p;

    {
        try {
            p = new Player("Franco", t, Wizard.AIRMAGE, matchConstants);
        } catch (GameException e) {
            fail();
        }
    }

    @Test
    void constructorAndEqualsTest() {
        Team t1 = new Team(HouseColor.GREY, (byte) 2, (byte) 6);
        assertEquals(t, t1);
        assertEquals(t1.getHouseColor(), HouseColor.GREY);
    }

    @Test
    void addAndGetPlayerTest() {
        assertEquals(1, t.getPlayers().size());
        assertThrows(NotAllowedException.class, () -> t.addPlayer(p), "player already present in team");
        try {
            assertThrows(IllegalArgumentException.class, () -> t.addPlayer(null), "Passing null player to team");
            Player p1 = new Player("Gigi", new Team(HouseColor.BLACK, (byte) 2, (byte) 6), Wizard.WOODMAGE, matchConstants);
            t.addPlayer(p1);
        } catch (GameException ex) {
            fail();
        }
        assertEquals(t.getPlayers().get(0), p);
        assertEquals(t.getPlayers().size(), 2);

        assertThrows(NotAllowedException.class, () -> t.addPlayer(p),
                "team is full,should launch exception");

    }

    @Test
    void removeAndAddTowersTest() {
        try {
            t.removeTowers((byte) 3);
            t.addTowers((byte) 2);
        } catch (EndGameException ex1) {
            fail();
        }
        assertEquals(t.getTowersLeft(), 5);
        assertThrows(EndGameException.class, () -> t.removeTowers((byte) 10),
                "tower <=0, should launch winner exception");
    }

    @Test
    void testToString() {
        assertEquals(t.getHouseColor().name().toLowerCase() + " team", t.toString());
    }

    @Test
    void testHashCode() {
        assertEquals(Objects.hash(t.getHouseColor()), t.hashCode());
    }
}