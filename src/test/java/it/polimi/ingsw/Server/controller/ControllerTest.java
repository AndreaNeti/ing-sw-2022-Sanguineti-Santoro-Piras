package it.polimi.ingsw.Server.controller;

import it.polimi.ingsw.exceptions.GameException;
import it.polimi.ingsw.Enum.Color;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;


class ControllerTest {
    Controller controllerExpert, controllerExpert2, controllerNormal;
    ClientHandler p1, p2, p3, p4;

    public ControllerTest() {
        controllerExpert = new Controller(new MatchType((byte) 4, true), 69L);
        controllerExpert2 = new Controller(new MatchType((byte) 2, true), 69L);
        controllerNormal = new Controller(new MatchType((byte) 3, false), 69L);

        p1 = Mockito.mock(ClientHandler.class);
        p2 = Mockito.mock(ClientHandler.class);
        p3 = Mockito.mock(ClientHandler.class);
        p4 = Mockito.mock(ClientHandler.class);

        try {
            controllerExpert.addPlayer(p1, "Gigi");
            controllerExpert.addPlayer(p2, "Franco");
            controllerExpert.addPlayer(p3, "Carola");
            controllerExpert.addPlayer(p4, "Filomena");
        } catch (GameException e) {
            fail();
        }

        try {
            controllerNormal.addPlayer(p1, "Gigi");
            controllerNormal.addPlayer(p2, "Franco");
            controllerNormal.addPlayer(p3, "Carola");
        } catch (GameException e) {
            fail();
        }

        try {
            controllerExpert2.addPlayer(p1, "Gigi");
            controllerExpert2.addPlayer(p2, "Franco");
        } catch (GameException e) {
            fail();
        }
    }

    @Test
    void addPlayerTest() {
        ClientHandler p = Mockito.mock(ClientHandler.class);
        assertThrows(GameException.class, () -> controllerExpert.addPlayer(p, "Impostor"), "Match is full");
        assertThrows(GameException.class, () -> controllerNormal.addPlayer(p, "Impostor"), "Match is full");
    }

    @Test
    void playCardTest() {
        try {
            controllerExpert.playCard((byte) 1);
        } catch (GameException | NullPointerException e) {
            fail();
        }
        assertThrows(GameException.class, () -> controllerExpert.playCard((byte) 1), "Cannot play this card");
        assertDoesNotThrow(() -> controllerExpert.playCard((byte) 2));
        try {
            for (int i = 3; i < 5; i++)
                controllerExpert.playCard((byte) i);
        } catch (GameException | NullPointerException e) {
            fail();
        }
        assertThrows(GameException.class, () -> controllerExpert.playCard((byte) 6), "Not in planification phase");
    }

