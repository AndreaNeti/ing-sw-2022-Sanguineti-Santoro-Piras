package it.polimi.ingsw.Server.model;

import it.polimi.ingsw.Server.controller.Server;
import it.polimi.ingsw.Server.model.GameComponents.Bag;
import it.polimi.ingsw.Server.model.GameComponents.GameComponent;
import it.polimi.ingsw.Util.*;
import it.polimi.ingsw.exceptions.serverExceptions.EndGameException;
import it.polimi.ingsw.exceptions.serverExceptions.GameException;
import it.polimi.ingsw.exceptions.serverExceptions.NotAllowedException;
import it.polimi.ingsw.exceptions.serverExceptions.NotEnoughStudentsException;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class CharacterCardTest {
    Team t1, t2;
    Player p1, p2;
    ArrayList<Team> teamList = new ArrayList<>();
    ArrayList<Player> playerList = new ArrayList<>();
    ExpertGame game;
    CharacterCard c0, c1, c2, c3, c4, c5, c6, c7, c8, c9, c10, c11;
    Byte charToSelect;

    public CharacterCardTest() {
        MatchType matchType = new MatchType((byte) 2, true);
        MatchConstants matchConstants = Server.getMatchConstants(matchType);
        t1 = new Team(HouseColor.WHITE, (byte) 1, (byte) 8);
        t2 = new Team(HouseColor.BLACK, (byte) 1, (byte) 8);
        try {
            p1 = new Player("Franco", t1, Wizard.WOODMAGE, matchConstants);
            p2 = new Player("Gigi", t2, Wizard.SANDMAGE, matchConstants);
        } catch (GameException e) {
            fail();
        }
        teamList.add(t1);
        teamList.add(t2);
        playerList.add(p1);
        playerList.add(p2);
        game = new ExpertGame(teamList, matchConstants);
        // fill lunch halls, p1 will gain enough coins to test char.play methods
        game.setCurrentPlayer(p1);
        try {
            for (Color c : Color.values()) {
                p1.getEntranceHall().moveStudents(c, p1.getEntranceHall().howManyStudents(c), game.getBag());
                p2.getEntranceHall().moveStudents(c, p2.getEntranceHall().howManyStudents(c), game.getBag());
            }
            for (Color c : Color.values()) {
                game.getBag().moveStudents(c, (byte) 3, p1.getEntranceHall());
                for (int i = 0; i < 3; i++) {
                    game.move(c, 0, 1);
                }
            }
        } catch (GameException e) {
            e.printStackTrace();
            fail();
        }
//            for (Player p : playerList) {
//                game.setCurrentPlayer(p);
//                byte nStudents = (byte) Math.min(10, p.getEntranceHall().howManyStudents(c));
//                for (byte i = 0; i < nStudents; i++) {
//                    try {
//                        // move from entrance to lunch hall
//                        game.move(c, 0, 1);
//                    } catch (GameException e) {
//                        fail();
//                    }
//                }
//            }
        // Have to select a char card to pass inputs, get the first available
        charToSelect = game.transformAllGameInDelta().getCharacters().stream().findFirst().get().getCharId();
        c0 = new CharacterCard(new Char0((byte) -10), CharacterCardData.CH0);
        try {
            game.drawStudents((GameComponent) c0.getLogicCard(), (byte) ((GameComponent) c0.getLogicCard()).getMaxStudents());
        } catch (EndGameException | GameException e) {
            fail();
        }
        c1 = new CharacterCard(new Char1(), CharacterCardData.CH1);
        c2 = new CharacterCard(new Char2(), CharacterCardData.CH2);
        c3 = new CharacterCard(new Char3(), CharacterCardData.CH3);
        c4 = new CharacterCard(new Char4(), CharacterCardData.CH4);
        c5 = new CharacterCard(new Char5(), CharacterCardData.CH5);
        c6 = new CharacterCard(new Char6((byte) -11), CharacterCardData.CH6);
        try {
            game.drawStudents((GameComponent) c6.getLogicCard(), (byte) ((GameComponent) c6.getLogicCard()).getMaxStudents());
        } catch (EndGameException | GameException e) {
            fail();
        }
        c7 = new CharacterCard(new Char7(), CharacterCardData.CH7);
        c8 = new CharacterCard(new Char8(), CharacterCardData.CH8);
        c9 = new CharacterCard(new Char9(), CharacterCardData.CH9);
        c10 = new CharacterCard(new Char10((byte) -12), CharacterCardData.CH10);
        try {
            game.drawStudents((GameComponent) c10.getLogicCard(), (byte) ((GameComponent) c10.getLogicCard()).getMaxStudents());
        } catch (EndGameException | GameException e) {
            fail();
        }
        c11 = new CharacterCard(new Char11(), CharacterCardData.CH11);
    }

    @Test
    void playChar0() {
        int color = 0;
        int islandId = 2 * MatchType.MAX_PLAYERS; // first island id
        try {
            game.chooseCharacter(charToSelect);
            game.setCharacterInputs(Arrays.asList(-5, -1));
            assertThrows(NotAllowedException.class, () -> c0.play(game), "not valid inputs");
            // also resets character inputs
            game.setCurrentPlayer(p1);
            // use first available color on the card to test (it's chosen randomly)
            while (((GameComponent) c0.getLogicCard()).howManyStudents(Color.values()[color]) == 0 && color < Color.values().length) {
                color++;
            }
            game.chooseCharacter(charToSelect);
            game.setCharacterInputs(Arrays.asList(color, islandId));
        } catch (GameException e) {
            fail(e.getMessage());
        }
        byte old = 0;
        try {
            old = game.getComponentById(islandId).howManyStudents(Color.values()[color]);
            c0.play(game);
        } catch (GameException | EndGameException e) {
            fail(e);
        }
        try {
            assertEquals(game.getComponentById(islandId).howManyStudents(Color.values()[color]), old + 1);
        } catch (GameException e) {
            fail(e);
        }
        assertEquals(((GameComponent) c0.getLogicCard()).howManyStudents(), ((GameComponent) c0.getLogicCard()).getMaxStudents());
    }

    @Test
    void playChar1() {
        try {
            game.setCurrentPlayer(p2);
            for (Color c : Color.values()) {
                p2.getEntranceHall().moveStudents(c, p2.getEntranceHall().howManyStudents(c), game.getBag());
            }
            game.getBag().moveStudents(Color.RED, (byte) 4, p2.getEntranceHall());
            for (int i = 0; i < 4; i++) {
                //entranceHall of p2 is 2 and lunchHall is 3
                game.move(Color.RED, 2, 3);
            }
        } catch (GameException e) {
            fail();
        }
        assertEquals(game.getProfessor()[0], p2.getWizard());
        game.setCurrentPlayer(p1);
        try {
            game.getBag().moveStudents(Color.RED, (byte) 1, p1.getLunchHall());
        } catch (GameException e) {
            fail();
        }

        try {
            c1.play(game);
        } catch (GameException | EndGameException e) {
            fail();
        }

        assertEquals(game.getProfessor()[0], p1.getWizard());
    }

    @Test
    void playChar2() {
        try {
            game.chooseCharacter(charToSelect);
            game.setCharacterInputs(List.of(-1));
            assertThrows(NotAllowedException.class, () -> c2.play(game), "not valid inputs");
        } catch (GameException e) {
            fail();
        }
        int color = -1;
        Team winnerTeam = null;
        for (int i = 0; i < 5 && color < 0; i++) {
            Wizard profController = game.getProfessor()[i];
            if (profController != null) {
                color = i;
                // team index is player index % team size
                winnerTeam = game.getTeams().get(profController.ordinal() % game.getTeams().size());
            }
        }
        game.setCurrentPlayer(p1);
        try {
            game.chooseCharacter(charToSelect);
            game.getBag().moveStudents(Color.values()[color], (byte) 5, game.getIslands().get(0));
            game.setCharacterInputs(List.of(2 * MatchType.MAX_PLAYERS));
        } catch (GameException e) {
            fail();
        }

        try {
            c2.play(game);
        } catch (GameException | EndGameException e) {
            fail();
        }

        assertEquals(game.getIslands().get(0).getTeamColor(), winnerTeam.getHouseColor());
    }

    @Test
    void playChar3() {
        game.setCurrentPlayer(p1);
        try {
            game.playCard(new AssistantCard((byte) 3, (byte) 2));
            game.chooseCharacter(charToSelect);
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
            game.chooseCharacter(charToSelect);
            game.setCharacterInputs(List.of(-1));
        } catch (GameException e) {
            fail();
        }
        assertThrows(NotAllowedException.class, () -> c4.play(game), "not valid inputs");
        game.setCurrentPlayer(p1);

        try {
            game.getIslands().get(2).moveAll(game.getBag());
            game.getBag().moveStudents(Color.RED, (byte) 2, game.getIslands().get(2));
            game.chooseCharacter(charToSelect);
            //id of island 2 is 2*maxPlayers + 2
            game.setCharacterInputs(List.of(2 * MatchType.MAX_PLAYERS + 2));
        } catch (GameException e) {
            fail();
        }

        try {
            c4.play(game);
        } catch (GameException | EndGameException e) {
            fail();
        }
        assertEquals(game.getIslands().get(2).getProhibitions(), 1);

        try {
            game.calculateInfluence(game.getIslands().get(2));
        } catch (EndGameException e) {
            fail();
        }

        assertNull(game.getIslands().get(2).getTeamColor());
    }

    @Test
    void playChar5() {
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
                game.getBag().moveStudents(color, (byte) (1), game.getIslands().get(4));
            }
        } catch (GameException e) {
            fail();
        }
        HouseColor old = null;
        try {
            game.calculateInfluence(game.getIslands().get(4));
            old = game.getIslands().get(4).getTeamColor();
            game.setCurrentPlayer(p1);
            game.chooseCharacter(charToSelect);
            game.getBag().moveStudents(Color.values()[4], (byte) (2), game.getIslands().get(4));
            c5.play(game);
            game.calculateInfluence(game.getIslands().get(4));
        } catch (GameException | EndGameException e) {
            fail(e.getMessage());
        }
        assertNotEquals(game.getIslands().get(4).getTeamColor(), old);
    }

    @Test
    void playChar6() {
        int color1 = 0, color2 = Color.values().length - 1;
        try {

            game.chooseCharacter(charToSelect);
            game.setCharacterInputs(Arrays.asList(-5, -1));
            assertThrows(NotAllowedException.class, () -> c6.play(game), "not valid inputs");
            // also resets character inputs
            game.setCurrentPlayer(p1);
            // use first available color on the card to test (it's chosen randomly)
            while (((GameComponent) c6.getLogicCard()).howManyStudents(Color.values()[color1]) == 0 && color1 < Color.values().length) {
                game.setCurrentPlayer(p1);
                game.chooseCharacter(charToSelect);
                game.setCharacterInputs(Arrays.asList(color1, color2));
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
                game.chooseCharacter(charToSelect);
                game.setCharacterInputs(Arrays.asList(color1, color2));
                assertThrows(NotEnoughStudentsException.class, () -> c6.play(game), "not enough students");
                color2--;
            }
            game.setCurrentPlayer(p1);
            game.chooseCharacter(charToSelect);
            game.setCharacterInputs(Arrays.asList(color1, color2));
        } catch (GameException e) {
            fail();
        }
        byte oldC6Color1 = ((GameComponent) c6.getLogicCard()).howManyStudents(Color.values()[color1]);
        byte oldC6Color2 = ((GameComponent) c6.getLogicCard()).howManyStudents(Color.values()[color2]);
        byte oldEntranceColor1 = game.getCurrentPlayer().getEntranceHall().howManyStudents(Color.values()[color1]);
        byte oldEntranceColor2 = game.getCurrentPlayer().getEntranceHall().howManyStudents(Color.values()[color2]);
        try {
            c6.play(game);
        } catch (GameException | EndGameException e) {
            fail();
        }
        if (color1 != color2) {
            assertEquals(((GameComponent) c6.getLogicCard()).howManyStudents(Color.values()[color1]), oldC6Color1 - 1);
            assertEquals(((GameComponent) c6.getLogicCard()).howManyStudents(Color.values()[color2]), oldC6Color2 + 1);
            assertEquals(game.getCurrentPlayer().getEntranceHall().howManyStudents(Color.values()[color1]), oldEntranceColor1 + 1);
            assertEquals(game.getCurrentPlayer().getEntranceHall().howManyStudents(Color.values()[color2]), oldEntranceColor2 - 1);
        } else {
            assertEquals(((GameComponent) c6.getLogicCard()).howManyStudents(Color.values()[color1]), oldC6Color1);
            assertEquals(game.getCurrentPlayer().getEntranceHall().howManyStudents(Color.values()[color1]), oldEntranceColor1);
        }
    }

    @Test
    void playChar7() {
        try {
            game.getIslands().get(4).moveAll(game.getIslands().get(1));
            Color color;
            for (int i = 2; i < 5; i++) {
                color = Color.values()[i];
                game.getBag().moveStudents(color, (byte) 5, p2.getLunchHall());
            }
            game.calculateProfessor();
            for (int i = 0; i < 5; i++) {
                color = Color.values()[i];
                game.getBag().moveStudents(color, (byte) (1), game.getIslands().get(4));
            }
        } catch (GameException e) {
            fail();
        }
        try {
            game.setCurrentPlayer(p1);
            game.chooseCharacter(charToSelect);
            c7.play(game);
            game.calculateInfluence(game.getIslands().get(4));
        } catch (GameException | EndGameException e) {
            fail();
        }
        // p1 team is t1
        assertEquals(game.getIslands().get(4).getTeamColor(), t1.getHouseColor());
    }

    @Test
    void playChar8() {
        Bag test = new Bag((byte) 50);
        try {
            game.chooseCharacter(charToSelect);
            game.setCharacterInputs(List.of(-1));
            assertThrows(NotAllowedException.class, () -> c8.play(game), "not valid inputs");
        } catch (GameException e) {
            e.printStackTrace();
            fail();
        }
        game.setCurrentPlayer(p1);
        try {
            game.getIslands().get(4).moveAll(game.getBag());
            Color color;
            for (int i = 3; i < 5; i++) {
                color = Color.values()[i];
                test.moveStudents(color, (byte) 4, p2.getLunchHall());
            }
            game.calculateProfessor();
            for (int i = 0; i < 5; i++) {
                color = Color.values()[i];
                test.moveStudents(color, (byte) (i + 1), game.getIslands().get(4));
            }
        } catch (GameException e) {
            e.printStackTrace();
            fail();
        }
        try {
            game.chooseCharacter(charToSelect);
            game.setCharacterInputs(List.of(4));
            c8.play(game);
            game.calculateInfluence(game.getIslands().get(4));
        } catch (GameException | EndGameException e) {
            e.printStackTrace();
            fail();
        }
        // p1 team is t1
        assertEquals(game.getIslands().get(4).getTeamColor(), t1.getHouseColor());
    }

    @Test
    void playChar9() {
        try {
            game.chooseCharacter(charToSelect);
            game.setCharacterInputs(Arrays.asList(-1, -1));
            assertThrows(NotAllowedException.class, () -> c9.play(game), "not valid inputs");
            game.setCurrentPlayer(p1);
        } catch (GameException e) {
            fail();
        }
        // remove all red students from p1 lunch hall and tests exception
        try {
            p1.getLunchHall().moveStudents(Color.RED, p1.getLunchHall().howManyStudents(Color.RED), game.getBag());
            game.chooseCharacter(charToSelect);
            // first input is lunch hall color
            game.setCharacterInputs(Arrays.asList(Color.RED.ordinal(), 1));
            assertThrows(NotEnoughStudentsException.class, () -> c9.play(game), "can't swap this, no red students");
            game.setCurrentPlayer(p1);
        } catch (GameException e) {
            fail();
        }
        try {
            // entranceHall has no students, should launch exception
            game.chooseCharacter(charToSelect);
            game.setCharacterInputs(Arrays.asList(1, 3));
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
            game.chooseCharacter(charToSelect);
            game.setCharacterInputs(Arrays.asList(colorLunch, Color.RED.ordinal()));
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
            if (((GameComponent) c10.getLogicCard()).howManyStudents(Color.values()[i]) != 0) {
                color = i;
            }
        }

        for (int i = 0; i < 5 && wrongColor == -1; i++) {
            if (((GameComponent) c10.getLogicCard()).howManyStudents(Color.values()[i]) == 0) {
                wrongColor = i;
            }
        }

        try {
            game.chooseCharacter(charToSelect);
            game.setCharacterInputs(List.of(wrongColor));

            assertThrows(NotEnoughStudentsException.class, () -> c10.play(game), "Not enough students");

            game.setCurrentPlayer(p1);
            game.chooseCharacter(charToSelect);
            game.setCharacterInputs(List.of(color));
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
        assertEquals(((GameComponent) c10.getLogicCard()).howManyStudents(), ((GameComponent) c10.getLogicCard()).getMaxStudents());
    }

    @Test
    void playChar11() {
        Random rand = new Random();
        int color = rand.nextInt(Color.values().length);
        try {
            game.chooseCharacter(charToSelect);
            game.setCharacterInputs(List.of(-5));
            assertThrows(NotAllowedException.class, () -> c11.play(game), "not valid inputs");

            for (Player p : game.getPlayers()) {
                try {
                    game.drawStudents(p.getLunchHall(), (byte) 20);
                } catch (EndGameException e) {
                    fail();
                }
            }
            // also resets character inputs
            game.setCurrentPlayer(p1);
            game.chooseCharacter(charToSelect);
            game.setCharacterInputs(List.of(color));
        } catch (GameException e) {
            fail(e);
        }
        ArrayList<Player> players = game.getPlayers();
        byte[] oldValues = new byte[players.size()];
        for (byte i = 0; i < players.size(); i++)
            oldValues[i] = players.get(i).getLunchHall().howManyStudents(Color.values()[color]);
        try {
            c11.play(game);
        } catch (GameException | EndGameException e) {
            e.printStackTrace();
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
        assertEquals(c0.getCharId(), 0);
        assertEquals(c1.getCharId(), 1);
        assertEquals(c2.getCharId(), 2);
        assertEquals(c3.getCharId(), 3);
        assertEquals(c4.getCharId(), 4);
        assertEquals(c5.getCharId(), 5);
        assertEquals(c6.getCharId(), 6);
        assertEquals(c7.getCharId(), 7);
        assertEquals(c8.getCharId(), 8);
        assertEquals(c9.getCharId(), 9);
        assertEquals(c10.getCharId(), 10);
        assertEquals(c11.getCharId(), 11);
        GameComponent test = (GameComponent) c0.getLogicCard();
        assertEquals(test.getId(), -10);
        test = (GameComponent) c6.getLogicCard();
        assertEquals(test.getId(), -11);
        test = (GameComponent) c10.getLogicCard();
        assertEquals(test.getId(), -12);

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