package it.polimi.ingsw.model;

import junit.framework.TestCase;

public class GameComponentTest extends TestCase {

    GameComponent gameComponent = new Island();
    GameComponent gameComponent1 = new Cloud(3);
/*
    public void testAddStudents() {
        gameComponent.addStudents(Color.RED, (byte) 5);
        gameComponent.addStudents(Color.BLUE, (byte) 4);
        gameComponent.addStudents(Color.PINK, (byte) -3);
        assertEquals(gameComponent.howManyStudents(Color.RED), 5);
        assertEquals(gameComponent.howManyStudents(Color.BLUE), 4);
        assertEquals(gameComponent.howManyStudents(Color.YELLOW), 0);
        assertEquals(gameComponent.howManyStudents(Color.GREEN), 0);
        assertEquals(gameComponent.howManyStudents(Color.PINK), -3);
        gameComponent.addStudents(Color.BLUE, (byte) 2);
        assertEquals(gameComponent.howManyStudents(Color.RED), 5);
        assertEquals(gameComponent.howManyStudents(Color.BLUE), 6);
        assertEquals(gameComponent.howManyStudents(Color.YELLOW), 0);
        assertEquals(gameComponent.howManyStudents(Color.GREEN), 0);
        assertEquals(gameComponent.howManyStudents(Color.PINK), -3);
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
        } catch (GameException ex1) {
            fail();
        }
        assertEquals(gameComponent.howManyStudents(Color.RED), 5);
        assertEquals(gameComponent1.howManyStudents(Color.RED), 0);
        assertEquals(gameComponent.howManyStudents(Color.BLUE), 9);
        assertEquals(gameComponent1.howManyStudents(Color.BLUE), 1);
        assertEquals(gameComponent.howManyStudents(Color.YELLOW), 1);
        assertEquals(gameComponent1.howManyStudents(Color.YELLOW), 1);
        assertEquals(gameComponent.howManyStudents(Color.GREEN), 3);
        assertEquals(gameComponent1.howManyStudents(Color.GREEN), 2);
        assertEquals(gameComponent1.howManyStudents(Color.PINK), 1);
        assertEquals(gameComponent.howManyStudents(Color.PINK), 5);


    }

*/
}