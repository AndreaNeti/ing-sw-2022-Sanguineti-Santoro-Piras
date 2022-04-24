package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.EndGameException;
import it.polimi.ingsw.exceptions.GameException;
import it.polimi.ingsw.exceptions.NotExpertGameException;
import org.junit.Before;
import org.junit.jupiter.api.Test;

import java.net.Socket;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class ExpertGameTest {
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
    ExpertGame gameWith2;
    ExpertGame gameWith4;
    ExpertGame gameWith3;

    //constructor of expert game
    public ExpertGameTest() {
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
        gameWith2 = new ExpertGame((byte) 2, teamList2, players2);
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

        gameWith4 = new ExpertGame((byte) 4, teamList4, players4);
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

        gameWith3 = new ExpertGame((byte) 3, teamList3, players3);
        gameWith3.setCurrentPlayer(p1);
    }
    
    // constructor test
    @Test
    void contructorTest() {
        assertEquals(gameWith2.getTeams(), teamList2);
        assertEquals(gameWith2.getPlayers(), players2);
        assertEquals(gameWith3.getTeams(), teamList3);
        assertEquals(gameWith3.getPlayers(), players3);
        assertEquals(gameWith4.getTeams(), teamList4);
        assertEquals(gameWith4.getPlayers(), players4);


    }
}
