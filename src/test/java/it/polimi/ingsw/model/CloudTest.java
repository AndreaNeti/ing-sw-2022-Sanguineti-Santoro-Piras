package it.polimi.ingsw.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CloudTest {


    @Test
    void cloudTest() {
        Cloud cloud = new Cloud(4);
        assertEquals(cloud.getMaxStudents(), 3);
        Cloud cloud1 = new Cloud(3);
        assertEquals(cloud1.getMaxStudents(), 4);
        Cloud cloud2 = new Cloud(2);
        assertEquals(cloud2.getMaxStudents(), 3);

    }
}
