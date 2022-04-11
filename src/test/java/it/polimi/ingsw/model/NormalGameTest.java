package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.EndGameException;
import it.polimi.ingsw.exceptions.GameException;
import it.polimi.ingsw.exceptions.NotExpertGameException;
import org.junit.jupiter.api.Test;

import java.net.Socket;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class NormalGameTest {
    Socket socket1 = new Socket(), socket2 = new Socket();
    Team t1 = new Team(HouseColor.WHITE, (byte) 1, (byte) 8);
    Team t2 = new Team(HouseColor.BLACK, (byte) 1, (byte) 8);
    Player p1, p2;

    {
        try {
            p1 = new Player(socket1, t1, Wizard.AIRMAGE, "Franco", 7);
            p2 = new Player(socket2, t2, Wizard.ELECTROMAGE, "Gigi", 7);
        } catch (GameException e) {
            fail();
        }
    }

    ArrayList<Player> players = new ArrayList<>() {{
        add(p1);
        add(p2);
    }};
    ArrayList<Team> teamList = new ArrayList<>() {{
        add(t1);
        add(t2);
    }};

    NormalGame game = new NormalGame((byte) 2, teamList, players) {{
        setCurrentPlayer(p1);
    }};

    @Test
    void getTest() {
        assertEquals(game.getTeams(), teamList);
        assertEquals(game.getPlayers(), players);
        assertEquals(game.getIslands().size(), 12);
        assertEquals(game.getBag().getClass(), Bag.class);
        assertEquals(game.getCurrentPlayer(), p1);
        for (Player p : game.getProfessor())
            assertNull(p);
    }


    @Test
    void move() {
    }

    @Test
    void playCard() {
        try {
            game.playCard((byte) 5);
        } catch (GameException | EndGameException e) {
            fail();
        }
        assertEquals(5, p1.getPlayedCard());
        assertEquals(3, p1.getPlayedCardMoves());
        game.setCurrentPlayer(p2);
        try {
            game.playCard((byte) 6);
        } catch (GameException | EndGameException e) {
            fail();
        }
        assertEquals(6, p2.getPlayedCard());
        assertEquals(3, p2.getPlayedCardMoves());
        assertTrue(p1.compare(p2, p1) > 0);
    }

    @Test
    void moveMotherNature() {
    }

    @Test
    void calculateWinner() {
    }

    @Test
    void refillClouds() {
    }

    @Test
    void setCharacterInput() {
        assertThrows(NotExpertGameException.class, () -> game.setCharacterInput(0));
    }

    @Test
    void chooseCharacter() {
        assertThrows(NotExpertGameException.class, () -> game.chooseCharacter(0));
    }

    @Test
    void playCharacter() {
        assertThrows(NotExpertGameException.class, () -> game.playCharacter());
    }

    @Test
    void moveFromCloud() {

    }
}
