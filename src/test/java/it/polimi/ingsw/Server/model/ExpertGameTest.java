package it.polimi.ingsw.Server.model;

import it.polimi.ingsw.Enum.Color;
import it.polimi.ingsw.Enum.HouseColor;
import it.polimi.ingsw.Enum.Wizard;
import it.polimi.ingsw.Server.controller.MatchConstants;
import it.polimi.ingsw.Server.controller.MatchType;
import it.polimi.ingsw.Server.controller.Server;
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
        MatchType matchType;
        MatchConstants matchConstants;

        // create game with 2 players
        matchType = new MatchType((byte) 2, true);
        matchConstants = Server.getMatchConstants(matchType);
        t1 = new Team(HouseColor.BLACK, (byte) 1, (byte) 8);
        t2 = new Team(HouseColor.WHITE, (byte) 1, (byte) 8);

        teamList2 = new ArrayList<>(2);
        teamList2.add(t1);
        teamList2.add(t2);
        try {
            p1_2 = new Player("Franco", t1, Wizard.WOODMAGE, matchConstants);
            p2_2 = new Player("Gigi", t2, Wizard.SANDMAGE, matchConstants);
        } catch (GameException e) {
            fail();
        }
        players2 = new ArrayList<>(2);
        players2.add(p1_2);
        players2.add(p2_2);
        gameWith2 = new ExpertGame(teamList2, matchConstants);
        gameWith2.setCurrentPlayer(p1_2);

        // create game with 4 players
        matchType = new MatchType((byte) 4, true);
        matchConstants = Server.getMatchConstants(matchType);
        teamList4 = new ArrayList<>(2);
        t1 = new Team(HouseColor.BLACK, (byte) 2, (byte) 8);
        t2 = new Team(HouseColor.WHITE, (byte) 2, (byte) 8);
        teamList4.add(t1);

        teamList4.add(t2);
        try {
            p1_4 = new Player("Franco", t1, Wizard.WOODMAGE, matchConstants);
            p2_4 = new Player("Gigi", t2, Wizard.SANDMAGE, matchConstants);
            p3_4 = new Player("Carola", t1, Wizard.AIRMAGE, matchConstants);
            p4_4 = new Player("Filomena", t2, Wizard.ELECTROMAGE, matchConstants);

        } catch (GameException e) {
            fail();
        }
        players4 = new ArrayList<>(4);
        players4.add(p1_4);
        players4.add(p2_4);
        players4.add(p3_4);
        players4.add(p4_4);


        gameWith4 = new ExpertGame(teamList4, matchConstants);
        gameWith4.setCurrentPlayer(p1_4);

        //create a game with 3 people
        matchType = new MatchType((byte) 3, true);
        matchConstants = Server.getMatchConstants(matchType);
        t1 = new Team(HouseColor.BLACK, (byte) 2, (byte) 6);
        t2 = new Team(HouseColor.WHITE, (byte) 2, (byte) 6);
        t3 = new Team(HouseColor.GREY, (byte) 1, (byte) 6);
        teamList3 = new ArrayList<>(3);
        teamList3.add(t1);
        teamList3.add(t2);
        teamList3.add(t3);
        try {
            p1_3 = new Player("Franco", t1, Wizard.WOODMAGE, matchConstants);
            p2_3 = new Player("Gigi", t2, Wizard.SANDMAGE, matchConstants);
            p3_3 = new Player("Carola", t3, Wizard.AIRMAGE, matchConstants);


        } catch (GameException e) {
            fail();
        }
        players3 = new ArrayList<>(3);
        players3.add(p1_3);
        players3.add(p2_3);
        players3.add(p3_3);

        gameWith3 = new ExpertGame(teamList3, matchConstants);
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
            gameWith2.getBag().moveStudents(Color.RED, (byte) 6, p1_2.getEntranceHall());
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

            gameWith2.getBag().moveStudents(Color.RED, (byte) 3, p1_2.getEntranceHall());
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
                gameWith2.setProhibition(new Island((byte) (i + 2 * gameWith2.getPlayerSize())));
            }
        } catch (GameException e) {
            fail();
        }
        assertThrows(NotAllowedException.class, () -> gameWith2.setProhibition(new Island((byte) 6)), "No more prohibitions");
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
            gameWith2.getBag().moveStudents(Color.RED, (byte) 6, p1_2.getEntranceHall());
            for (byte i = 0; i < 6; i++) {
                gameWith2.move(Color.RED, 0, 1);
            }
            gameWith2.getBag().moveStudents(Color.RED, (byte) 3, p1_2.getEntranceHall());
            for (byte i = 0; i < 3; i++) {
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
                gameWith2.getBag().moveStudents(color, (byte) 3, p1_2.getEntranceHall());
                for (byte i = 0; i < 3; i++) {
                    gameWith2.move(color, 0, 1);
                }
            }
            for (Color color : Color.values()) {
                gameWith2.getBag().moveStudents(color, (byte) 1, p1_2.getEntranceHall());
            }
            gameWith2.chooseCharacter((byte) 0);
        } catch (GameException e) {
            fail();
        }
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
        } catch (GameException | EndGameException e) {
            // additional check if card selected is c0, c6 or c10 and it does not contain the selected color
            boolean worked = false;
            for (int i = 0; i < 5 && !worked; i++) {
                try {
                    gameWith2.setCharacterInput(i);
                    gameWith2.playCharacter();
                    worked = true;
                } catch (GameException | EndGameException e1) {
                    for (int j = 0; j < 5 && !worked; j++) {
                        try {
                            gameWith2.setCharacterInput(j);
                            gameWith2.setCharacterInput(i);
                            gameWith2.playCharacter();
                            worked = true;
                        } catch (GameException | EndGameException ignored) {
                        }

                    }
                }

            }
            if (!worked)

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
                gameWith4.move(Color.values()[players4.indexOf(p)], 2 * p.getWizard().ordinal(), 2 * p.getWizard().ordinal() + 1);
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
            gameWith3.getBag().moveStudents(Color.RED, (byte) 4, p1_3.getEntranceHall());
            gameWith3.getBag().moveStudents(Color.BLUE, (byte) 4, p2_3.getEntranceHall());
            gameWith3.getBag().moveStudents(Color.YELLOW, (byte) 4, p3_3.getEntranceHall());

            for (Player p : players3) {
                gameWith3.setCurrentPlayer(p);
                gameWith3.move(Color.values()[players3.indexOf(p)], 2 * p.getWizard().ordinal(), 2 * p.getWizard().ordinal() + 1);
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
        // p3_3 team is t3
        assertEquals(gameWith3.getIslands().get(0).getTeamColor(), t3.getHouseColor());
        assertEquals(gameWith3.getTeams().get(0).getTowersLeft(), 6);
        assertEquals(gameWith3.getTeams().get(1).getTowersLeft(), 6);
        assertEquals(gameWith3.getTeams().get(2).getTowersLeft(), 5);
        gameWith3.setCurrentPlayer(p1_3);

        try {
            gameWith3.getBag().moveStudents(Color.RED, (byte) 3, gameWith3.getIslands().get(0));
            gameWith3.calculateInfluence(gameWith3.getIslands().get(0));
        } catch (GameException | EndGameException e) {
            fail();
        }
        // p3_3 team is t3
        assertEquals(gameWith3.getIslands().get(0).getTeamColor(), t3.getHouseColor());
        assertEquals(gameWith3.getTeams().get(0).getTowersLeft(), 6);
        assertEquals(gameWith3.getTeams().get(1).getTowersLeft(), 6);
        assertEquals(gameWith3.getTeams().get(2).getTowersLeft(), 5);
        try {
            gameWith3.getBag().moveStudents(Color.RED, (byte) 1, gameWith3.getIslands().get(0));
            gameWith3.calculateInfluence(gameWith3.getIslands().get(0));
        } catch (GameException | EndGameException e) {
            fail();
        }
        // p1_3 team is t1
        assertEquals(gameWith3.getIslands().get(0).getTeamColor(), t1.getHouseColor());
        assertEquals(gameWith3.getTeams().get(0).getTowersLeft(), 5);
        assertEquals(gameWith3.getTeams().get(1).getTowersLeft(), 6);
        assertEquals(gameWith3.getTeams().get(2).getTowersLeft(), 6);
        try {
            gameWith3.getBag().moveStudents(Color.BLUE, (byte) 7, gameWith3.getIslands().get(0));
            gameWith3.calculateInfluence(gameWith3.getIslands().get(0));
        } catch (GameException | EndGameException e) {
            fail();
        }
        // p2_3 team is t3
        assertEquals(gameWith3.getIslands().get(0).getTeamColor(), t2.getHouseColor());
        assertEquals(gameWith3.getTeams().get(0).getTowersLeft(), 6);
        assertEquals(gameWith3.getTeams().get(1).getTowersLeft(), 5);
        assertEquals(gameWith3.getTeams().get(2).getTowersLeft(), 6);

    }

}
