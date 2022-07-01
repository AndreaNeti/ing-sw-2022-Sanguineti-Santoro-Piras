package it.polimi.ingsw.server.model.GameComponents;

import it.polimi.ingsw.utils.Color;
import it.polimi.ingsw.exceptions.serverExceptions.EndGameException;
import it.polimi.ingsw.exceptions.serverExceptions.GameException;
import it.polimi.ingsw.exceptions.serverExceptions.NotAllowedException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LunchHallTest {
    private final Bag bag = new Bag((byte) 20);

    @Test
    void lunchHallTest() {
        LunchHall lunchHall = new LunchHall(10 * Color.values().length, (byte) 1);
        GameComponent gc = new Island((byte) 4);
        try {
            bag.drawStudent(gc, (byte) (20 * Color.values().length - 1));
        } catch (EndGameException | GameException e) {
            fail(e);
        }
        assertEquals(0, lunchHall.howManyStudents());
        assertThrows(NotAllowedException.class, () -> gc.moveStudents(Color.BLUE, (byte) 11, lunchHall), "Cannot exceed 10 students per color");
        assertEquals(0, lunchHall.howManyStudents());
        try {
            gc.moveStudents(Color.BLUE, (byte) 10, lunchHall);
        } catch (GameException e) {
            fail(e);
        }
        assertEquals(10, lunchHall.howManyStudents());
        assertEquals(10, lunchHall.howManyStudents(Color.BLUE));
        assertThrows(NotAllowedException.class, () -> lunchHall.moveAll(bag), "Should launch cause you can't use this method");
    }
}