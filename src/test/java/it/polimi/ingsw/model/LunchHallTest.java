package it.polimi.ingsw.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LunchHallTest {

    @Test
    void constructorAndGetTest() {
        LunchHall l = new LunchHall(10);
        l.addStudents(Color.GREEN, (byte) 45);
        assertEquals(l.howManyStudents(Color.GREEN), 45);

    }

}