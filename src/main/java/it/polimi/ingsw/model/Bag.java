package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Bag extends GameComponent {

    private final Random rand;
    private boolean refilled;

    public Bag() {
        super();

        for (Color c : Color.values()) {
            try {
                this.addStudents(c, (byte) 2);
            } catch (NotAllowedException ex) {
                ex.printStackTrace();
                // should not call this
            }
        }
        refilled = false;

        rand = new Random(System.currentTimeMillis());
    }

    public void drawStudent(GameComponent gameComponent, byte number) throws EndGameException, UnexpectedValueException {
        if (number < 0) throw new UnexpectedValueException();
        byte i = 0;
        int studentsToDraw = Math.min(number, howManyStudents());
        if (studentsToDraw > 0) {
            List<Color> availableColors = new ArrayList<>(Arrays.asList(Color.values()));
            while (i < studentsToDraw) {
                Color color = availableColors.get(rand.nextInt(availableColors.size()));
                try {
                    moveStudents(color, (byte) 1, gameComponent);
                    i++;
                } catch (NotEnoughStudentsException ex) {
                    // not extracting that color anymore
                    availableColors.remove(color);
                } catch (NotAllowedException ex) {
                    ex.printStackTrace();
                    // should not call this
                }

            }
        }
        // if this becomes true it means that the bag is now empty
        if (howManyStudents() == 0) {
            if (refilled)
                throw new EndGameException(false);
            else {
                // refill the bag after island initialization
                refilled = true;
                for (Color c : Color.values()) {
                    try {
                        this.addStudents(c, (byte) 24);
                    } catch (NotAllowedException ex) {
                        ex.printStackTrace();
                        // should not call this
                    }
                }
            }
        }
    }
}

