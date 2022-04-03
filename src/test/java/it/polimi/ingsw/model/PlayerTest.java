package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.EndGameException;
import it.polimi.ingsw.exceptions.NotAllowedException;
import it.polimi.ingsw.exceptions.UnexpectedValueException;
import it.polimi.ingsw.exceptions.UsedCardException;
import org.junit.Before;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.net.Socket;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {
    Socket socket= new Socket();
    Team t=new Team(HouseColor.WHITE,(byte)1,(byte) 2);
    Team t1=new Team(HouseColor.BLACK,(byte)1,(byte) 2);
    Player p=new Player(socket,t,Wizard.AIRMAGE,"prova");

    @Test
    void constructorAndEqualsTest(){
        Player p1=new Player(socket,t,Wizard.AIRMAGE,"prova");
        assertEquals(p,p1);
        assertEquals(p.getPlayedCard(),0);
        assertEquals(p.getWizard(),Wizard.AIRMAGE);
        assertEquals(p.getPlayedCardMoves(),0);
    }

    @Test
    void cardTest() {
        try{
            p.useCard((byte)4);
        }catch (UsedCardException|UnexpectedValueException|NotAllowedException| EndGameException ex){
            fail();
        }
        assertEquals(p.getPlayedCard(),4);
        assertEquals(p.getPlayedCardMoves(),2);



        assertThrows(UnexpectedValueException.class,()->p.useCard((byte) -5),"negative value");
        assertThrows(UsedCardException.class,()->p.useCard((byte) 4),"negative value");
        try{
            p.useCard((byte) 1);
            assertEquals(p.getPlayedCard(),1);
            p.useCard((byte) 2);
            assertEquals(p.getPlayedCard(),1);
            p.useCard((byte) 3);
            assertEquals(p.getPlayedCard(),2);
            p.useCard((byte) 5);
            assertEquals(p.getPlayedCard(),3);
            p.useCard((byte) 6);
            assertEquals(p.getPlayedCard(),3);
            p.useCard((byte) 7);
            assertEquals(p.getPlayedCard(),4);
            p.useCard((byte) 8);
            assertEquals(p.getPlayedCard(),4);
            p.useCard((byte) 9);
            assertEquals(p.getPlayedCard(),5);
        }catch(UsedCardException|UnexpectedValueException|NotAllowedException| EndGameException ex){
            fail();
        }
        assertThrows(UsedCardException.class,()->p.useCard((byte) 4),"negative value");
        assertThrows(EndGameException.class,()->p.useCard((byte) 9),"no more card");
        assertThrows(NotAllowedException.class,()->p.useCard((byte) 3),"can't call this");
    }

    @Test
    void getPlayedCardMoves() {
    }

    @Test
    void getPlayedCard() {
    }

    @Test
    void getLunchHall() {
    }

    @Test
    void getEntranceHall() {
    }

    @Test
    void getWizard() {
    }

    @Test
    void getNickName() {
    }

    @Test
    void getSocket() {
    }

    @Test
    void getTeam() {
    }

    @Test
    void compare() {
    }
}