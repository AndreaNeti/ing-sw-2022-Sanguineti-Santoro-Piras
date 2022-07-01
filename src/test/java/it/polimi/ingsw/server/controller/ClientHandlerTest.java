package it.polimi.ingsw.server.controller;

import it.polimi.ingsw.utils.MatchType;
import it.polimi.ingsw.exceptions.serverExceptions.GameException;
import it.polimi.ingsw.exceptions.serverExceptions.NotAllowedException;
import it.polimi.ingsw.network.PingMessage;
import it.polimi.ingsw.network.toClientMessage.OK;
import it.polimi.ingsw.network.toClientMessage.ToClientMessage;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.net.Socket;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ClientHandlerTest {
    ClientHandler p1, p2;
    ObjectOutputStream out1, out2;

    Socket client1, client2;

    @BeforeEach
    public void initialize() {
        client1 = mock(Socket.class);
        client2 = mock(Socket.class);
        PipedOutputStream outputStream1 = new PipedOutputStream();
        PipedOutputStream outputStream2 = new PipedOutputStream();
        PipedInputStream inputStream1 = new PipedInputStream();
        PipedInputStream inputStream2 = new PipedInputStream();
        PipedOutputStream mockOutputStream1 = mock(PipedOutputStream.class);
        PipedOutputStream mockOutputStream2 = mock(PipedOutputStream.class);
        PipedInputStream mockInputStream1 = mock(PipedInputStream.class);
        PipedInputStream mockInputStream2 = mock(PipedInputStream.class);

        // each client handler's output stream is a mock output stream connected to a mock input stream
        // each client handler's input stream is connected to an output stream that doesn't write anything in order to avoid saturating the
        // input stream buffer
        try {
            inputStream1.connect(outputStream1);
            mockInputStream1.connect(mockOutputStream1);
            out1 = new ObjectOutputStream(outputStream1);
            inputStream2.connect(outputStream2);
            mockInputStream2.connect(mockOutputStream2);
            out2 = new ObjectOutputStream(outputStream2);
            when(client1.getOutputStream()).thenReturn(mockOutputStream1);
            when(client1.getInputStream()).thenReturn(inputStream1);
            when(client2.getOutputStream()).thenReturn(mockOutputStream2);
            when(client2.getInputStream()).thenReturn(inputStream2);

        } catch (IOException e) {
            fail(e);
        }

        try {
            p1 = new ClientHandler(client1);
            p2 = new ClientHandler(client2);
        } catch (IOException e) {
            fail(e);
        }
        try {
            p1.setNickName("Paolino");
            p2.setNickName("Franco");
        } catch (GameException e) {
            fail(e);
        }
    }

    @AfterEach
    public void quit() {
        if (p1.getController() != null)
            p1.quit();
        if (p2.getController() != null)
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
        try {
            out1.writeObject(new PingMessage());
        } catch (IOException e) {
            fail(e);
        }
        p1.quit();
        p1.run();
    }

    @Test
    void joinMatchByIdTest() {

        MatchType type = new MatchType((byte) 3, false);
        try {
            p2.createMatch(type);
        } catch (GameException e) {
            fail(e);
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
            fail(e);
        }
        assertDoesNotThrow(() -> p1.joinByMatchType(type));
        assertThrows(NotAllowedException.class, () -> p1.joinByMatchType(type), "Already joined a match");
    }

    @Test
    void createMatchTest() {
        System.out.println("Ciao");
        MatchType type = new MatchType((byte) 3, false);
        try {
            p2.createMatch(type);
        } catch (GameException e) {
            fail(e);
        }
        assertThrows(NotAllowedException.class, () -> p2.createMatch(type), "Already joined a match");
    }

    @Test
    void hashTest() {
        assertEquals(Objects.hash(client1), p1.hashCode());
    }

    @Test
    void pingTest() {
        assertDoesNotThrow(() -> p1.sendPingPong());
    }

}