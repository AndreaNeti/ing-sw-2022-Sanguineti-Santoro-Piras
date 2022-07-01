package it.polimi.ingsw.Server.controller;

import it.polimi.ingsw.Util.AssistantCard;
import it.polimi.ingsw.Util.Color;
import it.polimi.ingsw.Util.MatchType;
import it.polimi.ingsw.exceptions.serverExceptions.EndGameException;
import it.polimi.ingsw.exceptions.serverExceptions.GameException;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;


class ControllerTest {
    private final Controller controllerExpert, controllerExpert2, controllerNormal;
    private final ClientHandler p1, p2, p3, p4;
    private final List<AssistantCard> assistantCardList = new ArrayList<>(11);

    public ControllerTest() {
        controllerExpert = new Controller(new MatchType((byte) 4, true), 69L);
        controllerExpert2 = new Controller(new MatchType((byte) 2, true), 69L);
        controllerNormal = new Controller(new MatchType((byte) 3, false), 69L);

        p1 = mock(ClientHandler.class);
        p2 = mock(ClientHandler.class);
        p3 = mock(ClientHandler.class);
        p4 = mock(ClientHandler.class);

        try {
            controllerExpert.addPlayer(p1, "Gigi");
            controllerExpert.addPlayer(p2, "Franco");
            controllerExpert.addPlayer(p3, "Carola");
            controllerExpert.addPlayer(p4, "Filomena");
        } catch (GameException e) {
            fail(e);
        }

        try {
            controllerNormal.addPlayer(p1, "Gigi");
            controllerNormal.addPlayer(p2, "Franco");
            controllerNormal.addPlayer(p3, "Carola");
        } catch (GameException e) {
            fail(e);
        }

        try {
            controllerExpert2.addPlayer(p1, "Gigi");
            controllerExpert2.addPlayer(p2, "Franco");
        } catch (GameException e) {
            fail(e);
        }
        // just to make indexes equal to card value
        assistantCardList.add(new AssistantCard((byte) 0, (byte) 0));
        for (byte i = 1; i <= 10; i++)
            assistantCardList.add(new AssistantCard(i, (byte) ((i + 1) / 2)));
    }

    @Test
    void addPlayerTest() {
        ClientHandler p = mock(ClientHandler.class);
        assertThrows(GameException.class, () -> controllerExpert.addPlayer(p, "Impostor"), "Match is full");
        assertThrows(GameException.class, () -> controllerNormal.addPlayer(p, "Impostor"), "Match is full");
    }

    @Test
    void playCardTest() {
        try {
            controllerExpert.playCard(assistantCardList.get(1));
        } catch (GameException | EndGameException | NullPointerException e) {
            fail(e);
        }
        assertThrows(GameException.class, () -> controllerExpert.playCard(assistantCardList.get(1)), "Cannot play this card");
        assertDoesNotThrow(() -> controllerExpert.playCard(assistantCardList.get(2)));
        try {
            for (int i = 3; i < 5; i++)
                controllerExpert.playCard(assistantCardList.get(i));
        } catch (GameException | EndGameException | NullPointerException e) {
            fail(e);
        }
        assertThrows(GameException.class, () -> controllerExpert.playCard(assistantCardList.get(6)), "Not in planification phase");
    }

    @Test
    void moveTest() {
        assertThrows(GameException.class, () -> controllerExpert.move(Color.RED, 0), "Not in action phase");
        try {
            for (int i = 3; i < 7; i++)
                controllerExpert.playCard(assistantCardList.get(i));
        } catch (GameException | EndGameException | NullPointerException e) {
            fail(e);
        }
        assertThrows(GameException.class, () -> controllerExpert.move(Color.RED, 0), "Can't move to the selected GameComponent");
        assertThrows(GameException.class, () -> controllerExpert.move(Color.RED, -1), "Can't move to the selected GameComponent");
        int moved = 0;
        for (int i = 0; i < 3; i++) {
            for (Color c : Color.values()) {
                try {
                    controllerExpert.move(c, 1);
                    moved++;
                } catch (GameException | EndGameException ignored) {
                }
            }
        }
        if (moved != 3) fail();
        assertThrows(GameException.class, () -> controllerExpert.move(Color.RED, 0), "Wrong phase");
    }

