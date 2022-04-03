package it.polimi.ingsw.model;

import org.junit.jupiter.api.Test;

import java.net.Socket;

import static org.junit.jupiter.api.Assertions.*;

class LunchHallTest {
    Socket socket=new Socket();
    Team t=new Team(HouseColor.GREY,(byte)2,(byte) 3);
    Player p=new Player(socket, t,Wizard.AIRMAGE, "test");

    @Test
    void constructorAndGetTest() {
        LunchHall l=new LunchHall(p);
        assertEquals(p,l.getPlayer());
        l.addStudents(Color.GREEN,(byte)45);
        assertEquals(l.getStudentSize(Color.GREEN),45);

    }


}