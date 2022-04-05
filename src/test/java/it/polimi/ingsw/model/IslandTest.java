package it.polimi.ingsw.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class IslandTest {
    Island island = new Island();
    Island island1 = new Island();
    Team t = new Team(HouseColor.BLACK, (byte) 2, (byte) 8);

    @Test
    void getAndSetTeamTest() {
        assertNull(island.getTeam());
        island.setTeam(t);
        assertEquals(island.getTeam(), t);

    }

    /*
        @Test
        void mergeTest() {
            island.addStudents(Color.RED, (byte) 5);
            island.addStudents(Color.BLUE, (byte) 4);
            island.addStudents(Color.PINK, (byte) 3);

            island1.addStudents(Color.BLUE, (byte) 6);
            island1.addStudents(Color.YELLOW, (byte) 2);
            island1.addStudents(Color.GREEN, (byte) 5);
            island1.addStudents(Color.PINK, (byte) 3);

            island.merge(island1);
            for (Color c : Color.values())
                assertEquals(island1.howManyStudents(c), 0);

            assertEquals(island.howManyStudents(Color.RED), 5);
            assertEquals(island.howManyStudents(Color.BLUE), 10);
            assertEquals(island.howManyStudents(Color.YELLOW), 2);
            assertEquals(island.howManyStudents(Color.GREEN), 5);
            assertEquals(island.howManyStudents(Color.PINK), 6);


        }
    */
    @Test
    void getNumberTest() {
        assertEquals(island.getNumber(), 1);
        assertEquals(island1.getNumber(), 1);
        island.merge(island1);
        assertEquals(island.getNumber(), 2);
    }

   /* @Test
    void getStudentSizeTest() {
        island.addStudents(Color.GREEN, (byte) 4);
        assertEquals(island.howManyStudents(Color.GREEN), 4);
    }

    @Test
    void getAndSetProhibitionTest() {
        assertFalse(island.getProhibition());
        island.setProhibition(true);
        assertTrue(island.getProhibition());
    }
*/

}