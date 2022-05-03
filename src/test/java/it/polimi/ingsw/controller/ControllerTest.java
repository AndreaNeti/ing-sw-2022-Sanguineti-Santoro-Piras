package it.polimi.ingsw.controller;

import it.polimi.ingsw.exceptions.GameException;
import it.polimi.ingsw.model.Color;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;


class ControllerTest {
    Controller controllerExpert, controllerExpert2, controllerNormal;
    PlayerHandler p1, p2, p3, p4;

    public ControllerTest() {
        controllerExpert = new Controller(new MatchType((byte) 4, true));
        controllerExpert2 = new Controller(new MatchType((byte) 2, true));
        controllerNormal = new Controller(new MatchType((byte) 3, false));

        p1 = Mockito.mock(PlayerHandler.class);
        p2 = Mockito.mock(PlayerHandler.class);
        p3 = Mockito.mock(PlayerHandler.class);
        p4 = Mockito.mock(PlayerHandler.class);

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
        PlayerHandler p = Mockito.mock(PlayerHandler.class);
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
                            controllerExpert2.move(c, 1);
                            moved++;
                        } catch (GameException ignored) {
                        }
                    }
                }
                if (moved != 3) fail();
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
                for (int j = 0; j < 5; j++) {
                    try {
                        controllerExpert2.setCharacterInput(i);
                        controllerExpert2.playCharacter();
                        worked = true;
                    } catch (GameException e1) {
                        try {
                            controllerExpert2.setCharacterInput(i);
                            controllerExpert2.setCharacterInput(j);
                            controllerExpert2.playCharacter();
                            worked = true;
                        } catch (GameException ignored) {
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
        assertDoesNotThrow(() -> controllerExpert.disconnectEveryone(p1));
    }

//    @Test
//    void endGameTest() {
//        chooseCharacterTest();
//        try {
//            controllerExpert2.playCard((byte) 9);
//            controllerExpert2.playCard((byte) 10);
//        } catch (GameException e) {
//            fail(e.getMessage());
//        }
//            for (int k = 0; k < 2; k++) {
//                int moved = 0;
//                for (Color c : Color.values()) {
//                    for (int j = 0; j < 3 && moved != 3; j++) {
//                        try {
//                            controllerExpert2.move(c, 1);
//                            moved++;
//                        } catch (GameException ignored) {
//                        }
//                    }
//                }
//                if (moved != 3) fail();
//                try {
//                    controllerExpert2.moveMotherNature((i + 1) / 2);
//                } catch (GameException e) {
//                    fail(e.getMessage());
//                }
//                try {
//                    controllerExpert2.moveFromCloud(-1 - k % 2);
//                } catch (GameException e) {
//                    fail(e.getMessage());
//                }
//            }
//        }
//    }
}