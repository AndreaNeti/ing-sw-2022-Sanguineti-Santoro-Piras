package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.EndGameException;
import it.polimi.ingsw.exceptions.GameException;
import it.polimi.ingsw.exceptions.NotEnoughStudentsException;
import it.polimi.ingsw.exceptions.UnexpectedValueException;
import org.junit.jupiter.api.Test;

import java.net.Socket;
import java.util.ArrayList;
import java.util.Random;

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
            // 14 students are the minimum to get 3 coins after moving them all to the lunch all in the worst case
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
                byte nStudents = (byte) Math.min(10, p.getEntranceHall().howManyStudents(c));
                for (byte i = 0; i < nStudents; i++) {
                    try {
                        // move from entrance to lunch hall
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
            game.setCharacterInput(-5);
            game.setCharacterInput(-1);
            assertThrows(UnexpectedValueException.class, () -> c0.play(game), "not valid inputs");
            // also resets character inputs
            game.setCurrentPlayer(p1);
            // use first available color on the card to test (it's chosen randomly)
            while (((GameComponent) c0).howManyStudents(Color.values()[color]) == 0 && color < Color.values().length) {
                color++;
            }
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
    void playChar1() {
        int color = -1;
        for (int i = 0; i < 5; i++) {
            if (game.getProfessor()[i] != null && p1.getWizard() == game.getProfessor()[i]) {
                color = i;
                break;
            }
        }
        game.setCurrentPlayer(p2);

        while (p2.getLunchHall().howManyStudents(Color.values()[color]) < p1.getLunchHall().howManyStudents(Color.values()[color])) {
            try {
                game.getBag().moveStudents(Color.values()[color], (byte) 1, p2.getLunchHall());
            } catch (GameException e) {
                fail();
            }
        }

        try {
            c1.play(game);
        } catch (GameException | EndGameException e) {
            fail();
        }

        assertEquals(game.getProfessor()[color], p2.getWizard());
    }

    @Test
    void playChar2() {
        try {
            game.chooseCharacter(0);
            game.setCharacterInput(-1);
            assertThrows(UnexpectedValueException.class, () -> c2.play(game), "not valid inputs");
        } catch (GameException e) {
            fail();
        }
        int color = -1;
        Team winnerTeam = null;
        for (int i = 0; i < 5 && color < 0; i++) {
            Wizard profController = game.getProfessor()[i];
            if (profController != null) {
                color = i;
                winnerTeam = game.getPlayers().get(profController.ordinal()).getTeam();
            }
        }
        game.setCurrentPlayer(p1);
        try {
            game.chooseCharacter(0);
            game.getBag().moveStudents(Color.values()[color], (byte) 5, game.getIslands().get(0));
            game.setCharacterInput(0);
        } catch (GameException e) {
            fail();
        }

        try {
            c2.play(game);
        } catch (GameException | EndGameException e) {
            fail();
        }

        assertEquals(game.getIslands().get(0).getTeam(), winnerTeam);
    }

    @Test
    void playChar3() {
        game.setCurrentPlayer(p1);
        try {
            game.playCard((byte) 3);
            game.chooseCharacter(0);
        } catch (GameException | EndGameException e) {
            fail();
        }

        try {
            c3.play(game);
        } catch (GameException | EndGameException e) {
            fail();
        }
        try {
            game.moveMotherNature(4);
        } catch (GameException | EndGameException e) {
            fail();
        }
    }

    @Test
    void playChar4() {
        try {
            game.chooseCharacter(0);
            game.setCharacterInput(-1);
        } catch (GameException e) {
            fail();
        }
        assertThrows(UnexpectedValueException.class, () -> c4.play(game), "not valid inputs");
        game.setCurrentPlayer(p1);

        try {
            game.chooseCharacter(0);
            game.setCharacterInput(2);
        } catch (GameException e) {
            fail();
        }

        try {
            c4.play(game);
        } catch (GameException | EndGameException e) {
            fail();
        }

        assertEquals(game.getIslands().get(2).getProhibitions(), 1);
    }


    @Test
    void playChar6() {
        int color1 = 0, color2 = Color.values().length - 1;
        try {
            game.chooseCharacter(0);
            game.setCharacterInput(-5);
            game.setCharacterInput(-1);
            assertThrows(UnexpectedValueException.class, () -> c6.play(game), "not valid inputs");
            // also resets character inputs
            game.setCurrentPlayer(p1);
            // use first available color on the card to test (it's chosen randomly)
            while (((GameComponent) c6).howManyStudents(Color.values()[color1]) == 0 && color1 < Color.values().length) {
                game.setCurrentPlayer(p1);
                game.chooseCharacter(0);
                game.setCharacterInput(color1);
                game.setCharacterInput(color2);
                assertThrows(NotEnoughStudentsException.class, () -> c6.play(game), "not enough students");
                color1++;
            }
            try {
                game.drawStudents(game.getCurrentPlayer().getEntranceHall(), (byte) game.getCurrentPlayer().getEntranceHall().getMaxStudents());
            } catch (EndGameException e) {
                fail();
            }
            // use last available color on the card to test (it's chosen randomly), more likely to be different from color1
            while (game.getCurrentPlayer().getEntranceHall().howManyStudents(Color.values()[color2]) == 0 && color2 > 0) {
                game.setCurrentPlayer(p1);
                game.chooseCharacter(0);
                game.setCharacterInput(color1);
                game.setCharacterInput(color2);
                assertThrows(NotEnoughStudentsException.class, () -> c6.play(game), "not enough students");
                color2--;
            }
            game.setCurrentPlayer(p1);
            game.chooseCharacter(0);
            game.setCharacterInput(color1);
            game.setCharacterInput(color2);
        } catch (GameException e) {
            fail();
        }
        byte oldC6Color1 = ((GameComponent) c6).howManyStudents(Color.values()[color1]);
        byte oldC6Color2 = ((GameComponent) c6).howManyStudents(Color.values()[color2]);
        byte oldEntranceColor1 = game.getCurrentPlayer().getEntranceHall().howManyStudents(Color.values()[color1]);
        byte oldEntranceColor2 = game.getCurrentPlayer().getEntranceHall().howManyStudents(Color.values()[color2]);
        try {
            c6.play(game);
        } catch (GameException | EndGameException e) {
            fail();
        }
        if (color1 != color2) {
            assertEquals(((GameComponent) c6).howManyStudents(Color.values()[color1]), oldC6Color1 - 1);
            assertEquals(((GameComponent) c6).howManyStudents(Color.values()[color2]), oldC6Color2 + 1);
            assertEquals(game.getCurrentPlayer().getEntranceHall().howManyStudents(Color.values()[color1]), oldEntranceColor1 + 1);
            assertEquals(game.getCurrentPlayer().getEntranceHall().howManyStudents(Color.values()[color2]), oldEntranceColor2 - 1);
        } else {
            assertEquals(((GameComponent) c6).howManyStudents(Color.values()[color1]), oldC6Color1);
            assertEquals(game.getCurrentPlayer().getEntranceHall().howManyStudents(Color.values()[color1]), oldEntranceColor1);
        }
    }

    @Test
    void playChar8() {
        try{
            game.chooseCharacter(0);
            game.setCharacterInput(-1);
            assertThrows(UnexpectedValueException.class, () -> c8.play(game), "not valid inputs");
        } catch (GameException e) {
            fail();
        }
        game.setCurrentPlayer(p1);
        try {
            game.getIslands().get(4).moveAll(game.getIslands().get(1));
            Color color;
            for (int i = 0; i < 3; i++) {
                color = Color.values()[i];
                if (p1.getLunchHall().howManyStudents(color) <= p2.getLunchHall().howManyStudents(color)) {
                    int sum = p2.getLunchHall().howManyStudents(color) - p1.getLunchHall().howManyStudents(color) + 1;
                    game.getBag().moveStudents(color, (byte) sum, p1.getLunchHall());
                }
            }
            for (int i = 3; i < 5; i++) {
                color = Color.values()[i];
                if (p2.getLunchHall().howManyStudents(color) <= p1.getLunchHall().howManyStudents(color)) {
                    int sum = p1.getLunchHall().howManyStudents(color) - p2.getLunchHall().howManyStudents(color) + 1;
                    game.getBag().moveStudents(color, (byte) sum, p2.getLunchHall());
                }
            }
            game.calculateProfessor();
            for (int i = 0; i < 5; i++) {
                color = Color.values()[i];
                game.getBag().moveStudents(color, (byte) (i + 1), game.getIslands().get(4));
            }
        } catch (GameException e) {
            fail();
        }
        Team old = null;
        try {
            game.chooseCharacter(0);
            game.setCharacterInput(4);
            c2.play(game);
            old = game.getIslands().get(4).getTeam();
            game.setCurrentPlayer(p1);
            game.chooseCharacter(0);
            game.setCharacterInput(4);
            c8.play(game);
            game.chooseCharacter(0);
            c2.play(game);
        } catch (GameException | EndGameException e) {
            fail();
        }
        assertNotEquals(game.getIslands().get(4).getTeam(), old);
    }

    @Test
    void playChar9() {
        try {
            game.chooseCharacter(0);
            game.setCharacterInput(-1);
            game.setCharacterInput(-1);
            assertThrows(UnexpectedValueException.class, () -> c9.play(game), "not valid inputs");
            game.setCurrentPlayer(p1);
        } catch (GameException e) {
            fail();
        }
        // remove all red students from p1 lunch hall and tests exception
        try {
            p1.getLunchHall().moveStudents(Color.RED, p1.getLunchHall().howManyStudents(Color.RED), game.getBag());
            game.chooseCharacter(0);
            // first input is lunch hall color
            game.setCharacterInput(Color.RED.ordinal());
            game.setCharacterInput(1);
            assertThrows(NotEnoughStudentsException.class, () -> c9.play(game), "can't swap this, no red students");
            game.setCurrentPlayer(p1);
        } catch (GameException e) {
            fail();
        }
        try {
            // entranceHall has no students, should launch exception
            game.chooseCharacter(0);
            game.setCharacterInput(1);
            game.setCharacterInput(3);
            assertThrows(NotEnoughStudentsException.class, () -> c9.play(game), "can't swap this, no red students");
            game.setCurrentPlayer(p1);
        } catch (GameException e) {
            fail();
        }
        // get two colors that can swap
        int colorLunch = -1;
        for (byte i = 0; i < Color.values().length && colorLunch < 0; i++)
            if (game.getCurrentPlayer().getLunchHall().howManyStudents(Color.values()[i]) > 0) colorLunch = i;
        // entranceHall has a red student
        try {
            game.getBag().moveStudents(Color.RED, (byte) 1, game.getCurrentPlayer().getEntranceHall());
        } catch (GameException e) {
            fail();
        }
        try {
            game.chooseCharacter(0);
            game.setCharacterInput(colorLunch);
            game.setCharacterInput(Color.RED.ordinal());
        } catch (GameException e) {
            fail();
        }
        byte redStudentsLunch = game.getCurrentPlayer().getLunchHall().howManyStudents(Color.RED);
        byte oldValue = game.getCurrentPlayer().getLunchHall().howManyStudents(Color.values()[colorLunch]);
        try {
            c9.play(game);
        } catch (GameException | EndGameException e) {
            fail();
        }
        if (colorLunch != Color.RED.ordinal()) {
            assertEquals(0, game.getCurrentPlayer().getEntranceHall().howManyStudents(Color.RED));
            assertEquals(1, game.getCurrentPlayer().getEntranceHall().howManyStudents(Color.values()[colorLunch]));
            assertEquals(game.getCurrentPlayer().getLunchHall().howManyStudents(Color.RED), redStudentsLunch + 1);
            assertEquals(game.getCurrentPlayer().getLunchHall().howManyStudents(Color.values()[colorLunch]), oldValue - 1);
        } else {
            // swapped same color
            assertEquals(0, game.getCurrentPlayer().getEntranceHall().howManyStudents(Color.RED));
            assertEquals(0, game.getCurrentPlayer().getEntranceHall().howManyStudents(Color.values()[colorLunch]));
            assertEquals(game.getCurrentPlayer().getLunchHall().howManyStudents(Color.RED), redStudentsLunch);
            assertEquals(game.getCurrentPlayer().getLunchHall().howManyStudents(Color.values()[colorLunch]), oldValue);
        }
    }

    @Test
    void playChar10() {
        game.setCurrentPlayer(p1);
        int color = -1;
        int wrongColor = -1;
        for (int i = 0; i < 5 && color == -1; i++) {
            if (((GameComponent) c10).howManyStudents(Color.values()[i]) != 0) {
                color = i;
            }
        }

        for (int i = 0; i < 5 && wrongColor == -1; i++) {
            if (((GameComponent) c10).howManyStudents(Color.values()[i]) == 0) {
                wrongColor = i;
            }
        }

        try {
            game.chooseCharacter(0);
            game.setCharacterInput(wrongColor);

            assertThrows(NotEnoughStudentsException.class, () -> c10.play(game), "Not enough students");

            game.setCurrentPlayer(p1);
            game.chooseCharacter(0);
            game.setCharacterInput(color);
        } catch (GameException e) {
            fail();
        }

        int oldSize = game.getCurrentPlayer().getLunchHall().howManyStudents(Color.values()[color]);
        try {
            c10.play(game);
        } catch (GameException | EndGameException e) {
            fail();
        }
        assertEquals(game.getCurrentPlayer().getLunchHall().howManyStudents(Color.values()[color]), oldSize + 1);
        assertEquals(((GameComponent) c10).howManyStudents(), ((GameComponent) c10).getMaxStudents());
    }

    @Test
    void playChar11() {
        Random rand = new Random();
        int color = rand.nextInt(Color.values().length);
        try {
            game.chooseCharacter(0);
            game.setCharacterInput(-5);
            assertThrows(UnexpectedValueException.class, () -> c11.play(game), "not valid inputs");

            for (Player p : game.getPlayers()) {
                try {
                    game.drawStudents(p.getLunchHall(), (byte) 20);
                } catch (EndGameException e) {
                    fail();
                }
            }
            // also resets character inputs
            game.setCurrentPlayer(p1);
            game.chooseCharacter(0);
            game.setCharacterInput(color);
        } catch (GameException e) {
            fail();
        }
        ArrayList<Player> players = game.getPlayers();
        byte[] oldValues = new byte[players.size()];
        for (byte i = 0; i < players.size(); i++)
            oldValues[i] = players.get(i).getLunchHall().howManyStudents(Color.values()[color]);
        try {
            c11.play(game);
        } catch (GameException | EndGameException e) {
            fail();
        }
        for (byte i = 0; i < players.size(); i++) {
            if (oldValues[i] < 3) assertEquals(0, players.get(i).getLunchHall().howManyStudents(Color.values()[color]));
            else assertEquals(oldValues[i] - 3, players.get(i).getLunchHall().howManyStudents(Color.values()[color]));
        }
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