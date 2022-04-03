package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.EndGameException;
import it.polimi.ingsw.exceptions.UnexpectedValueException;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class BagTest {
    Bag bag = new Bag();

    @Test
    void constructorAndGetTest() {
        assertEquals(bag.howManyStudents(), 10);
    }

    GameComponent island = new Island();

    @Test
    void drawStudentAndRefilledTest() {
        try {
            bag.drawStudent(island, (byte) 3);

        } catch (UnexpectedValueException | EndGameException ex) {
            fail();
        }
        assertEquals(island.howManyStudents(),3);


        assertThrows(UnexpectedValueException.class, ()-> bag.drawStudent(island,(byte)-4),"" +
                "it's negative value, should launch exception");
        try{
            bag.drawStudent(island,(byte) 7);
        }catch (UnexpectedValueException| EndGameException ex){
            fail();
        }

        assertEquals(island.howManyStudents(),10);
        assertEquals(bag.howManyStudents(),(byte) 24*5);

        assertThrows(EndGameException.class,()->bag.drawStudent(island,(byte) 121),"" +
                "Not enough student in the bag should launche exception");

    }
}