    @Test
    void moveMotherNatureTest() {
        assertThrows(GameException.class, () -> controllerExpert.moveMotherNature(1), "Not in action phase");
        try {
            for (int i = 3; i < 7; i++)
                controllerExpert.playCard(assistantCardList.get(i));
        } catch (GameException | EndGameException | NullPointerException e) {
            fail(e);
        }
        assertThrows(GameException.class, () -> controllerExpert.moveMotherNature(1), "Wrong phase");
        int moved = 0;
        for (int i = 0; i < 3; i++) {
            for (Color c : Color.values()) {
                try {
                    controllerExpert.move(c, 1);
                    moved++;
                } catch (GameException | EndGameException ignored) {
                }
            }
        }
        if (moved != 3) fail();
        assertDoesNotThrow(() -> controllerExpert.moveMotherNature(1));
        assertThrows(GameException.class, () -> controllerExpert.moveMotherNature(1), "Wrong phase");
    }

    @Test
    void moveFromCloudTest() {
        assertThrows(GameException.class, () -> controllerExpert.moveFromCloud(0), "Not in action phase");
        try {
            for (int i = 3; i < 7; i++)
                controllerExpert.playCard(assistantCardList.get(i));
        } catch (GameException | EndGameException | NullPointerException e) {
            fail(e);
        }
        assertThrows(GameException.class, () -> controllerExpert.moveFromCloud(0), "Wrong phase");
        int moved = 0;
        for (int i = 0; i < 3; i++) {
            for (Color c : Color.values()) {
                try {
                    controllerExpert.move(c, 1);
                    moved++;
                } catch (GameException | EndGameException ignored) {
                }
            }
        }
        if (moved != 3) fail();
        assertThrows(GameException.class, () -> controllerExpert.moveFromCloud(0), "Wrong phase");
        try {
            controllerExpert.moveMotherNature(1);
        } catch (GameException | EndGameException e) {
            fail(e);
        }
        assertThrows(GameException.class, () -> controllerExpert.moveFromCloud(1), "Component is not a cloud");
        assertThrows(GameException.class, () -> controllerExpert.moveFromCloud(-5), "Component is not a cloud");
        assertDoesNotThrow(() -> controllerExpert.moveFromCloud(-1));
        assertThrows(GameException.class, () -> controllerExpert.moveFromCloud(0), "Wrong phase");
    }

    @Test
    void sendMessageTest() {
        assertThrows(NullPointerException.class, () -> controllerExpert.sendMessage(null, "Ciao"));
        ClientHandler clientHandler = mock(ClientHandler.class);
        assertDoesNotThrow(() -> controllerExpert.sendMessage(clientHandler, "Ciao"));
    }

    @Test
    void chooseCharacterTest() {
        assertThrows(GameException.class, () -> controllerExpert2.chooseCharacter((byte) 1), "Not in action phase");
        for (int i = 0; i < 8; i++) {
            System.out.println("turn" + i);
            for (int j = i + 1; j < i + 3; j++) {
                try {
                    controllerExpert2.playCard(assistantCardList.get(j));
                } catch (GameException | EndGameException e) {
                    fail(e.getMessage());
                }
            }
            for (int k = 0; k < 2; k++) {
                int moved = 0;
                for (Color c : Color.values()) {
                    for (int j = 0; j < 3 && moved != 3; j++) {
                        try {
                            //2k+1 is the lunch hall
                            controllerExpert2.move(c, 2 * k + 1);
                            moved++;
                        } catch (GameException | EndGameException ignored) {
                        }
                    }
                }
                if (moved != 3) {
                    System.out.println("couldn't move any student to the lunch hall, moving it to the first island");
                    for (Color c : Color.values()) {
                        for (int j = 0; j < 3 && moved != 3; j++) {
                            try {
                                controllerExpert2.move(c, 2 * MatchType.MAX_PLAYERS);
                                moved++;
                            } catch (GameException | EndGameException ignored) {
                            }
                        }
                    }
                    if (moved != 3) fail();
                }

                try {
                    controllerExpert2.moveMotherNature((i + 1) / 2);
                } catch (GameException e) {
                    fail(e.getMessage());
                } catch (EndGameException e) {
                    assertTrue(e.isEndInstantly());
                    return;
                }
                try {
                    controllerExpert2.moveFromCloud(-1 - k % 2);
                } catch (GameException | EndGameException e) {
                    fail(e.getMessage());
                }
            }
        }
        try {
            controllerExpert2.playCard(assistantCardList.get(9));
            controllerExpert2.playCard(assistantCardList.get(10));
        } catch (GameException | EndGameException e) {
            fail(e.getMessage());
        }
        // check if 3 different character cards have been selected
        System.out.println("Selecting characters");
        int nCharacters = 12, selected = 3, counter = 0;
        for (byte i = -10; i > -10 - nCharacters; i--) {
            try {
                controllerExpert2.chooseCharacter(i);
                counter++;
                if (counter >= selected)
                    break;
            } catch (GameException e) {
                System.out.println(e.getMessage());
            }
        }
        assertEquals(selected, counter);
    }

