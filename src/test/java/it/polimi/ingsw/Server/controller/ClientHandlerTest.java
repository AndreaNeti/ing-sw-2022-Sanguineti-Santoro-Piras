package it.polimi.ingsw.Server.controller;

import it.polimi.ingsw.exceptions.GameException;
import it.polimi.ingsw.exceptions.NotAllowedException;
import it.polimi.ingsw.network.toClientMessage.OK;
import it.polimi.ingsw.network.toClientMessage.ToClientMessage;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ClientHandlerTest {
    ClientHandler p1, p2, p3, p4;
    ObjectOutputStream out1, out2;
    ObjectInputStream in1, in2;

    PipedInputStream inputStream1, inputStream2;

    @BeforeEach
    public void initialize() {
        Socket client1 = mock(Socket.class);
        Socket client2 = mock(Socket.class);
        PipedOutputStream outputStream1 = new PipedOutputStream();
        PipedOutputStream outputStream2 = new PipedOutputStream();
        PipedInputStream a = new PipedInputStream();
        PipedOutputStream b = new PipedOutputStream();
        PipedInputStream c = new PipedInputStream();
        PipedOutputStream d = new PipedOutputStream();

        try {
            a.connect(outputStream1);
            inputStream1 = new PipedInputStream(b);
            out1 = new ObjectOutputStream(outputStream1);
            c.connect(outputStream2);
            inputStream2 = new PipedInputStream(d);
            out2 = new ObjectOutputStream(outputStream2);
            when(client1.getOutputStream()).thenReturn(b);
            when(client1.getInputStream()).thenReturn(a);
            when(client2.getOutputStream()).thenReturn(d);
            when(client2.getInputStream()).thenReturn(c);

        } catch (IOException e) {
            fail();
        }

        p1 = new ClientHandler(client1);
        p2 = new ClientHandler(client2);
        try {
            in1 = new ObjectInputStream(inputStream1);
            in2 = new ObjectInputStream(inputStream2);
        } catch (IOException e) {
            fail();
        }
        try {
            p1.setNickName("Paolino");
            p2.setNickName("Franco");
        } catch (GameException e) {
            fail();
        }
    }

    @AfterEach
    public void quit() {
        if(p1.getController() != null)
            p1.quit();
        if(p2.getController() != null)
            p2.quit();
        Server.removeNickName("Paolino");
        Server.removeNickName("Franco");
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
        try {
            in1.readObject();
            in2.readObject();
            in2.readObject();
            in2.readObject();
            in2.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        assertDoesNotThrow(() -> p1.joinByMatchId(p2.getController().getMatchId()));
        assertThrows(NotAllowedException.class, () -> p1.joinByMatchId(1L), "Already joined a match");
    }

    @Test
    void joinMatchByTypeTest() {
        MatchType type = new MatchType((byte) 3, false);
        try {
            p2.createMatch(type);
        } catch (GameException e) {
            fail();
        }
        try {
            in1.readObject();
            in2.readObject();
            in2.readObject();
            in2.readObject();
            in2.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        assertDoesNotThrow(() -> p1.joinByMatchType(type));
        assertThrows(NotAllowedException.class, () -> p1.joinByMatchType(type), "Already joined a match");
    }

    @Test
    void createMatchTest() {
        MatchType type = new MatchType((byte) 3, false);
        assertDoesNotThrow(() -> p1.createMatch(type));
        assertThrows(NotAllowedException.class, () -> p1.createMatch(type), "Already joined a match");
//        p1.quit();
    }
}