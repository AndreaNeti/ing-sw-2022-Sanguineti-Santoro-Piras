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
    ArrayList<Player> players2;
    ArrayList<Player> players3;
    ArrayList<Player> players4;
    NormalGame gameWith2;
    NormalGame gameWith4;
    NormalGame gameWith3;

    //create a game with2Players
    public NormalGameTest() {
        t1 = new Team(HouseColor.WHITE, (byte) 1, (byte) 8);
        t2 = new Team(HouseColor.BLACK, (byte) 1, (byte) 8);

        teamList2 = new ArrayList<>(2);
        teamList2.add(t1);
        teamList2.add(t2);
        try {
            p1 = new Player(socket1, t1, Wizard.AIRMAGE, "Franco", 7);
            p2 = new Player(socket2, t2, Wizard.ELECTROMAGE, "Gigi", 7);
        } catch (GameException e) {
            fail();
        }
        players2 = new ArrayList<>(2);
        players2.add(p1);
        players2.add(p2);
        gameWith2 = new NormalGame((byte) 2, teamList2, players2);
        gameWith2.setCurrentPlayer(p1);


        teamList4 = new ArrayList<>(2);
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
        players4 = new ArrayList<>(4);
        players4.add(p1);
        players4.add(p2);

        gameWith4 = new NormalGame((byte) 4, teamList4, players4);
        gameWith4.setCurrentPlayer(p1);

        //create a game with 3 people
        t1 = new Team(HouseColor.WHITE, (byte) 2, (byte) 6);
        t2 = new Team(HouseColor.BLACK, (byte) 2, (byte) 6);
        t3 = new Team(HouseColor.GREY, (byte) 1, (byte) 6);
        teamList3 = new ArrayList<>(3);
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
        players3 = new ArrayList<>(3);
        players3.add(p1);
        players3.add(p2);
        players3.add(p3);

        gameWith3 = new NormalGame((byte) 3, teamList3, players3);
        gameWith3.setCurrentPlayer(p1);
    }


    @Test
    void getTest() {
        assertEquals(gameWith2.getTeams(), teamList2);
        assertEquals(gameWith2.getPlayers(), players2);
        assertEquals(gameWith3.getTeams(), teamList3);
        assertEquals(gameWith3.getPlayers(), players3);
        assertEquals(gameWith4.getTeams(), teamList4);
        assertEquals(gameWith4.getPlayers(), players4);


        assertEquals(gameWith2.getIslands().size(), 12);
        int[] color = {0, 0, 0, 0, 0};
        for (Island i : gameWith2.getIslands()) {
            int index = gameWith2.getIslands().indexOf(i);
            if (index == gameWith2.getMotherNaturePosition() || index == (gameWith2.getMotherNaturePosition() + 6) % 12) {
                assertEquals(i.howManyStudents(), 0);
            } else {
                assertEquals(i.howManyStudents(), 1);
                for (Color c : Color.values()) {
                    if (i.howManyStudents(c) == 1) {
                        if (color[c.ordinal()] == 2)
                            fail();
                        else
                            color[c.ordinal()]++;
                    }
                }
            }
        }
        for(Cloud cloud:gameWith3.getClouds()){
            assertEquals(cloud.howManyStudents(),4);
        }
        for(Cloud cloud:gameWith4.getClouds()){
            assertEquals(cloud.howManyStudents(),3);
        }
        for(Cloud cloud:gameWith2.getClouds()){
            assertEquals(cloud.howManyStudents(),3);
        }

        for(Player p:gameWith2.getPlayers()){
            assertEquals(p.getEntranceHall().howManyStudents(),7);

        }
        for(Player p:gameWith3.getPlayers()){
            assertEquals(p.getEntranceHall().howManyStudents(),9);

        }
        for(Player p:gameWith4.getPlayers()){
            assertEquals(p.getEntranceHall().howManyStudents(),7);

        }


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
