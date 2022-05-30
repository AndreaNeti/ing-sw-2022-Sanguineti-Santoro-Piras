package it.polimi.ingsw.Server.model.GameComponents;

import it.polimi.ingsw.Enum.Color;
import it.polimi.ingsw.exceptions.serverExceptions.EndGameException;
import it.polimi.ingsw.exceptions.serverExceptions.GameException;
import it.polimi.ingsw.exceptions.serverExceptions.NotAllowedException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BagTest {
    byte nStudentPerColor = 24;
    Bag bag = new Bag(nStudentPerColor);

    @Test
    void constructorTest() {

        assertEquals(bag.howManyStudents(), nStudentPerColor * Color.values().length);
        for (Color c : Color.values()) {
            assertFalse(bag.canAddStudents(c,(byte) 1));
            assertTrue(bag.canAddStudents(c,(byte) 0));
            assertEquals(bag.howManyStudents(c),nStudentPerColor);
        }
        assertEquals(bag.getMaxStudents(),nStudentPerColor * Color.values().length);
        assertEquals(bag.getId(),69);

    }

    GameComponent island = new Island((byte) 2);
    GameComponent cloud = new Cloud(2, (byte) -1);
    @Test
    void drawStudentAndRefilledTest() {
        try {
            bag.drawStudent(island, (byte) 3);

        } catch (GameException | EndGameException ex) {
            fail();
        }
        assertEquals(island.howManyStudents(), 3);


        assertThrows(IllegalArgumentException.class, () -> bag.drawStudent(island, (byte) -4), "it's negative value, should launch exception");
        //another extraction to the same island
        try {
            bag.drawStudent(island, (byte) 7);
        } catch (GameException | EndGameException ex) {
            fail();
        }

        assertEquals(island.howManyStudents(), 10);
        assertEquals(bag.howManyStudents(), nStudentPerColor * Color.values().length - island.howManyStudents());

        assertThrows(NotAllowedException.class, () -> bag.drawStudent(cloud, (byte) 5), "Can't add 5 students to cloud");

        assertThrows(EndGameException.class, () -> bag.drawStudent(island, (byte) (bag.howManyStudents() + 1)), "Not enough student in the bag should launch the exception");
        assertThrows(IllegalArgumentException.class, () -> bag.moveAll(island), "Can't move pieces from the Bag");

    }
}