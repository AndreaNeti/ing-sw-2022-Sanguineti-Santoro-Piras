package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.EndGameException;
import it.polimi.ingsw.exceptions.GameException;
import it.polimi.ingsw.exceptions.NotAllowedException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EntranceHallTest {
    EntranceHall entranceHall =  new EntranceHall(9,(byte)0);
    Bag bag=new Bag((byte) 10, (byte) 69);
    @Test
    public void entranceTest() {
        assertEquals(entranceHall.howManyStudents(), 0);
        assertEquals(entranceHall.getMaxStudents(), 9);
        try{
            bag.drawStudent(entranceHall,(byte) 9);
        }catch (EndGameException| GameException ex){
            fail();
        }
        for (Color c : Color.values()) {
            assertFalse(entranceHall.canAddStudents(c, (byte) 1));
        }
        assertThrows(NotAllowedException.class,()->entranceHall.moveAll(bag),"Should launch cause you can't use this method");

    }

}