    @Test
    void setCharacterInputTest() {
        assertThrows(GameException.class, () -> controllerExpert2.setCharacterInputs(List.of(0)), "Not in action phase");
        chooseCharacterTest();
        assertDoesNotThrow(() -> controllerExpert2.setCharacterInputs(List.of(0)));
    }

    @Test
    void playCharacterTest() {
        assertThrows(GameException.class, controllerExpert2::playCharacter, "Not in action phase");
        chooseCharacterTest();
        // rare case where game ends before being able to play any card, skip this test.
        if(controllerExpert2.isGameFinished())
            return;
        try {
            controllerExpert2.playCharacter();
        } catch (GameException | EndGameException e) {
            System.out.println(e.getMessage());
            // Bruteforces char inputs until it finds something working
            boolean worked = false;
            for (int i = 0; i <= 2 * MatchType.MAX_PLAYERS && !worked; i = ((i != Color.values().length) ? i + 1 : 2 * MatchType.MAX_PLAYERS)) {
                for (int j = 0; j <= 2 * MatchType.MAX_PLAYERS && !worked; j = ((j != Color.values().length) ? j + 1 : 2 * MatchType.MAX_PLAYERS)) {
                    try {
                        controllerExpert2.setCharacterInputs(List.of(i));
                        controllerExpert2.playCharacter();
                        worked = true;
                    } catch (GameException | EndGameException e1) {
                        System.out.println(e1.getMessage());
                        try {
                            controllerExpert2.setCharacterInputs(Arrays.asList(i, j));
                            controllerExpert2.playCharacter();
                            worked = true;
                        } catch (GameException | EndGameException e2) {
                            System.out.println(e2.getMessage());
                        }
                    }
                }
            }
            if (!worked && controllerExpert2.isGameFinished()) fail(e);
        }
        assertThrows(GameException.class, controllerExpert2::playCharacter, "A card has already been played this turn");
    }

    @Test
    void removePlayerTest() {
        assertThrows(NullPointerException.class, () -> controllerExpert.removePlayer(null));
        assertDoesNotThrow(() -> controllerExpert.removePlayer(p3));
    }

    @Test
    void disconnectTest() {
        assertDoesNotThrow(() -> controllerExpert.disconnectPlayerQuit(p1));
    }

    @Test
    void endGameTest() {
        assertFalse(controllerExpert2.isGameFinished());
        for (int i = 0; i < 10; i++) {
            for (int j = i; j < i + 2; j++) {
                try {
                    controllerExpert2.playCard(assistantCardList.get(j % 10 + 1));
                } catch (GameException | EndGameException e) {
                    fail(e.getMessage());
                }
            }
            assertFalse(controllerExpert2.isGameFinished());
            for (int k = 0; k < 2; k++) {
                int moved = 0;
                for (Color c : Color.values()) {
                    for (int j = 0; j < 3 && moved != 3; j++) {
                        try {
                            controllerExpert2.move(c, 10);
                            moved++;
                        } catch (GameException | EndGameException ignored) {
                        }
                    }
                }
                assertFalse(controllerExpert2.isGameFinished());
                if (moved != 3) fail();
                try {
                    controllerExpert2.moveMotherNature(1);
                } catch (GameException | EndGameException e) {
                    fail(e.getMessage());
                }
                assertFalse(controllerExpert2.isGameFinished());
                try {
                    controllerExpert2.moveFromCloud(-1 - k % 2);
                } catch (GameException | EndGameException e) {
                    fail(e.getMessage());
                }
            }
        }
        assertTrue(controllerExpert2.isGameFinished());
    }

    @Test
    void isMyTurnTest() {
        ArrayList<ClientHandler> players = new ArrayList<>();
        players.add(p1);
        players.add(p2);
        players.add(p3);
        players.add(p4);
        for (int i = 0; i < 4; i++) {
            for (ClientHandler p : players) {
                if (i == players.indexOf(p))
                    assertTrue(controllerExpert.isMyTurn(p));
                else
                    assertFalse(controllerExpert.isMyTurn(p));
            }
            try {
                controllerExpert.playCard(assistantCardList.get(i + 1));
            } catch (GameException | EndGameException e) {
                fail(e);
            }
        }
    }

    @Test
    void getWinnersTest() {
        assertNull(controllerExpert2.getWinners());
        endGameTest();
        assertNotNull(controllerExpert2.getWinners());
    }
}
