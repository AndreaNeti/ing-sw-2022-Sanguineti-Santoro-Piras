package it.polimi.ingsw.Server.controller;

import it.polimi.ingsw.exceptions.GameException;
import it.polimi.ingsw.exceptions.NotAllowedException;
import it.polimi.ingsw.network.toClientMessage.OK;
import it.polimi.ingsw.network.toClientMessage.ToClientMessage;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ClientHandlerTest {
    static ClientHandler p1, p2, p3, p4;

    //    ServerSocket server = new ServerSocket();
    static ServerSocket server = mock(ServerSocket.class);
    static Socket client1 = mock(Socket.class);
    static Socket client2 = mock(Socket.class);

    @BeforeAll
    static void initialize() {
        try {
            PipedOutputStream outputStream1 = new PipedOutputStream();
            PipedOutputStream outputStream2 = new PipedOutputStream();
            when(client1.getOutputStream()).thenReturn(outputStream1);
            when(client2.getOutputStream()).thenReturn(outputStream2);
            PipedInputStream inputStream1 = new PipedInputStream(outputStream1);
            PipedInputStream inputStream2 = new PipedInputStream(outputStream2);
            when(client1.getInputStream()).thenReturn(inputStream1);
            when(client2.getInputStream()).thenReturn(inputStream2);

        } catch (IOException e) {
            fail();
        }

        p1 = new ClientHandler(client1);
        p2 = new ClientHandler(client2);

        try {
            p1.setNickName("Paolino");
            p2.setNickName("Franco");
        } catch (GameException e) {
            fail();
        }
    }

    @Test
    void constructorTest() {
        assertThrows(NullPointerException.class, () -> new ClientHandler(null));
    }

    @Test
    void NicknameTest() {

        assertEquals(p1.getNickName(), "Paolino");
        assertThrows(NotAllowedException.class, () -> p1.setNickName("Impostor"), "Nickname already set");
    }

    @Test
    void updateTest() {
        ToClientMessage m = new OK();
        assertDoesNotThrow(() -> p1.update(m));
    }

    @Test
    void runTest() {
    }

    @Test
    void joinMatchByIdTest() {
        MatchType type = new MatchType((byte) 3, false);
        try {
            p2.createMatch(type);
        } catch (GameException e) {
            fail();
        }
        assertDoesNotThrow(() -> p1.joinByMatchId(p2.getController().getMatchId()));
        assertThrows(NotAllowedException.class, () -> p1.joinByMatchId(1L), "Already joined a match");
        p1.quit();
        p2.quit();
    }

    @Test
    void joinMatchByTypeTest() {
        MatchType type = new MatchType((byte) 3, false);
        try {
            p2.createMatch(type);
        } catch (GameException e) {
            fail();
        }
        assertDoesNotThrow(() -> p1.joinByMatchType(type));
        assertThrows(NotAllowedException.class, () -> p1.joinByMatchType(type), "Already joined a match");
        p1.quit();
        p2.quit();
    }

    @Test
    void createMatchTest() {
        MatchType type = new MatchType((byte) 3, false);
        assertDoesNotThrow(() -> p1.createMatch(type));
        assertThrows(NotAllowedException.class, () -> p1.createMatch(type), "Already joined a match");
        p1.quit();
    }
}