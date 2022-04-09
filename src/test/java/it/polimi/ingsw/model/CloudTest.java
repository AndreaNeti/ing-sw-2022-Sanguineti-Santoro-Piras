package it.polimi.ingsw.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
class CloudTest {


    @Test
    void cloudTest(){
        Cloud cloud=new Cloud(4);
        assertTrue(cloud.getFrontBack());
        Cloud cloud1=new Cloud(3);
        assertFalse(cloud1.getFrontBack());
        Cloud cloud2=new Cloud(2);
        assertTrue(cloud2.getFrontBack());
    }
}
