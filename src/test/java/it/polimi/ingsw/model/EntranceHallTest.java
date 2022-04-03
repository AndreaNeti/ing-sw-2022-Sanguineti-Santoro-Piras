package it.polimi.ingsw.model;

import org.junit.jupiter.api.Test;

import java.net.Socket;

import static org.junit.jupiter.api.Assertions.*;

class EntranceHallTest {
    Socket socket=new Socket();
    Team t=new Team(HouseColor.GREY,(byte)2,(byte) 3);
    Player p=new Player(socket, t,Wizard.AIRMAGE, "test");
    @Test
    void constructAndGetTest() {
        EntranceHall e=new EntranceHall(p);
        assertEquals(e.getPlayer(),p);

    }
}