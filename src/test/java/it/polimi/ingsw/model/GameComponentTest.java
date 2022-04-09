package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.EndGameException;
import it.polimi.ingsw.exceptions.GameException;
import junit.framework.TestCase;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class GameComponentTest extends TestCase {
    Bag bag = new Bag((byte) 2);
    GameComponent island = new Island();
    GameComponent cloud = new Cloud(3);

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
        island.moveAll(bag);
        assertEquals(island.howManyStudents(), 0);
        assertEquals(bag.howManyStudents(), 2 * Color.values().length);
    }
}