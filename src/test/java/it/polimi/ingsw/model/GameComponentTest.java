package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.NotEnoughStudentsException;
import junit.framework.TestCase;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class GameComponentTest extends TestCase {

    GameComponent gameComponent = new Island();
    GameComponent gameComponent1 = new Cloud(3);

    @Test
    public void testGetStudents() {
        byte[] array;
        array = gameComponent.getStudents();
        int i;
        for (i = 0; i < array.length; i++)
            assertEquals(array[i], 0);
        assertEquals(i, 5);

    }

    @Test
    public void testAddStudents() {
        byte[] array;
        gameComponent.addStudents(Color.RED, (byte) 5);
        gameComponent.addStudents(Color.BLUE, (byte) 4);
        gameComponent.addStudents(Color.PINK, (byte) -3);
        array = gameComponent.getStudents();
        assertEquals(array[0], 5);
        assertEquals(array[1], 4);
        assertEquals(array[2], 0);
        assertEquals(array[3], 0);
        assertEquals(array[4], -3);
        gameComponent.addStudents(Color.BLUE, (byte) 2);
        array = gameComponent.getStudents();
        assertEquals(array[0], 5);
        assertEquals(array[1], 6);
        assertEquals(array[2], 0);
        assertEquals(array[3], 0);
        assertEquals(array[4], -3);
    }

    public void testMoveStudents() {
        gameComponent.addStudents(Color.RED, (byte) 5);
        gameComponent.addStudents(Color.BLUE, (byte) 4);
        gameComponent.addStudents(Color.PINK, (byte) 3);

        gameComponent1.addStudents(Color.BLUE, (byte) 6);
        gameComponent1.addStudents(Color.YELLOW, (byte) 2);
        gameComponent1.addStudents(Color.GREEN, (byte) 5);
        gameComponent1.addStudents(Color.PINK, (byte) 3);

        NotEnoughStudentsException ex = assertThrows(NotEnoughStudentsException.class, () ->
                        gameComponent.moveStudents(Color.BLUE, (byte) 10, gameComponent1),
                "Should throw exception but it didn't");

        try {
            gameComponent1.moveStudents(Color.RED, (byte) 0, gameComponent);
            gameComponent1.moveStudents(Color.BLUE, (byte) 5, gameComponent);
            gameComponent1.moveStudents(Color.YELLOW, (byte) 1, gameComponent);
            gameComponent1.moveStudents(Color.GREEN, (byte) 3, gameComponent);
            gameComponent1.moveStudents(Color.PINK, (byte) 2, gameComponent);
        } catch (NotEnoughStudentsException ex1) {
            fail();
        }
        assertEquals(gameComponent.getStudents()[0], 5);
        assertEquals(gameComponent1.getStudents()[0], 0);
        assertEquals(gameComponent.getStudents()[1], 9);
        assertEquals(gameComponent1.getStudents()[1], 1);
        assertEquals(gameComponent.getStudents()[2], 1);
        assertEquals(gameComponent1.getStudents()[2], 1);
        assertEquals(gameComponent.getStudents()[3], 3);
        assertEquals(gameComponent1.getStudents()[3], 2);
        assertEquals(gameComponent1.getStudents()[4], 1);
        assertEquals(gameComponent.getStudents()[4], 5);


    }


}