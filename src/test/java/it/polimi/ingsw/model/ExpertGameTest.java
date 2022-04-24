package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.GameException;
import it.polimi.ingsw.exceptions.NotAllowedException;
import it.polimi.ingsw.exceptions.UnexpectedValueException;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class ExpertGameTest {
    Team t1, t2, t3;
    ArrayList<Team> teamList2, teamList3, teamList4;
    Player p1, p2, p3, p4;
    ArrayList<Player> players2, players3, players4;
    ExpertGame gameWith2, gameWith3, gameWith4;

    //constructor of expert game
    public ExpertGameTest() {
        t1 = new Team(HouseColor.WHITE, (byte) 1, (byte) 8);
        t2 = new Team(HouseColor.BLACK, (byte) 1, (byte) 8);

        teamList2 = new ArrayList<>(2);
        teamList2.add(t1);
        teamList2.add(t2);
        try {
            p1 = new Player("Franco", t1, Wizard.WOODMAGE, 7);
            p2 = new Player("Gigi", t2, Wizard.SANDMAGE, 7);
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
            p1 = new Player("Franco", t1, Wizard.WOODMAGE, 7);
            p2 = new Player("Gigi", t2, Wizard.SANDMAGE, 7);
            p3 = new Player("Carola", t1, Wizard.AIRMAGE, 7);
            p4 = new Player("Filomena", t2, Wizard.ELECTROMAGE, 7);

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
            p1 = new Player("Franco", t1, Wizard.WOODMAGE, 9);
            p2 = new Player("Gigi", t2, Wizard.SANDMAGE, 9);
            p3 = new Player("Carola", t1, Wizard.AIRMAGE, 9);


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
    void constructorTest() {
        assertEquals(gameWith2.getTeams(), teamList2);
        assertEquals(gameWith2.getPlayers(), players2);
        assertEquals(gameWith3.getTeams(), teamList3);
        assertEquals(gameWith3.getPlayers(), players3);
        assertEquals(gameWith4.getTeams(), teamList4);
        assertEquals(gameWith4.getPlayers(), players4);
    }

    @Test
    void coinsTest() {
        gameWith2.setCurrentPlayer(p1);
        try {
            for (Color color : Color.values()) {
                p1.getEntranceHall().moveStudents(color, p1.getEntranceHall().howManyStudents(color), gameWith2.getBag());
            }
            gameWith2.getBag().moveStudents(Color.RED, (byte) 9, p1.getEntranceHall());
        } catch (GameException e) {
            fail();
        }
        try {
            byte coins = 1;
            assertEquals(gameWith2.getCoinsPlayer(p1), coins);
            gameWith2.move(Color.RED, 0, 1);
            assertEquals(gameWith2.getCoinsPlayer(p1), coins);
            gameWith2.move(Color.RED, 0, 1);
            assertEquals(gameWith2.getCoinsPlayer(p1), coins);
            gameWith2.move(Color.RED, 0, 1);
            coins += 1;
            assertEquals(gameWith2.getCoinsPlayer(p1), coins);
            gameWith2.move(Color.RED, 0, 1);
            gameWith2.move(Color.RED, 0, 1);
            gameWith2.move(Color.RED, 0, 1);
            coins += 1;
            assertEquals(gameWith2.getCoinsPlayer(p1), coins);
            gameWith2.move(Color.RED, 0, 1);
            gameWith2.move(Color.RED, 0, 1);
            gameWith2.move(Color.RED, 0, 1);
            coins += 1;
            assertEquals(gameWith2.getCoinsPlayer(p1), coins);
        } catch (GameException e) {
            fail();
        }
    }

    @Test
    void prohibitionsTest() {
        try {
            for (int i = 0; i < 4; i++) {
                gameWith2.setProhibition(i);
            }
        } catch (GameException e) {
            fail();
        }
        assertThrows(NotAllowedException.class, () -> gameWith2.setProhibition(0), "No more prohibitions");
    }

    @Test
    void chooseCharacterTest() {
        assertThrows(UnexpectedValueException.class, () -> gameWith2.chooseCharacter((byte) -1));
        assertThrows(UnexpectedValueException.class, () -> gameWith2.chooseCharacter((byte) 3));
    }

    @Test
    void characterInputsTest() {
        assertThrows(NotAllowedException.class, () -> gameWith2.setCharacterInput(0), "There is no played character card");
        ArrayList<Integer>  inputs = new ArrayList<>();
        gameWith2.setCurrentPlayer(p1);
        try {
            for (Color color : Color.values()) {
                p1.getEntranceHall().moveStudents(color, p1.getEntranceHall().howManyStudents(color), gameWith2.getBag());
            }
            gameWith2.getBag().moveStudents(Color.RED, (byte) 9, p1.getEntranceHall());
            for (int i = 0; i < 9; i++) {
                gameWith2.move(Color.RED, 0, 1);
            }
        } catch (GameException e) {
            fail();
        }
        try {
            gameWith2.chooseCharacter((byte) 0);
            for (int i = 0; i < 5; i++) {
                gameWith2.setCharacterInput(i);
                inputs.add(i);
            }
        } catch (GameException e) {
            fail();
        }

        assertEquals(gameWith2.getCharacterInputs(), inputs);
    }

    @Test
    void playCharacterTest() {

    }
}
