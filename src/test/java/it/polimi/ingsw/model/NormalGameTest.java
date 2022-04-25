package it.polimi.ingsw.model;

import it.polimi.ingsw.controller.PlayerHandler;
import it.polimi.ingsw.exceptions.EndGameException;
import it.polimi.ingsw.exceptions.GameException;
import it.polimi.ingsw.exceptions.NotAllowedException;
import it.polimi.ingsw.exceptions.NotExpertGameException;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class NormalGameTest {
    Team t1, t2, t3;
    ArrayList<Team> teamList2, teamList3, teamList4;
    Player p1_2, p2_2, p1_3, p2_3, p3_3, p1_4, p2_4, p3_4, p4_4;
    ArrayList<Player> players2, players3, players4;
    NormalGame gameWith2, gameWith3, gameWith4;

    //constructor of game
    public NormalGameTest() {
        t1 = new Team(HouseColor.WHITE, (byte) 1, (byte) 8);
        t2 = new Team(HouseColor.BLACK, (byte) 1, (byte) 8);

        teamList2 = new ArrayList<>(2);
        teamList2.add(t1);
        teamList2.add(t2);
        try {
            p1_2 = new Player("Franco", t1, Wizard.WOODMAGE, 7);
            p2_2 = new Player("Gigi", t2, Wizard.SANDMAGE, 7);
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
            p1_4 = new Player("Franco", t1, Wizard.WOODMAGE, 7);
            p2_4 = new Player("Gigi", t2, Wizard.SANDMAGE, 7);
            p3_4 = new Player("Carola", t1, Wizard.AIRMAGE, 7);
            p4_4 = new Player("Filomena", t2, Wizard.ELECTROMAGE, 7);

        } catch (GameException e) {
            fail();
        }
        players4 = new ArrayList<>(4);
        players4.add(p1_4);
        players4.add(p2_4);
        players4.add(p3_4);
        players4.add(p4_4);
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
            p1_3 = new Player("Franco", t1, Wizard.WOODMAGE, 9);
            p2_3 = new Player("Gigi", t2, Wizard.SANDMAGE, 9);
            p3_3 = new Player("Carola", t3, Wizard.AIRMAGE, 9);


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
                        if (color[c.ordinal()] == 2) fail();
                        else color[c.ordinal()]++;
                    }
                }
            }
        }
        for (Cloud cloud : gameWith3.getClouds()) {
            assertEquals(cloud.howManyStudents(), 4);
        }
        for (Cloud cloud : gameWith4.getClouds()) {
            assertEquals(cloud.howManyStudents(), 3);
        }
        for (Cloud cloud : gameWith2.getClouds()) {
            assertEquals(cloud.howManyStudents(), 3);
        }

        for (Player p : gameWith2.getPlayers()) {
            assertEquals(p.getEntranceHall().howManyStudents(), 7);

        }
        for (Player p : gameWith3.getPlayers()) {
            assertEquals(p.getEntranceHall().howManyStudents(), 9);

        }
        for (Player p : gameWith4.getPlayers()) {
            assertEquals(p.getEntranceHall().howManyStudents(), 7);

        }

        assertEquals(gameWith2.getBag().getClass(), Bag.class);
        assertEquals(gameWith2.getCurrentPlayer(), p1_2);
        for (Wizard p : gameWith2.getProfessor())
            assertNull(p);
    }


    @Test
    void refillCloudTest() {
        GameComponent islandTest = new Island();
        for (Cloud cloud : gameWith2.getClouds()) {
            try {
                cloud.moveAll(islandTest);
            } catch (GameException ignored) {
                fail();
            }
        }
        try {
            gameWith2.refillClouds();
        } catch (EndGameException ex) {
            fail();
        }

        for (Cloud cloud : gameWith2.getClouds()) {

            assertEquals(cloud.howManyStudents(), 3);
        }
    }

    @Test
    void playCardTest() {
        assertEquals(gameWith2.getCurrentPlayer(), p1_2);
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
    }

    @Test
    void calculateProfessorTest() {
        //at the beginning after the calculation no one should have professor
        gameWith2.calculateProfessor();
        for (Wizard w : gameWith2.getProfessor()) {
            assertNull(w);
        }
        gameWith3.calculateProfessor();
        for (Wizard w : gameWith3.getProfessor()) {
            assertNull(w);
        }
        gameWith4.calculateProfessor();
        for (Wizard w : gameWith3.getProfessor()) {
            assertNull(w);
        }
        try {
            gameWith4.getBag().moveStudents(Color.BLUE, (byte) 5, gameWith4.getCurrentPlayer().getLunchHall());
        } catch (GameException ex) {
            fail();
        }
        gameWith4.calculateProfessor();
        assertEquals(gameWith4.getProfessor()[1], gameWith4.getCurrentPlayer().getWizard());

        try {
            gameWith3.getBag().moveStudents(Color.BLUE, (byte) 5, p3_3.getLunchHall());
        } catch (GameException ex) {
            fail();
        }
        gameWith3.calculateProfessor();
        assertEquals(p3_3.getWizard(), gameWith3.getProfessor()[1]);
        try {
            gameWith3.getBag().moveStudents(Color.RED, (byte) 5, p1_3.getLunchHall());
            gameWith3.getBag().moveStudents(Color.RED, (byte) 5, p2_3.getLunchHall());
        } catch (GameException ex) {
            fail();
        }
        gameWith3.calculateProfessor();
        assertEquals(gameWith3.getProfessor()[0], p1_3.getWizard());

        try {
            gameWith3.getBag().moveStudents(Color.BLUE, (byte) 6, p2_3.getLunchHall());
        } catch (GameException ex) {
            fail();
        }
        gameWith3.calculateProfessor();
        assertEquals(p2_3.getWizard(), gameWith3.getProfessor()[1]);
    }

    @Test
    void moveTest() {
        assertThrows(NotAllowedException.class, () -> gameWith2.move(Color.RED, -5, -6), "" + "Wrong Index should catch an error");
        //move from entrance to island with index 2
        for (Color c : Color.values()) {
            if (gameWith2.getCurrentPlayer().getEntranceHall().howManyStudents(c) > 0) {
                byte studentsBefore = gameWith2.getIslands().get(2).howManyStudents(c);
                try {
                    gameWith2.move(c, 0, 4);
                } catch (GameException e) {
                    fail();
                }
                assertEquals(studentsBefore + 1, gameWith2.getIslands().get(2).howManyStudents(c));
            }
        }
    }

    @Test
    void moveMotherNatureTest() {
        Bag bagTest=new Bag((byte)20);
        //svuoto l'isola 0,1,2,3 così posso metterci gli studenti che voglio
        try {
            gameWith4.getIslands().get(0).moveAll(gameWith3.getIslands().get(4));
            gameWith4.getIslands().get(1).moveAll(gameWith3.getIslands().get(4));
            gameWith4.getIslands().get(2).moveAll(gameWith3.getIslands().get(4));
            gameWith4.getIslands().get(3).moveAll(gameWith3.getIslands().get(4));
            gameWith4.getIslands().get(7).moveAll(gameWith3.getIslands().get(4));
        } catch (NotAllowedException e) {
            fail();
        }
        gameWith4.setCurrentPlayer(p1_3);
        try {
            gameWith4.setCurrentPlayer(p1_4);
            gameWith4.playCard((byte) 8);
            assertThrows(NotAllowedException.class, ()->gameWith4.moveMotherNature((byte)10),"");
            gameWith4.moveMotherNature(4);
            gameWith4.setCurrentPlayer(p2_4);
            gameWith4.playCard((byte) 8);
            gameWith4.moveMotherNature(1);
            gameWith4.setCurrentPlayer(p3_4);
            gameWith4.playCard((byte) 6);
            gameWith4.moveMotherNature(3);
            gameWith4.setCurrentPlayer(p4_4);
            gameWith4.playCard((byte) 8);
            gameWith4.moveMotherNature(4);
        } catch (GameException | EndGameException e) {
            fail();
        }

        assertEquals(gameWith4.getIslands().size(), 12);

        //return to player 1 which now move students on 3 island (Played cards remain the same)
        gameWith4.setCurrentPlayer(p1_4);
        //assign to p1_4 professor red
        try {
            bagTest.moveStudents(Color.RED, (byte) (10-p1_4.getLunchHall().howManyStudents(Color.RED)), p1_4.getLunchHall());
            gameWith4.calculateProfessor();
        //put on the island 1 some red students so the first player win
            bagTest.moveStudents(Color.RED, (byte) (2), gameWith4.getIslands().get(1));
        } catch (GameException e) {
            fail();
        }
        int movesToReach1=Math.floorMod(1-(gameWith4.getMotherNaturePosition()),gameWith4.getIslands().size());
        assertEquals(gameWith4.getMotherNaturePosition(),0);

        try {
            gameWith4.playCard((byte)7);
            gameWith4.moveMotherNature(1);
        } catch (GameException | EndGameException e) {
            fail();
        }
        assertEquals(gameWith4.getIslands().get(1).getTeamColor(),p1_4.getTeam().getHouseColor());
        //let's try to merge island 2 and 1 by putting blue student on island 2
        //and the other player of the team has professor
        try {
            gameWith4.setCurrentPlayer(p3_4);
            bagTest.moveStudents(Color.BLUE, (byte) (10-p3_4.getLunchHall().howManyStudents(Color.RED)), p3_4.getLunchHall());
            gameWith4.calculateProfessor();
            bagTest.moveStudents(Color.BLUE, (byte) (2), gameWith4.getIslands().get(2));
            gameWith4.playCard((byte) 1);
            gameWith4.moveMotherNature(1);
        } catch (GameException | EndGameException e) {
            fail();
        }
        assertEquals(gameWith4.getIslands().size(),11);
        assertEquals(gameWith4.getIslands().get(1).getTeamColor(), p3_4.getTeam().getHouseColor());
        //now blackTeam will have Green professor p2_4 and YellowProfessor p4_4
        try {
            bagTest.moveStudents(Color.GREEN, (byte) (10-p2_4.getLunchHall().howManyStudents(Color.GREEN)), p2_4.getLunchHall());
            bagTest.moveStudents(Color.YELLOW, (byte) (10-p4_4.getLunchHall().howManyStudents(Color.GREEN)), p4_4.getLunchHall());
            gameWith4.calculateProfessor();
            //put on the island 3(which is now 2 after the merge) some green students so the second player win
            bagTest.moveStudents(Color.GREEN, (byte) (2), gameWith4.getIslands().get(2));
            gameWith4.moveMotherNature(1);
        } catch (GameException |EndGameException ex) {
            fail();
        }
        assertEquals(gameWith4.getIslands().get(2).getTeamColor(),p2_4.getTeam().getHouseColor());
        //return to the island 1 and let black Team win
        try{
            gameWith4.setCurrentPlayer(p1_4);
            gameWith4.playCard((byte) 10);


            gameWith4.moveMotherNature(5);
            bagTest.moveStudents(Color.GREEN, (byte) (4), gameWith4.getIslands().get(1));
            bagTest.moveStudents(Color.YELLOW, (byte) (3), gameWith4.getIslands().get(1));
            gameWith4.moveMotherNature(5);

        }catch (GameException|EndGameException ex){
            fail();
        }
        assertEquals(gameWith4.getIslands().size(),10);
        assertEquals(gameWith4.getIslands().get(1).getTeamColor(),p2_4.getTeam().getHouseColor());

    }

    @Test
    void calculateWinnerTest() {
        //you can win by having the maximum number of tower used
        //or in case of parity maximum number of professor

    }

    @Test
    void moveFromCloudsTest() {
        assertThrows(NotAllowedException.class, () -> gameWith4.moveFromCloud(-5));
        assertEquals(gameWith4.getCurrentPlayer().getEntranceHall().howManyStudents(), 7);

        for (int i = 0; i < 3; i++) {
            for (Color c : Color.values())
                try {

                    gameWith4.move(c, 0, 3);
                    break;
                } catch (GameException ignored) {
                }
        }

        try {
            gameWith4.moveFromCloud(-4);
        } catch (NotAllowedException e) {
            fail();
        }
        assertEquals(gameWith4.getClouds().get(3).howManyStudents(), 0);
        assertEquals(gameWith4.getCurrentPlayer().getEntranceHall().howManyStudents(), 7);

    }


    @Test
    void setCharacterInput() {
        assertThrows(NotExpertGameException.class, () -> gameWith2.setCharacterInput(0));
    }

    @Test
    void chooseCharacter() {
        assertThrows(NotExpertGameException.class, () -> gameWith2.chooseCharacter((byte) 0));
    }

    @Test
    void playCharacter() {
        assertThrows(NotExpertGameException.class, () -> gameWith2.playCharacter());
    }

    @Test
    void moveFromCloud() {

    }
}
