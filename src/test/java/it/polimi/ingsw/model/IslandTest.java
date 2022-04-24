package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.EndGameException;
import it.polimi.ingsw.exceptions.GameException;
import it.polimi.ingsw.exceptions.UnexpectedValueException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class IslandTest {
    Island island = new Island();
    Island island1 = new Island();
    Team t = new Team(HouseColor.BLACK, (byte) 2, (byte) 8);
    Bag bag = new Bag((byte) 20);


    @Test
    void constructorAndTeamTest() {
        assertEquals(island.getProhibitions(), 0);
        assertEquals(island.getNumber(), 1);
        assertTrue(island.canAddStudents(Color.GREEN, (byte) 135));
        assertTrue(island.canAddStudents(Color.RED, (byte) 57));
        assertTrue(island.canAddStudents(Color.YELLOW, (byte) 1));
        assertTrue(island.canAddStudents(Color.PINK, (byte) 125));
        assertTrue(island.canAddStudents(Color.BLUE, (byte) 0));
        assertEquals(island.howManyStudents(), 0);
        assertEquals(island.getMaxStudents(), Integer.MAX_VALUE);
        assertNull(island.getTeamColor());
        island.setTeamColor(null);
        assertNull(island.getTeamColor());
        island.setTeamColor(t.getHouseColor());
        assertEquals(island.getTeamColor(), t.getHouseColor());
    }


    @Test
    void mergeAndProhibitionTest() {

        try {
            bag.drawStudent(island, (byte) 10);
            bag.drawStudent(island1, (byte) 10);
        } catch (GameException | EndGameException e) {
            fail();
        }
        assertThrows(UnexpectedValueException.class, () -> island.addProhibitions((byte) -1), "Cannot add negative prohibitions");
        try {
            island1.addProhibitions((byte) 2);
            island.addProhibitions((byte) 1);
        } catch (UnexpectedValueException e) {
            fail();
        }
        assertEquals(island1.getProhibitions(), 2);
        //this should print an err in system line
        island.merge(null);

        island.merge(island1);
        assertEquals(island.getProhibitions(), 3);

        for (Color c : Color.values())
            assertEquals(0, island1.howManyStudents(c));

        assertEquals(island.howManyStudents(), 20);
        assertEquals(island.getNumber(), 2);
        island.removeProhibition();
        assertEquals(island.getProhibitions(), 2);
    }


}