    @Test
    void moveTest() {
        assertThrows(GameException.class, () -> controllerExpert.move(Color.RED, 0), "Not in action phase");
        try {
            for (int i = 3; i < 7; i++)
                controllerExpert.playCard((byte) i);
        } catch (GameException | NullPointerException e) {
            fail();
        }
        assertThrows(GameException.class, () -> controllerExpert.move(Color.RED, 0), "Can't move to the selected GameComponent");
        assertThrows(GameException.class, () -> controllerExpert.move(Color.RED, -1), "Can't move to the selected GameComponent");
        int moved = 0;
        for (int i = 0; i < 3; i++) {
            for (Color c : Color.values()) {
                try {
                    controllerExpert.move(c, 1);
                    moved++;
                } catch (GameException ignored) {
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
                controllerExpert.playCard((byte) i);
        } catch (GameException | NullPointerException e) {
            fail();
        }
        assertThrows(GameException.class, () -> controllerExpert.moveMotherNature(1), "Wrong phase");
        int moved = 0;
        for (int i = 0; i < 3; i++) {
            for (Color c : Color.values()) {
                try {
                    controllerExpert.move(c, 1);
                    moved++;
                } catch (GameException ignored) {
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
                controllerExpert.playCard((byte) i);
        } catch (GameException | NullPointerException e) {
            fail();
        }
        assertThrows(GameException.class, () -> controllerExpert.moveFromCloud(0), "Wrong phase");
        int moved = 0;
        for (int i = 0; i < 3; i++) {
            for (Color c : Color.values()) {
                try {
                    controllerExpert.move(c, 1);
                    moved++;
                } catch (GameException ignored) {
                }
            }
        }
        if (moved != 3) fail();
        assertThrows(GameException.class, () -> controllerExpert.moveFromCloud(0), "Wrong phase");
        try {
            controllerExpert.moveMotherNature(1);
        } catch (GameException e) {
            fail();
        }
        assertThrows(GameException.class, () -> controllerExpert.moveFromCloud(1), "Component is not a cloud");
        assertThrows(GameException.class, () -> controllerExpert.moveFromCloud(-5), "Component is not a cloud");
        assertDoesNotThrow(() -> controllerExpert.moveFromCloud(-1));
        assertThrows(GameException.class, () -> controllerExpert.moveFromCloud(0), "Wrong phase");
    }

    @Test
    void sendMessageTest() {
        assertThrows(NullPointerException.class, () -> controllerExpert.sendMessage(null, "Ciao"));
        assertDoesNotThrow(() -> controllerExpert.sendMessage("Paolo", "Ciao"));
    }

    @Test
    void chooseCharacterTest() {
        assertThrows(GameException.class, () -> controllerExpert2.chooseCharacter((byte) 1), "Not in action phase");
        for (int i = 0; i < 8; i++) {
            for (int j = i + 1; j < i + 3; j++) {
                try {
                    controllerExpert2.playCard((byte) j);
                } catch (GameException e) {
                    fail(e.getMessage());
                }
            }
            for (int k = 0; k < 2; k++) {
                int moved = 0;
                for (Color c : Color.values()) {
                    for (int j = 0; j < 3 && moved != 3; j++) {
                        try {
                            //2k+1 is the lunch hall
                            controllerExpert2.move(c, 2*k+1);
                            moved++;
                        } catch (GameException ignored) {
                        }
                    }
                }
                if (moved != 3) {
                    for (Color c : Color.values()) {
                        for (int j = 0; j < 3 && moved != 3; j++) {
                            try {
                                controllerExpert2.move(c, 6);
                                moved++;
                            } catch (GameException ignored) {
                            }
                        }
                    }
                    if (moved != 3) fail();
                }

                try {
                    controllerExpert2.moveMotherNature((i + 1) / 2);
                } catch (GameException e) {
                    fail(e.getMessage());
                }
                try {
                    controllerExpert2.moveFromCloud(-1 - k % 2);
                } catch (GameException e) {
                    fail(e.getMessage());
                }
            }
        }
        try {
            controllerExpert2.playCard((byte) 9);
            controllerExpert2.playCard((byte) 10);
        } catch (GameException e) {
            fail(e.getMessage());
        }
        assertDoesNotThrow(() -> controllerExpert2.chooseCharacter((byte) 0));
        assertDoesNotThrow(() -> controllerExpert2.chooseCharacter((byte) 1));
        assertDoesNotThrow(() -> controllerExpert2.chooseCharacter((byte) 2));
    }

    @Test
    void setCharacterInputTest() {
        assertThrows(GameException.class, () -> controllerExpert2.setCharacterInput(0), "Not in action phase");
        chooseCharacterTest();
        assertDoesNotThrow(() -> controllerExpert2.setCharacterInput(0));
    }

    @Test
    void playCharacterTest() {
        assertThrows(GameException.class, () -> controllerExpert2.playCharacter(), "Not in action phase");
        chooseCharacterTest();
        try {
            controllerExpert2.playCharacter();
        } catch (GameException e) {
            // additional check if card selected is c0, c6 or c10 and it does not contain the selected color
            boolean worked = false;
            for (int i = 0; i < 5 && !worked; i++) {
                for (int j = 0; j < 5 && !worked; j++) {
                    try {
                        controllerExpert2.setCharacterInput(i);
                        controllerExpert2.playCharacter();
                        worked = true;
                    } catch (GameException e1) {
                        System.out.println(e1.getMessage());
                        try {
                            controllerExpert2.setCharacterInput(i);
                            controllerExpert2.setCharacterInput(j);
                            controllerExpert2.playCharacter();
                            worked = true;
                        } catch (GameException e2) {
                            System.out.println(e2.getMessage());
                        }
                    }
                }
            }
            if (!worked) fail();
        }
        assertThrows(GameException.class, () -> controllerExpert2.playCharacter(), "A card has already been played this turn");
    }

    @Test
    void removePlayerTest() {
        assertThrows(NullPointerException.class, () -> controllerExpert.removePlayer(null));
        assertDoesNotThrow(() -> controllerExpert.removePlayer(p3));
    }

    @Test
    void disconnectTest() {
        assertDoesNotThrow(() -> controllerExpert.disconnectPlayerQuitted(p1));
    }

    @Test
    void endGameTest() {
        assertFalse(controllerExpert2.isGameFinished());
        for (int i = 0; i < 10; i++) {
            for (int j = i; j < i + 2; j++) {
                try {
                    controllerExpert2.playCard((byte) (j % 10 + 1));
                } catch (GameException e) {
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
                        } catch (GameException ignored) {
                        }
                    }
                }
                assertFalse(controllerExpert2.isGameFinished());
                if (moved != 3) fail();
                try {
                    controllerExpert2.moveMotherNature(1);
                } catch (GameException e) {
                    fail(e.getMessage());
                }
                assertFalse(controllerExpert2.isGameFinished());
                try {
                    controllerExpert2.moveFromCloud(-1 - k % 2);
                } catch (GameException e) {
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
                controllerExpert.playCard((byte) (i + 1));
            } catch (GameException e) {
                fail();
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
