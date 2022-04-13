package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.EndGameException;
import it.polimi.ingsw.exceptions.GameException;
import it.polimi.ingsw.exceptions.NotExpertGameException;
import org.junit.Before;
import org.junit.jupiter.api.Test;

import java.net.Socket;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.*;

public class NormalGameTest {
    Socket socket1 = new Socket(), socket2 = new Socket();
    Team t1;
    Team t2;
    Team t3;
    ArrayList<Team> teamList2;
    ArrayList<Team> teamList3;
    ArrayList<Team> teamList4;
    Player p1_2, p2_2,p1_3,p2_3,p3_3,p1_4,p2_4,p3_4, p4_4;
    ArrayList<Player> players2;
    ArrayList<Player> players3;
    ArrayList<Player> players4;
    NormalGame gameWith2;
    NormalGame gameWith4;
    NormalGame gameWith3;

    //constructor of game
    public NormalGameTest() {
        t1 = new Team(HouseColor.WHITE, (byte) 1, (byte) 8);
        t2 = new Team(HouseColor.BLACK, (byte) 1, (byte) 8);

        teamList2 = new ArrayList<>(2);
        teamList2.add(t1);
        teamList2.add(t2);
        try {
            p1_2 = new Player(socket1, t1, Wizard.AIRMAGE, "Franco", 7);
            p2_2 = new Player(socket2, t2, Wizard.ELECTROMAGE, "Gigi", 7);
        } catch (GameException e) {
            fail();
        }
        players2 = new ArrayList<>(2);
        players2.add(p1_2);
        players2.add(p2_2);
        gameWith2 = new NormalGame((byte) 2, teamList2, players2);
        gameWith2.setCurrentPlayer(p1_2);


        teamList4 = new ArrayList<>(2);
        t1 = new Team(HouseColor.WHITE, (byte) 2, (byte) 8);
        t2 = new Team(HouseColor.BLACK, (byte) 2, (byte) 8);
        teamList4.add(t1);

        teamList4.add(t2);
        try {
            p1_4 = new Player(socket1, t1, Wizard.AIRMAGE, "Franco", 7);
            p2_4 = new Player(socket2, t2, Wizard.ELECTROMAGE, "Gigi", 7);
            p3_4 = new Player(socket1, t1, Wizard.SANDMAGE, "Luigi", 7);
            p4_4 = new Player(socket1, t2, Wizard.WOODMAGE, "Filomena", 7);

        } catch (GameException e) {
            fail();
        }
        players4 = new ArrayList<>(4);
        players4.add(p1_4);
        players4.add(p2_4);

        gameWith4 = new NormalGame((byte) 4, teamList4, players4);
        gameWith4.setCurrentPlayer(p1_4);

        //create a game with 3 people
        t1 = new Team(HouseColor.WHITE, (byte) 2, (byte) 6);
        t2 = new Team(HouseColor.BLACK, (byte) 2, (byte) 6);
        t3 = new Team(HouseColor.GREY, (byte) 1, (byte) 6);
        teamList3 = new ArrayList<>(3);
        teamList3.add(t1);
        teamList3.add(t2);
        teamList3.add(t3);
        try {
            p1_3 = new Player(socket1, t1, Wizard.AIRMAGE, "Franco", 9);
            p2_3 = new Player(socket2, t2, Wizard.ELECTROMAGE, "Gigi", 9);
            p3_3 = new Player(socket1, t1, Wizard.SANDMAGE, "Luigi", 9);


        } catch (GameException e) {
            fail();
        }
        players3 = new ArrayList<>(3);
        players3.add(p1_3);
        players3.add(p2_3);
        players3.add(p3_3);

        gameWith3 = new NormalGame((byte) 3, teamList3, players3);
        gameWith3.setCurrentPlayer(p1_3);

    }


    @Test
    void constructorTest() {
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
        assertEquals(gameWith2.getCurrentPlayer(), p1_2);
        for (Wizard p : gameWith2.getProfessor())
            assertNull(p);
    }


    @Test
    void refillCloudTest(){
        GameComponent islandTest=new Island();
        for (Cloud cloud: gameWith2.getClouds()) {
            try{
                cloud.moveAll(islandTest);
            }catch (GameException ignored){
                fail();
            }
        }
        try{
            gameWith2.refillClouds();
        }catch (EndGameException ex){
            fail();
        }

        for (Cloud cloud: gameWith2.getClouds()) {

            assertEquals(cloud.howManyStudents(),3);
        }
    }

    @Test
    void playCardTest() {
        assertEquals(gameWith2.getCurrentPlayer(),p1_2);
        try {
            gameWith2.playCard((byte) 5);
        } catch (GameException | EndGameException e) {
            fail();
        }
        assertEquals(5, p1_2.getPlayedCard());
        assertEquals(3, p1_2.getPlayedCardMoves());
        gameWith2.setCurrentPlayer(p2_2);
        try {
            gameWith2.playCard((byte) 6);
        } catch (GameException | EndGameException e) {
            fail();
        }
        assertEquals(6, p2_2.getPlayedCard());
        assertEquals(3, p2_2.getPlayedCardMoves());
        assertTrue(p1_2.compare(p2_2, p1_2) > 0);
    }

    @Test
    void calculateProfessorTest(){
        //at the beginning after the calculation no one should have professor
        gameWith2.calculateProfessor();
        for (Wizard w: gameWith2.getProfessor()) {
            assertNull(w);
        }
        gameWith3.calculateProfessor();
        for (Wizard w: gameWith3.getProfessor()) {
            assertNull(w);
        }
        gameWith4.calculateProfessor();
        for (Wizard w: gameWith3.getProfessor()) {
            assertNull(w);
        }
        try {
            gameWith4.getBag().moveStudents(Color.BLUE, (byte) 5, gameWith4.getCurrentPlayer().getLunchHall());
        }catch(GameException ex){
            fail();
        }
        gameWith4.calculateProfessor();
        assertEquals(gameWith4.getProfessor()[1],gameWith4.getCurrentPlayer().getWizard());

        try {
            gameWith3.getBag().moveStudents(Color.BLUE, (byte) 5, p3_3.getLunchHall());
        }catch(GameException ex){
            fail();
        }
        gameWith3.calculateProfessor();
        assertEquals(p3_3.getWizard(),gameWith3.getProfessor()[1]);
        try {
            gameWith3.getBag().moveStudents(Color.RED, (byte) 5, p1_3.getLunchHall());
            gameWith3.getBag().moveStudents(Color.RED, (byte) 5, p2_3.getLunchHall());
        }catch(GameException ex){
            fail();
        }
        gameWith3.calculateProfessor();
        assertEquals(gameWith3.getProfessor()[0],p1_3.getWizard());

        try {
            gameWith3.getBag().moveStudents(Color.BLUE, (byte) 6, p2_3.getLunchHall());
        }catch(GameException ex){
            fail();
        }
        gameWith3.calculateProfessor();
        assertEquals(p2_3.getWizard(),gameWith3.getProfessor()[1]);
    }
    @Test
    void moveTest(){

    }
    @Test

    void moveMotherNatureTest(){
        //now it's the beginning of a game

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
