package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.EndGameException;
import it.polimi.ingsw.exceptions.GameException;
import it.polimi.ingsw.exceptions.NotExpertGameException;
import org.junit.Before;
import org.junit.jupiter.api.Test;

import java.net.Socket;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class NormalGameTest {
    Socket socket1 = new Socket(), socket2 = new Socket();
    Team t1;
    Team t2;
    Team t3;
    ArrayList<Team> teamList2;
    ArrayList<Team> teamList3;
    ArrayList<Team> teamList4;
    Player p1, p2, p3, p4;
    ArrayList<Player> players;
    NormalGame gameWith2;
    NormalGame gameWith4;
    NormalGame gameWith3;

    //create a game with2Players
    public NormalGameTest() {
        t1 = new Team(HouseColor.WHITE, (byte) 1, (byte) 8);
        t2 = new Team(HouseColor.BLACK, (byte) 1, (byte) 8);

        teamList2=new ArrayList<>(2);
        teamList2.add(t1);
        teamList2.add(t2);
        try {
            p1 = new Player(socket1, t1, Wizard.AIRMAGE, "Franco", 7);
            p2 = new Player(socket2, t2, Wizard.ELECTROMAGE, "Gigi", 7);
        } catch (GameException e) {
            fail();
        }
        players=new ArrayList<>(2);
        players.add(p1);
        players.add(p2);
        gameWith2 = new NormalGame((byte) 2, teamList2, players);
        gameWith2.setCurrentPlayer(p1);



        teamList4=new ArrayList<>(2);
        t1 = new Team(HouseColor.WHITE, (byte) 2, (byte) 8);
        t2 = new Team(HouseColor.BLACK, (byte) 2, (byte) 8);
        teamList4.add(t1);

        teamList4.add(t2);
        try {
            p1 = new Player(socket1, t1, Wizard.AIRMAGE, "Franco", 7);
            p2 = new Player(socket2, t2, Wizard.ELECTROMAGE, "Gigi", 7);
            p3 = new Player(socket1, t1, Wizard.SANDMAGE, "Luigi", 7);
            p4 = new Player(socket1, t2, Wizard.WOODMAGE, "Filomena", 7);

        } catch (GameException e) {
            fail();
        }
        players=new ArrayList<>(4);
        players.add(p1);
        players.add(p2);

        gameWith4 = new NormalGame((byte) 4,teamList4,players );
        gameWith4.setCurrentPlayer(p1);

        //create a game with 3 people
        t1 = new Team(HouseColor.WHITE, (byte) 2, (byte) 6);
        t2 = new Team(HouseColor.BLACK, (byte) 2, (byte) 6);
        t3=new Team(HouseColor.GREY,(byte) 1,(byte) 6);
        teamList3=new ArrayList<>(3);
        teamList3.add(t1);
        teamList3.add(t2);
        teamList3.add(t3);
        try {
            p1 = new Player(socket1, t1, Wizard.AIRMAGE, "Franco", 9);
            p2 = new Player(socket2, t2, Wizard.ELECTROMAGE, "Gigi", 9);
            p3 = new Player(socket1, t1, Wizard.SANDMAGE, "Luigi", 9);


        } catch (GameException e) {
            fail();
        }
        players=new ArrayList<>(3);
        players.add(p1);
        players.add(p2);
        players.add(p3);

        gameWith3 = new NormalGame((byte) 3,teamList3,players );
        gameWith3.setCurrentPlayer(p1);
    }


    @Test
    void getTest() {
        assertEquals(gameWith2.getTeams(), teamList2);
        //assertEquals(gameWith2.getPlayers(), players);
        assertEquals(gameWith2.getIslands().size(), 12);
        assertEquals(gameWith2.getBag().getClass(), Bag.class);
        assertEquals(gameWith2.getCurrentPlayer(), p1);
        for (Wizard p : gameWith2.getProfessor())
            assertNull(p);
    }


    @Test
    void move() {
    }

    @Test
    void playCard() {
        gameWith2.setCurrentPlayer(p1);
        try {
            gameWith2.playCard((byte) 5);
        } catch (GameException | EndGameException e) {
            fail();
        }
        assertEquals(5, p1.getPlayedCard());
        assertEquals(3, p1.getPlayedCardMoves());
        gameWith2.setCurrentPlayer(p2);
        try {
            gameWith2.playCard((byte) 6);
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
        assertThrows(NotExpertGameException.class, () -> gameWith2.setCharacterInput(0));
    }

    @Test
    void chooseCharacter() {
        assertThrows(NotExpertGameException.class, () -> gameWith2.chooseCharacter(0));
    }

    @Test
    void playCharacter() {
        assertThrows(NotExpertGameException.class, () -> gameWith2.playCharacter());
    }

    @Test
    void moveFromCloud() {

    }
}
