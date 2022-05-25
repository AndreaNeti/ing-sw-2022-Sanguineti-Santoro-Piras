package it.polimi.ingsw.Server.model;

import it.polimi.ingsw.Server.controller.MatchConstants;
import it.polimi.ingsw.Server.controller.MatchType;
import it.polimi.ingsw.Server.controller.Server;
import it.polimi.ingsw.Enum.HouseColor;
import it.polimi.ingsw.Enum.Wizard;
import it.polimi.ingsw.exceptions.serverExceptions.EndGameException;
import it.polimi.ingsw.exceptions.serverExceptions.GameException;
import it.polimi.ingsw.exceptions.serverExceptions.NotAllowedException;
import it.polimi.ingsw.exceptions.serverExceptions.UsedCardException;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {
    MatchConstants matchConstants = Server.getMatchConstants(new MatchType((byte) 2, false));
    Team t = new Team(HouseColor.WHITE, (byte) 2, (byte) 2);
    Team t1 = new Team(HouseColor.BLACK, (byte) 1, (byte) 2);
    Player p, p1;

    {
        try {
            p = new Player("Franco", t, Wizard.AIRMAGE, matchConstants);
            p1 = new Player("Gigi", t1, Wizard.WOODMAGE, matchConstants);
        } catch (GameException e) {
            fail();
        }
    }

    @Test
    void constructorAndEqualsAndCompareTest() {
        assertThrows(IllegalArgumentException.class, () -> new Player(null, null, null, null));
        Player p2 = null;
        try {
            p2 = new Player("Filippo", t, Wizard.AIRMAGE, matchConstants);
        } catch (GameException e) {
            fail();
        }
        assertNotEquals(p, p2);
        assertEquals(p.getPlayedCard(), 0);
        assertEquals(p.getWizard(), Wizard.AIRMAGE);
        assertEquals(p.getPlayedCardMoves(), 0);
        assertEquals(p.getLunchHall(), p2.getLunchHall());
        assertEquals(p.getEntranceHall(), p2.getEntranceHall());
        assertNotEquals(p2, p1);
    }

    @Test
    void cardTest() {
        try {
            p.useCard((byte) 4);
            p1.useCard((byte) 3);
        } catch (UsedCardException | NotAllowedException | EndGameException ex) {
            fail();
        }
        assertEquals(p.getPlayedCard(), 4);
        assertEquals(p.getPlayedCardMoves(), 2);


        assertThrows(IllegalArgumentException.class, () -> p.useCard((byte) -5), "negative value");
        assertThrows(UsedCardException.class, () -> p.useCard((byte) 4), "negative value");
        try {
            p.useCard((byte) 1);
            assertEquals(p.getPlayedCardMoves(), 1);
            assertEquals(p.getPlayedCard(), 1);
            p.useCard((byte) 2);
            assertEquals(p.getPlayedCard(), 2);
            assertEquals(p.getPlayedCardMoves(), 1);
            p.useCard((byte) 3);
            assertEquals(p.getPlayedCard(), 3);
            assertEquals(p.getPlayedCardMoves(), 2);

            p.useCard((byte) 5);
            assertEquals(p.getPlayedCard(), 5);
            assertEquals(p.getPlayedCardMoves(), 3);
            p.useCard((byte) 6);
            assertEquals(p.getPlayedCardMoves(), 3);
            assertEquals(p.getPlayedCard(), 6);
            p.useCard((byte) 7);
            assertEquals(p.getPlayedCard(), 7);
            assertEquals(p.getPlayedCardMoves(), 4);
            p.useCard((byte) 8);
            assertEquals(p.getPlayedCard(), 8);
            assertEquals(p.getPlayedCardMoves(), 4);
            p.useCard((byte) 9);
            assertEquals(p.getPlayedCard(), 9);
            assertEquals(p.getPlayedCardMoves(), 5);
        } catch (UsedCardException | NotAllowedException | EndGameException ex) {
            fail();
        }
        assertThrows(UsedCardException.class, () -> p.useCard((byte) 4), "negative value");
        assertThrows(EndGameException.class, () -> p.useCard((byte) 10), "used last card");
        assertThrows(NotAllowedException.class, () -> p.useCard((byte) 3), "no cards to use");
        assertThrows(IllegalArgumentException.class, () -> p.useCard((byte) -1), "not a valid input");
    }


    @Test
    void testToString() {
        assertEquals(p.toString(), "Franco");
    }

    @Test
    void canPlayCardTest() {
        ArrayList<Byte> playedCards = new ArrayList<>();
        assertTrue(p.canPlayCard(playedCards, (byte) 5));
        playedCards.add((byte) 4);
        assertTrue(p.canPlayCard(playedCards, (byte) 5));
        playedCards.remove(0);

        playedCards.add((byte) 5);
        assertFalse(p.canPlayCard(playedCards, (byte) 5));


        try {
            p.useCard((byte) 1);
            p.useCard((byte) 2);
            p.useCard((byte) 6);
            p.useCard((byte) 7);
            p.useCard((byte) 8);
            p.useCard((byte) 9);
            p.useCard((byte) 10);
        } catch (UsedCardException | NotAllowedException | EndGameException e) {
            fail();
        }

        playedCards.add((byte) 3);
        playedCards.add((byte) 4);
        playedCards.add((byte) 5);
        assertTrue(p.canPlayCard(playedCards, (byte) 5));


    }

}