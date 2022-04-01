package it.polimi.ingsw.model;

import junit.framework.TestCase;
import org.junit.Test;

public class GameComponentTest extends TestCase {

    GameComponent gameComponent=new Island();
    GameComponent gameComponent1=new Bag();



    public void testAddStudents() {

    }

    public void testMoveStudents() {
    }
    @Test
    public void testGetStudents() {
        byte [] array;
        array=gameComponent.getStudents();
        int i;
        for(i=0;i<array.length;i++)
            assertEquals(array[i],0);
        assertEquals(i,5);
    }

}