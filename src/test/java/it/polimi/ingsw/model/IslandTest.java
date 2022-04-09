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
    void getAndSetTeamTest() {

        assertNull(island.getTeam());
        island.setTeam(null);
        assertNull(island.getTeam());
        island.setTeam(t);
        assertEquals(island.getTeam(), t);

    }

    @Test
    void mergeTest() {

        assertEquals(island.getNumber(), 1);
        assertEquals(island1.getNumber(), 1);
        try {
            bag.drawStudent(island, (byte) 10);
            bag.drawStudent(island1, (byte) 10);
        } catch (GameException | EndGameException e) {
            fail();
        }
        assertThrows(UnexpectedValueException.class, () -> island.addProhibitions((byte) -1), "Cannot add negative prohibitions");
        try {
            island1.addProhibitions((byte) 2);
        } catch (UnexpectedValueException e) {
            fail();
        }
        assertEquals(island1.getProhibitions(), 2);
        island.merge(island1);
        assertEquals(island.getProhibitions(), 2);

        for (Color c : Color.values())
            assertEquals(0, island1.howManyStudents(c));

        assertEquals(island.howManyStudents(), 20);
        assertEquals(island.getNumber(), 2);
    }

    @Test
    void getAndSetProhibitionTest() {
        assertEquals(0, island.getProhibitions());
        try {
            island.addProhibitions((byte) 1);
        } catch (UnexpectedValueException e) {
            fail();
        }
        assertEquals(1, island.getProhibitions());
        island.removeProhibition();
        assertEquals(0, island.getProhibitions());
    }

}