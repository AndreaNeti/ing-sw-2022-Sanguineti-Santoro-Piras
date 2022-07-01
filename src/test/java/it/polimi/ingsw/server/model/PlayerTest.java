package it.polimi.ingsw.server.model;

import it.polimi.ingsw.exceptions.serverExceptions.EndGameException;
import it.polimi.ingsw.exceptions.serverExceptions.GameException;
import it.polimi.ingsw.exceptions.serverExceptions.NotAllowedException;
import it.polimi.ingsw.server.controller.Server;
import it.polimi.ingsw.utils.*;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {
    private final MatchConstants matchConstants = Server.getMatchConstants(new MatchType((byte) 2, false));
    private final Team t = new Team(HouseColor.WHITE, (byte) 2, (byte) 2);
    private Player p, p1;
    private final List<AssistantCard> assistantCardList = new ArrayList<>(11);

    public PlayerTest() {
        try {
            p = new Player("Franco", t, Wizard.AIRMAGE, matchConstants);
            Team t1 = new Team(HouseColor.BLACK, (byte) 1, (byte) 2);
            p1 = new Player("Gigi", t1, Wizard.WOODMAGE, matchConstants);
        } catch (GameException e) {
            fail(e);
        }
        // just to make indexes equal to card value
        assistantCardList.add(new AssistantCard((byte) -1, (byte) 0));
        for (byte i = 1; i <= 10; i++)
            assistantCardList.add(new AssistantCard(i, (byte) ((i + 1) / 2)));
    }

    @Test
    void constructorAndEqualsAndCompareTest() {
        assertThrows(IllegalArgumentException.class, () -> new Player(null, null, null, null));
        Player p2 = null;
        try {
            p2 = new Player("Filippo", t, Wizard.AIRMAGE, matchConstants);
        } catch (GameException e) {
            fail(e);
        }
        assertNotEquals(p, p2);
        assertNull(p.getPlayedCard());
        assertEquals(p.getWizard(), Wizard.AIRMAGE);
        assertEquals(p.getLunchHall(), p2.getLunchHall());
        assertEquals(p.getEntranceHall(), p2.getEntranceHall());
        assertNotEquals(p2, p1);
    }

    @Test
    void cardTest() {
        try {
            p.useCard(assistantCardList.get(4));
            p1.useCard(assistantCardList.get(3));
        } catch (NotAllowedException | EndGameException e) {
            fail(e);
        }
        assertEquals(p.getPlayedCard().value(), 4);
        assertEquals(p.getPlayedCard().moves(), 2);


        assertThrows(NotAllowedException.class, () -> p.useCard(assistantCardList.get(0)), "negative value");
        try {
            p.useCard(assistantCardList.get(1));
            assertEquals(p.getPlayedCard().moves(), 1);
            assertEquals(p.getPlayedCard().value(), 1);
            p.useCard(assistantCardList.get(2));
            assertEquals(p.getPlayedCard().value(), 2);
            assertEquals(p.getPlayedCard().moves(), 1);
            p.useCard(assistantCardList.get(3));
            assertEquals(p.getPlayedCard().value(), 3);
            assertEquals(p.getPlayedCard().moves(), 2);

            p.useCard(assistantCardList.get(5));
            assertEquals(p.getPlayedCard().value(), 5);
            assertEquals(p.getPlayedCard().moves(), 3);
            p.useCard(assistantCardList.get(6));
            assertEquals(p.getPlayedCard().moves(), 3);
            assertEquals(p.getPlayedCard().value(), 6);
            p.useCard(assistantCardList.get(7));
            assertEquals(p.getPlayedCard().value(), 7);
            assertEquals(p.getPlayedCard().moves(), 4);
            p.useCard(assistantCardList.get(8));
            assertEquals(p.getPlayedCard().value(), 8);
            assertEquals(p.getPlayedCard().moves(), 4);
            p.useCard(assistantCardList.get(9));
            assertEquals(p.getPlayedCard().value(), 9);
            assertEquals(p.getPlayedCard().moves(), 5);
        } catch (NotAllowedException | EndGameException e) {
            fail(e);
        }
        assertThrows(NotAllowedException.class, () -> p.useCard(assistantCardList.get(0)), "not a valid input");
        assertThrows(EndGameException.class, () -> p.useCard(assistantCardList.get(10)), "used last card");
        assertThrows(NotAllowedException.class, () -> p.useCard(assistantCardList.get(3)), "no cards to use");
    }


    @Test
    void testToString() {
        assertEquals(p.toString(), "Franco");
    }

    @Test
    void canPlayCardTest() {
        ArrayList<AssistantCard> playedCards = new ArrayList<>();
        assertTrue(p.canPlayCard(playedCards, assistantCardList.get(5)));
        playedCards.add(assistantCardList.get(4));
        assertTrue(p.canPlayCard(playedCards, assistantCardList.get(5)));
        playedCards.remove(0);

        playedCards.add(assistantCardList.get(5));
        assertFalse(p.canPlayCard(playedCards, assistantCardList.get(5)));
        playedCards.remove(0);

        try {
            p.useCard(assistantCardList.get(1));
            p.useCard(assistantCardList.get(2));
            p.useCard(assistantCardList.get(6));
            p.useCard(assistantCardList.get(7));
            p.useCard(assistantCardList.get(8));
            p.useCard(assistantCardList.get(9));
            p.useCard(assistantCardList.get(10));
        } catch (NotAllowedException | EndGameException e) {
            fail(e);
        }

        playedCards.add(assistantCardList.get(3));
        playedCards.add(assistantCardList.get(4));
        playedCards.add(assistantCardList.get(5));
        assertEquals(playedCards, p.getAssistantCards());
        assertTrue(p.canPlayCard(playedCards, assistantCardList.get(5)));
        try {
            p.useCard(assistantCardList.get(3));
            p.useCard(assistantCardList.get(4));
        } catch (NotAllowedException | EndGameException e) {
            fail(e);
        }
        assertTrue(p.canPlayCard(playedCards, assistantCardList.get(5)));
    }

    @Test
    void hashTest() {
        assertEquals(Objects.hash(p.toString()), p.hashCode());
    }

}