package it.polimi.ingsw.server.model.GameComponents;

import it.polimi.ingsw.utils.Color;
import it.polimi.ingsw.exceptions.serverExceptions.EndGameException;
import it.polimi.ingsw.exceptions.serverExceptions.GameException;
import it.polimi.ingsw.exceptions.serverExceptions.NotAllowedException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EntranceHallTest {
    private final EntranceHall entranceHall = new EntranceHall(9, (byte) 0);
    private final Bag bag = new Bag((byte) 10);

    @Test
    public void entranceTest() {
        assertEquals(entranceHall.howManyStudents(), 0);
        assertEquals(entranceHall.getMaxStudents(), 9);
        try {
            bag.drawStudent(entranceHall, (byte) 9);
        } catch (EndGameException | GameException e) {
            fail(e);
        }
        for (Color c : Color.values()) {
            assertFalse(entranceHall.canAddStudents(c, (byte) 1));
        }
        assertThrows(NotAllowedException.class, () -> entranceHall.moveAll(bag), "Should launch cause you can't use this method");

    }

}