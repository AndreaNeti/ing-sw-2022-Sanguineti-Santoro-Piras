package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.EndGameException;
import it.polimi.ingsw.exceptions.GameException;
import it.polimi.ingsw.exceptions.NotAllowedException;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LunchHallTest {
    Bag bag = new Bag((byte) 20);

    @Test
    void constructorAndGetTest() {
        LunchHall l = new LunchHall(10 * Color.values().length);
        GameComponent gc = new Island();
        try {
            bag.drawStudent(gc, (byte) (20 * Color.values().length - 1));
        } catch (EndGameException | GameException e) {
            fail();
        }
        assertEquals(0, l.howManyStudents());
        assertThrows(NotAllowedException.class, () -> gc.moveStudents(Color.BLUE, (byte) 11, l), "Cannot exceed 10 students per color");
        assertEquals(0, l.howManyStudents());
        try {
            gc.moveStudents(Color.BLUE, (byte) 10, l);
        } catch (GameException e) {
            fail();
        }
        assertEquals(10, l.howManyStudents());
        assertEquals(10, l.howManyStudents(Color.BLUE));
    }
}