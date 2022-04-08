package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.EndGameException;
import it.polimi.ingsw.exceptions.GameException;
import it.polimi.ingsw.exceptions.UnexpectedValueException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BagTest {
    byte nStudentPerColor = 24;
    Bag bag = new Bag(nStudentPerColor);

    @Test
    void constructorAndGetTest() {
        assertEquals(bag.howManyStudents(), nStudentPerColor * Color.values().length);
    }

    GameComponent island = new Island();
    GameComponent cloud = new Cloud(2);

    @Test
    void drawStudentAndRefilledTest() {
        try {
            bag.drawStudent(island, (byte) 3);

        } catch (GameException | EndGameException ex) {
            fail();
        }
        assertEquals(island.howManyStudents(), 3);


        assertThrows(UnexpectedValueException.class, () -> bag.drawStudent(island, (byte) -4), "it's negative value, should launch exception");
        try {
            bag.drawStudent(island, (byte) 7);
        } catch (GameException | EndGameException ex) {
            fail();
        }

        assertEquals(island.howManyStudents(), 10);
        assertEquals(bag.howManyStudents(), nStudentPerColor * Color.values().length - island.howManyStudents());

        assertThrows(GameException.class, () -> bag.drawStudent(cloud, (byte) 5), "Can't add 5 students to cloud");
        assertThrows(EndGameException.class, () -> bag.drawStudent(island, (byte) (bag.howManyStudents() + 1)), "Not enough student in the bag should launche exception");
    }
}