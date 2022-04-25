package it.polimi.ingsw.model;

import it.polimi.ingsw.controller.PlayerHandler;
import it.polimi.ingsw.exceptions.EndGameException;
import it.polimi.ingsw.exceptions.GameException;
import it.polimi.ingsw.exceptions.NotAllowedException;
import it.polimi.ingsw.exceptions.UnexpectedValueException;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class ExpertGameTest {
    Team t1, t2, t3;
    ArrayList<Team> teamList2, teamList3, teamList4;
    Player p1_2, p2_2, p1_3, p2_3, p3_3, p1_4, p2_4, p3_4, p4_4;
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
            p1_2 = new Player("Franco", t1, Wizard.WOODMAGE, 9);
            p2_2 = new Player("Gigi", t2, Wizard.SANDMAGE, 9);
        } catch (GameException e) {
            fail();
        }
        players2 = new ArrayList<>(2);
        players2.add(p1_2);
        players2.add(p2_2);
        gameWith2 = new ExpertGame((byte) 2, teamList2, players2);
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


        gameWith4 = new ExpertGame((byte) 4, teamList4, players4);
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

        gameWith3 = new ExpertGame((byte) 3, teamList3, players3);
        gameWith3.setCurrentPlayer(p1_3);

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
        gameWith2.setCurrentPlayer(p1_2);
        try {
            for (Color color : Color.values()) {
                p1_2.getEntranceHall().moveStudents(color, p1_2.getEntranceHall().howManyStudents(color), gameWith2.getBag());
            }
        } catch (GameException e) {
            fail();
        }
        try {
            gameWith2.getBag().moveStudents(Color.RED, (byte) 9, p1_2.getEntranceHall());
        } catch (GameException e) {
            fail();
        }
        try {
            byte coins = 1;
            assertEquals(gameWith2.getCoinsPlayer(p1_2), coins);
            gameWith2.move(Color.RED, 0, 1);
            assertEquals(gameWith2.getCoinsPlayer(p1_2), coins);
            gameWith2.move(Color.RED, 0, 1);
            assertEquals(gameWith2.getCoinsPlayer(p1_2), coins);
            gameWith2.move(Color.RED, 0, 1);
            coins += 1;
            assertEquals(gameWith2.getCoinsPlayer(p1_2), coins);
            gameWith2.move(Color.RED, 0, 1);
            gameWith2.move(Color.RED, 0, 1);
            gameWith2.move(Color.RED, 0, 1);
            coins += 1;
            assertEquals(gameWith2.getCoinsPlayer(p1_2), coins);
            gameWith2.move(Color.RED, 0, 1);
            gameWith2.move(Color.RED, 0, 1);
            gameWith2.move(Color.RED, 0, 1);
            coins += 1;
            assertEquals(gameWith2.getCoinsPlayer(p1_2), coins);
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
        ArrayList<Integer> inputs = new ArrayList<>();
        gameWith2.setCurrentPlayer(p1_2);
        try {
            for (Color color : Color.values()) {
                p1_2.getEntranceHall().moveStudents(color, p1_2.getEntranceHall().howManyStudents(color), gameWith2.getBag());
            }
            gameWith2.getBag().moveStudents(Color.RED, (byte) 9, p1_2.getEntranceHall());
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
        assertThrows(NotAllowedException.class, () -> gameWith2.playCharacter(), "Cannot play character card");
        gameWith2.setCurrentPlayer(p1_2);
        try {
            for (Color color : Color.values()) {
                p1_2.getEntranceHall().moveStudents(color, p1_2.getEntranceHall().howManyStudents(color), gameWith2.getBag());
            }
            for (Color color : Color.values()) {
                gameWith2.getBag().moveStudents(color, (byte) 4, p1_2.getEntranceHall());
                for (int i = 0; i < 3; i++) {
                    gameWith2.move(color, 0, 1);
                }
            }
            gameWith2.chooseCharacter((byte) 0);
            try {
                for (int i = 0; i < 3; i++) {
                    gameWith2.setCharacterInput(i);
                }
            } catch (GameException e) {
                fail();
            }
            assertThrows(UnexpectedValueException.class, () -> gameWith2.playCharacter());
            try {
                for (int i = 0; i < 5; i++) {
                    gameWith2.setCharacterInput(i);
                }
            } catch (GameException e) {
                fail();
            }
            assertThrows(UnexpectedValueException.class, () -> gameWith2.playCharacter());
            try {
                for (int i = 0; i < 10; i++) {
                    gameWith2.setCharacterInput(i);
                }
            } catch (GameException e) {
                fail();
            }
            assertThrows(UnexpectedValueException.class, () -> gameWith2.playCharacter());
            try {
                gameWith2.playCharacter();
            } catch (UnexpectedValueException | EndGameException e) {
                gameWith2.setCharacterInput(0);
                try {
                    gameWith2.playCharacter();
                } catch (GameException | EndGameException e1) {
                    gameWith2.setCharacterInput(0);
                    gameWith2.setCharacterInput(1);
                    try {
                        gameWith2.playCharacter();
                    } catch (GameException | EndGameException e2) {
                        fail();
                    }
                }
            }
        } catch (GameException e) {
            fail();
        }
        assertThrows(NotAllowedException.class, () -> gameWith2.playCharacter(), "Cannot play character card");
    }

    @Test
    void calculateProfessorTest() {
        try {
            for (Color color : Color.values()) {
                p1_4.getEntranceHall().moveStudents(color, p1_4.getEntranceHall().howManyStudents(color), gameWith4.getBag());
                p2_4.getEntranceHall().moveStudents(color, p2_4.getEntranceHall().howManyStudents(color), gameWith4.getBag());
                p3_4.getEntranceHall().moveStudents(color, p3_4.getEntranceHall().howManyStudents(color), gameWith4.getBag());
                p4_4.getEntranceHall().moveStudents(color, p4_4.getEntranceHall().howManyStudents(color), gameWith4.getBag());
            }
            gameWith4.getBag().moveStudents(Color.RED, (byte) 1, p1_4.getEntranceHall());
            gameWith4.getBag().moveStudents(Color.BLUE, (byte) 1, p2_4.getEntranceHall());
            gameWith4.getBag().moveStudents(Color.YELLOW, (byte) 1, p3_4.getEntranceHall());
            gameWith4.getBag().moveStudents(Color.GREEN, (byte) 1, p4_4.getEntranceHall());
            for (Player p : players4) {
                gameWith4.setCurrentPlayer(p);
                gameWith4.move(Color.values()[players4.indexOf(p)], 0, 1);
            }
        } catch (GameException e) {
            fail();
        }
        for (Player p : players4) {
            assertEquals(gameWith4.getProfessor()[players4.indexOf(p)], p.getWizard());
        }
    }

    @Test
    void checkMotherNatureTest() {
        gameWith2.setCurrentPlayer(p1_2);
        try {
            gameWith2.playCard((byte) 1);
        } catch (GameException | EndGameException e) {
            fail();
        }
        assertThrows(NotAllowedException.class, () -> gameWith2.moveMotherNature(2));
        byte old = gameWith2.getMotherNaturePosition();
        try {
            gameWith2.moveMotherNature(1);
        } catch (GameException | EndGameException e) {
            fail();
        }
        assertEquals(gameWith2.getMotherNaturePosition(), old + 1);
    }

    @Test
    void calculateInfluenceTest() {
        try {
            for (Color color : Color.values()) {
                p1_3.getEntranceHall().moveStudents(color, p1_3.getEntranceHall().howManyStudents(color), gameWith3.getBag());
                p2_3.getEntranceHall().moveStudents(color, p2_3.getEntranceHall().howManyStudents(color), gameWith3.getBag());
                p3_3.getEntranceHall().moveStudents(color, p3_3.getEntranceHall().howManyStudents(color), gameWith3.getBag());
            }
            gameWith3.getBag().moveStudents(Color.RED, (byte) 6, p1_3.getEntranceHall());
            gameWith3.getBag().moveStudents(Color.BLUE, (byte) 6, p2_3.getEntranceHall());
            gameWith3.getBag().moveStudents(Color.YELLOW, (byte) 6, p3_3.getEntranceHall());

            for (Player p : players3) {
                gameWith3.setCurrentPlayer(p);
                gameWith3.move(Color.values()[players3.indexOf(p)], 0, 1);
            }
        } catch (GameException e) {
            fail();
        }

        try {
            gameWith3.getIslands().get(0).moveAll(gameWith3.getBag());
            for (int i = 0; i < 3; i++) {
                gameWith3.getBag().moveStudents(Color.values()[i], (byte) i, gameWith3.getIslands().get(0));
            }
            gameWith3.calculateInfluence(gameWith3.getIslands().get(0));
        } catch (GameException | EndGameException e) {
            fail();
        }

        assertEquals(gameWith3.getIslands().get(0).getTeamColor(), p3_3.getTeam().getHouseColor());
        gameWith3.setCurrentPlayer(p1_3);

        try {
            gameWith3.getBag().moveStudents(Color.RED, (byte) 3, gameWith3.getIslands().get(0));
            gameWith3.calculateInfluence(gameWith3.getIslands().get(0));
        } catch (GameException | EndGameException e) {
            fail();
        }
        assertEquals(gameWith3.getIslands().get(0).getTeamColor(), p3_3.getTeam().getHouseColor());
        try {
            gameWith3.getBag().moveStudents(Color.RED, (byte) 1, gameWith3.getIslands().get(0));
            gameWith3.calculateInfluence(gameWith3.getIslands().get(0));
        } catch (GameException | EndGameException e) {
            fail();
        }
        assertEquals(gameWith3.getIslands().get(0).getTeamColor(), p1_3.getTeam().getHouseColor());
        try {
            gameWith3.getBag().moveStudents(Color.BLUE, (byte) 10, gameWith3.getIslands().get(0));
            gameWith3.calculateInfluence(gameWith3.getIslands().get(0));
        } catch (GameException | EndGameException e) {
            fail();
        }
        assertEquals(gameWith3.getIslands().get(0).getTeamColor(), p2_3.getTeam().getHouseColor());

    }

}
