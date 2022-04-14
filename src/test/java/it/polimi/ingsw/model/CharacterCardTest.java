package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.EndGameException;
import it.polimi.ingsw.exceptions.GameException;
import it.polimi.ingsw.exceptions.UnexpectedValueException;
import org.junit.jupiter.api.Test;

import java.net.Socket;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class CharacterCardTest {
    Team t1, t2;
    Player p1, p2;
    ArrayList<Team> teamList = new ArrayList<>();
    ArrayList<Player> playerList = new ArrayList<>();
    ExpertGame game;
    CharacterCard c0, c1, c2, c3, c4, c5, c6, c7, c8, c9, c10, c11;

    public CharacterCardTest() {
        t1 = new Team(HouseColor.WHITE, (byte) 1, (byte) 8);
        t2 = new Team(HouseColor.BLACK, (byte) 1, (byte) 8);
        try {
            p1 = new Player(new Socket(), t1, Wizard.WOODMAGE, "Franco", 14);
            p2 = new Player(new Socket(), t2, Wizard.SANDMAGE, "Carola", 14);
        } catch (GameException e) {
            fail();
        }
        teamList.add(t1);
        teamList.add(t2);
        playerList.add(p1);
        playerList.add(p2);
        game = new ExpertGame((byte) 2, teamList, playerList);
        // fill lunch halls, players will gain enough coins to test char.play methods
        for (Color c : Color.values()) {
            for (Player p : playerList) {
                game.setCurrentPlayer(p);
                byte nStudents = p.getEntranceHall().howManyStudents(c);
                for (byte i = 0; i < nStudents; i++) {
                    try {
                        game.move(c, 0, 1);
                    } catch (GameException e) {
                        fail();
                    }
                }
            }
        }
        game.setCurrentPlayer(p1);
        c0 = new Char0();
        try {
            game.drawStudents((GameComponent) c0, (byte) ((GameComponent) c0).getMaxStudents());
        } catch (EndGameException e) {
            fail();
        }
        c1 = new Char1();
        c2 = new Char2();
        c3 = new Char3();
        c4 = new Char4();
        c5 = new Char5();
        c6 = new Char6();
        try {
            game.drawStudents((GameComponent) c6, (byte) ((GameComponent) c6).getMaxStudents());
        } catch (EndGameException e) {
            fail();
        }
        c7 = new Char7();
        c8 = new Char8();
        c9 = new Char9();
        c10 = new Char10();
        try {
            game.drawStudents((GameComponent) c10, (byte) ((GameComponent) c10).getMaxStudents());
        } catch (EndGameException e) {
            fail();
        }
        c11 = new Char11();
    }

    @Test
    void playChar0() {
        int color = 0;
        int islandId = 0;
        try {
            game.chooseCharacter(0);
            // use first available color on the card to test (it's chosen randomly)
            while (((GameComponent) c0).howManyStudents(Color.values()[color]) == 0 && color < Color.values().length) {
                color++;
            }
            game.setCharacterInput(-5);
            game.setCharacterInput(-1);
            assertThrows(UnexpectedValueException.class, () -> c0.play(game), "not valid inputs");
            // also resets character inputs
            game.setCurrentPlayer(p1);
            game.chooseCharacter(0);
            game.setCharacterInput(color);
            game.setCharacterInput(0);
        } catch (GameException e) {
            fail();
        }
        byte old = game.getIslands().get(islandId).howManyStudents(Color.values()[color]);
        try {
            c0.play(game);
        } catch (GameException | EndGameException e) {
            fail();
        }
        assertEquals(game.getIslands().get(islandId).howManyStudents(Color.values()[color]), old + 1);
        assertEquals(((GameComponent) c0).howManyStudents(), ((GameComponent) c0).getMaxStudents());
    }

    @Test
    void getCost() {
        assertEquals(c0.getCost(), 1);
        assertEquals(c1.getCost(), 2);
        assertEquals(c2.getCost(), 3);
        assertEquals(c3.getCost(), 1);
        assertEquals(c4.getCost(), 2);
        assertEquals(c5.getCost(), 3);
        assertEquals(c6.getCost(), 1);
        assertEquals(c7.getCost(), 2);
        assertEquals(c8.getCost(), 3);
        assertEquals(c9.getCost(), 1);
        assertEquals(c10.getCost(), 2);
        assertEquals(c11.getCost(), 3);
    }

    @Test
    void getId() {
        assertEquals(c0.getId(), 0);
        assertEquals(c1.getId(), 1);
        assertEquals(c2.getId(), 2);
        assertEquals(c3.getId(), 3);
        assertEquals(c4.getId(), 4);
        assertEquals(c5.getId(), 5);
        assertEquals(c6.getId(), 6);
        assertEquals(c7.getId(), 7);
        assertEquals(c8.getId(), 8);
        assertEquals(c9.getId(), 9);
        assertEquals(c10.getId(), 10);
        assertEquals(c11.getId(), 11);
    }

    @Test
    void canPlay() {
        assertTrue(c0.canPlay(2));
        assertTrue(c1.canPlay(0));
        assertTrue(c2.canPlay(1));
        assertTrue(c3.canPlay(0));
        assertTrue(c4.canPlay(1));
        assertTrue(c5.canPlay(0));
        assertTrue(c6.canPlay(2));
        assertTrue(c6.canPlay(4));
        assertTrue(c6.canPlay(6));
        assertTrue(c7.canPlay(0));
        assertTrue(c8.canPlay(1));
        assertTrue(c9.canPlay(2));
        assertTrue(c9.canPlay(4));
        assertTrue(c10.canPlay(1));
        assertTrue(c11.canPlay(1));
    }
}