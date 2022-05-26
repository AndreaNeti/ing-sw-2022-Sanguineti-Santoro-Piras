package it.polimi.ingsw.Server.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CloudTest {


    @Test
    void cloudTest() {
        Cloud cloud = new Cloud(3, (byte) -1);
        assertEquals(cloud.getMaxStudents(), 3);
        Cloud cloud1 = new Cloud(4, (byte) -2);
        assertEquals(cloud1.getMaxStudents(), 4);
        Cloud cloud2 = new Cloud(3, (byte) -3);
        assertEquals(cloud2.getMaxStudents(), 3);
        assertEquals(cloud2.getId(), -3);

    }
}
