package it.polimi.ingsw.Server.controller;

import it.polimi.ingsw.Util.MatchType;
import it.polimi.ingsw.exceptions.serverExceptions.GameException;
import it.polimi.ingsw.exceptions.serverExceptions.NotAllowedException;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ClientHandlerTest {
    ClientHandler p1, p2, p3, p4;
    ObjectOutputStream out1, out2;

    @BeforeEach
    public void initialize() {
        Socket client1 = mock(Socket.class);
        Socket client2 = mock(Socket.class);
        PipedOutputStream outputStream1 = new PipedOutputStream();
        PipedOutputStream outputStream2 = new PipedOutputStream();
        PipedInputStream a = new PipedInputStream();
        PipedOutputStream b = mock(PipedOutputStream.class);
        PipedInputStream c = new PipedInputStream();
        PipedOutputStream d = mock(PipedOutputStream.class);
        PipedInputStream inputStream1 = mock(PipedInputStream.class);
        PipedInputStream inputStream2 = mock(PipedInputStream.class);

        try {
            a.connect(outputStream1);
            inputStream1.connect(b);
            out1 = new ObjectOutputStream(outputStream1);
            c.connect(outputStream2);
            inputStream2.connect(d);
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
            p1.setNickName("Paolino");
            p2.setNickName("Franco");
        } catch (GameException e) {
            fail();
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
}