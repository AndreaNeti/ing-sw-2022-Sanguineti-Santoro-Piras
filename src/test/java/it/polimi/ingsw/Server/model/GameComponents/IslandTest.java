package it.polimi.ingsw.Server.model.GameComponents;

import it.polimi.ingsw.Server.model.Team;
import it.polimi.ingsw.Util.Color;
import it.polimi.ingsw.Util.HouseColor;
import it.polimi.ingsw.exceptions.serverExceptions.EndGameException;
import it.polimi.ingsw.exceptions.serverExceptions.GameException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class IslandTest {
    Island island = new Island((byte) 2);
    Island island1 = new Island((byte) 3);
    Team t = new Team(HouseColor.BLACK, (byte) 2, (byte) 8);
    Bag bag = new Bag((byte) 20);


    @Test
    void constructorAndTeamTest() {
        assertEquals(island.getProhibitions(), 0);
        assertEquals(island.getArchipelagoSize(), 1);
        assertTrue(island.canAddStudents(Color.GREEN, (byte) 135));
        assertTrue(island.canAddStudents(Color.RED, (byte) 57));
        assertTrue(island.canAddStudents(Color.YELLOW, (byte) 1));
        assertTrue(island.canAddStudents(Color.PINK, (byte) 125));
        assertTrue(island.canAddStudents(Color.BLUE, (byte) 0));
        assertEquals(island.howManyStudents(), 0);
        assertEquals(island.getMaxStudents(), Integer.MAX_VALUE);
        assertNull(island.getTeamColor());
        assertThrows(IllegalArgumentException.class, () -> island.setTeamColor(null));
        island.setTeamColor(t.getHouseColor());
        assertEquals(island.getTeamColor(), t.getHouseColor());
    }


    @Test
    void mergeAndProhibitionTest() {

        try {
            bag.drawStudent(island, (byte) 10);
            bag.drawStudent(island1, (byte) 10);
        } catch (GameException | EndGameException e) {
            fail(e);
        }
        assertThrows(IllegalArgumentException.class, () -> island.addProhibitions((byte) -1), "Cannot add negative prohibitions");

        island1.addProhibitions((byte) 2);
        island.addProhibitions((byte) 1);
        assertEquals(island1.getProhibitions(), 2);

        assertThrows(IllegalArgumentException.class, () -> island.merge(null));

        island.merge(island1);
        assertEquals(island.getProhibitions(), 3);

        for (Color c : Color.values())
            assertEquals(0, island1.howManyStudents(c));

        assertEquals(island.howManyStudents(), 20);
        assertEquals(island.getArchipelagoSize(), 2);
        island.removeProhibition();
        assertEquals(island.getProhibitions(), 2);
    }


}