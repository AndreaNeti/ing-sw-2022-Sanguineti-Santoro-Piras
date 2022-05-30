package it.polimi.ingsw.Server.model.GameComponents;

import it.polimi.ingsw.Enum.Color;
import it.polimi.ingsw.Server.model.GameComponents.*;
import it.polimi.ingsw.exceptions.serverExceptions.EndGameException;
import it.polimi.ingsw.exceptions.serverExceptions.GameException;
import it.polimi.ingsw.exceptions.serverExceptions.NotAllowedException;
import it.polimi.ingsw.exceptions.serverExceptions.NotEnoughStudentsException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class GameComponentTest {
    Bag bag = new Bag((byte) 2);
    GameComponent island = new Island((byte) 3);
    GameComponent cloud = new Cloud(4, (byte)-3);
    GameComponent island1 = new Island((byte ) 4);
    GameComponent island2 = new Island((byte) 6);
    @Test
    public void testMoveStudents() {
        try {
            bag.drawStudent(island, (byte) 9);
        } catch (EndGameException | GameException e) {
            fail();
        }
        assertEquals(island.howManyStudents(), 9);
        for (Color c : Color.values())
            assertTrue(island.howManyStudents(c) > 0);
        try {
            island.moveStudents(Color.RED, (byte) 1, cloud);
            island.moveStudents(Color.BLUE, (byte) 1, cloud);
            island.moveStudents(Color.PINK, (byte) 1, cloud);
            island.moveStudents(Color.GREEN, (byte) 1, cloud);
            assertEquals(cloud.howManyStudents(), 4);
            assertEquals(island.howManyStudents(), 5);
        } catch (GameException e) {
            fail();
        }
        assertThrows(GameException.class, () -> island.moveStudents(Color.YELLOW, (byte) 1, cloud), "Cloud can't have more than 4 students");
        try {
            cloud.moveAll(island);
            island.moveAll(bag);
        } catch (NotAllowedException exception) {
            fail();
        }
        assertEquals(island.howManyStudents(), 0);
        assertEquals(bag.howManyStudents(), 2 * Color.values().length);
    }

    @Test
    public void swapTest() {
        try {
            bag = new Bag((byte) 2);
            // at least one per color
            bag.drawStudent(island1, (byte) (bag.howManyStudents() - 1));
            bag = new Bag((byte) 2);
            bag.drawStudent(island2, (byte) (bag.howManyStudents() - 1));
        } catch (EndGameException | GameException e) {
            fail();
        }
        byte oldBlue1 = island1.howManyStudents(Color.BLUE);
        byte oldBlue2 = island2.howManyStudents(Color.BLUE);
        byte oldRed1 = island1.howManyStudents(Color.RED);
        byte oldRed2 = island2.howManyStudents(Color.RED);
        try {
            island1.swapStudents(Color.BLUE, Color.RED, island2);
        } catch (GameException e) {
            fail();
        }
        assertEquals(island1.howManyStudents(Color.BLUE), oldBlue1 - 1);
        assertEquals(island1.howManyStudents(Color.RED), oldRed1 + 1);
        assertEquals(island2.howManyStudents(Color.BLUE), oldBlue2 + 1);
        assertEquals(island2.howManyStudents(Color.RED), oldRed2 - 1);

        // swap same student color
        oldBlue1 = island1.howManyStudents(Color.BLUE);
        oldBlue2 = island2.howManyStudents(Color.BLUE);
        try {
            island1.swapStudents(Color.PINK, Color.PINK, island2);
        } catch (GameException e) {
            fail();
        }
        assertEquals(island1.howManyStudents(Color.BLUE), oldBlue1);
        assertEquals(island2.howManyStudents(Color.BLUE), oldBlue2);

        // island is empty, can't swap
        assertThrows(NotEnoughStudentsException.class, () -> island.swapStudents(Color.PINK, Color.BLUE, island1));
        assertThrows(NotEnoughStudentsException.class, () -> island1.swapStudents(Color.BLUE, Color.PINK, island));
        bag = new Bag((byte) 11);
        LunchHall lh = new LunchHall(50, (byte) 10);
        try {
            bag.drawStudent(lh, (byte) 50);
        } catch (EndGameException | GameException e) {
            fail(e);
        }
        assertThrows(NotAllowedException.class, () -> island1.swapStudents(Color.GREEN, Color.PINK, lh));
        try {
            // should launch exception, but it's the same gameComponent -> doesn't do anything
            island.swapStudents(Color.BLUE, Color.BLUE, island);
        } catch (GameException e) {
            fail(e);
        }
    }

    @Test
    public void moveAllTest() {

        try {
            bag.drawStudent(island, (byte) 3);
            bag.drawStudent(island1, (byte) 1);
        } catch (GameException | EndGameException ex) {
            fail();
        }
        try {
            island.moveAll(island1);
        } catch (NotAllowedException ex) {
            fail();
        }
        assertEquals(island.howManyStudents(), 0);
        assertEquals(island1.howManyStudents(), 4);

        //I need to see if the other island has 0 students it
        // prints an out line
        try {
            island.moveAll(island2);
        } catch (NotAllowedException ex) {
            fail();
        }
        try {
            bag.drawStudent(island, (byte) 5);
        } catch (EndGameException | GameException ex) {
            fail();
        }

        assertThrows(NotAllowedException.class, () -> island.moveAll(cloud), "Cloud is full should call exception");

    }
}