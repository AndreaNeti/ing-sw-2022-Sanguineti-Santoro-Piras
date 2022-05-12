package it.polimi.ingsw.Server.model;

import it.polimi.ingsw.Enum.Color;
import it.polimi.ingsw.exceptions.EndGameException;
import it.polimi.ingsw.exceptions.GameException;
import it.polimi.ingsw.exceptions.NotAllowedException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LunchHallTest {
    Bag bag = new Bag((byte) 20);

    @Test
    void lunchHallTest() {
        LunchHall lunchHall = new LunchHall(10 * Color.values().length, (byte) 1);
        GameComponent gc = new Island((byte) 4);
        try {
            bag.drawStudent(gc, (byte) (20 * Color.values().length - 1));
        } catch (EndGameException | GameException e) {
            fail();
        }
        assertEquals(0, lunchHall.howManyStudents());
        assertThrows(NotAllowedException.class, () -> gc.moveStudents(Color.BLUE, (byte) 11, lunchHall), "Cannot exceed 10 students per color");
        assertEquals(0, lunchHall.howManyStudents());
        try {
            gc.moveStudents(Color.BLUE, (byte) 10, lunchHall);
        } catch (GameException e) {
            fail();
        }
        assertEquals(10, lunchHall.howManyStudents());
        assertEquals(10, lunchHall.howManyStudents(Color.BLUE));
        assertThrows(IllegalArgumentException.class, () -> lunchHall.moveAll(bag), "Should launch cause you can't use this method");
    }
}