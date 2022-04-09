package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.*;
import org.junit.jupiter.api.Test;

import java.net.Socket;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {
    Socket socket = new Socket();
    Team t = new Team(HouseColor.WHITE, (byte) 2, (byte) 2);
    Team t1 = new Team(HouseColor.BLACK, (byte) 1, (byte) 2);
    Player p, p1;

    {
        try {
            p = new Player(socket, t, Wizard.AIRMAGE, "prova", 7);
            p1 = new Player(socket, t1, Wizard.WOODMAGE, "prova", 7);
        } catch (GameException e) {
            fail();
        }
    }

    @Test
    void constructorAndEqualsTest() {
        Player p2 = null;
        try {
            p2 = new Player(socket, t, Wizard.AIRMAGE, "prova", 7);
        } catch (GameException e) {
            fail();
        }
        assertEquals(p, p2);
        assertEquals(p.getPlayedCard(), 0);
        assertEquals(p.getWizard(), Wizard.AIRMAGE);
        assertEquals(p.getPlayedCardMoves(), 0);
        assertEquals(p.getLunchHall(), p2.getLunchHall());
        assertEquals(p.getEntranceHall(), p2.getEntranceHall());
        assertEquals(p.getSocket(), p2.getSocket());
        assertNotEquals(p2, p1);
        assertNotEquals(p2.getTeam(), p1.getTeam());
    }

    @Test
    void cardTest() {
        try {
            p.useCard((byte) 4);
            p1.useCard((byte) 3);
        } catch (UsedCardException | UnexpectedValueException | NotAllowedException | EndGameException ex) {
            fail();
        }
        assertTrue(p.compare(p, p1) > 0);
        assertEquals(p.getPlayedCard(), 4);
        assertEquals(p.getPlayedCardMoves(), 2);


        assertThrows(UnexpectedValueException.class, () -> p.useCard((byte) -5), "negative value");
        assertThrows(UsedCardException.class, () -> p.useCard((byte) 4), "negative value");
        try {
            p.useCard((byte) 1);
            assertEquals(p.getPlayedCardMoves(), 1);
            p.useCard((byte) 2);
            assertEquals(p.getPlayedCardMoves(), 1);
            p.useCard((byte) 3);
            assertEquals(p.getPlayedCardMoves(), 2);
            p.useCard((byte) 5);
            assertEquals(p.getPlayedCardMoves(), 3);
            p.useCard((byte) 6);
            assertEquals(p.getPlayedCardMoves(), 3);
            p.useCard((byte) 7);
            assertEquals(p.getPlayedCardMoves(), 4);
            p.useCard((byte) 8);
            assertEquals(p.getPlayedCardMoves(), 4);
            p.useCard((byte) 9);
            assertEquals(p.getPlayedCardMoves(), 5);
        } catch (UsedCardException | UnexpectedValueException | NotAllowedException | EndGameException ex) {
            fail();
        }
        assertThrows(UsedCardException.class, () -> p.useCard((byte) 4), "negative value");
        assertThrows(EndGameException.class, () -> p.useCard((byte) 10), "used last card");
        assertThrows(NotAllowedException.class, () -> p.useCard((byte) 3), "no cards to use");
        assertThrows(UnexpectedValueException.class, () -> p.useCard((byte) -1), "not a valid input");
    }


    @Test
    void testToString() {
        assertEquals(p.toString(), "prova");
    }
}