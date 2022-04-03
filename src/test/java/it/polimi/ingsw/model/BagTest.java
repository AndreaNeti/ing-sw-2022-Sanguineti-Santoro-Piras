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
        assertEquals(bag.getStudentSize(), 10);
    }

    GameComponent island = new Island();

    @Test
    void drawStudentAndRefilledTest() {
        try {
            bag.drawStudent(island, (byte) 3);

        } catch (UnexpectedValueException | EndGameException ex) {
            fail();
        }
        int sum = 0;
        for (byte color : island.getStudents()) {
            sum += color;
        }
        assertEquals(sum,3);


        assertThrows(UnexpectedValueException.class, ()-> bag.drawStudent(island,(byte)-4),"" +
                "it's negative value, should launch exception");
        try{
            bag.drawStudent(island,(byte) 7);
        }catch (UnexpectedValueException| EndGameException ex){
            fail();
        }

        sum = 0;
        for (byte color :island.getStudents()) {
            sum += color;
        }
        assertEquals(sum,10);
        assertEquals(bag.getStudentSize(),(byte) 24*5);

        assertThrows(EndGameException.class,()->bag.drawStudent(island,(byte) 121),"" +
                "Not enough student in the bag should launche exception");

    }
}