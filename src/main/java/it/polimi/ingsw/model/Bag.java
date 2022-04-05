package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.EndGameException;
import it.polimi.ingsw.exceptions.GameException;
import it.polimi.ingsw.exceptions.NotEnoughStudentsException;
import it.polimi.ingsw.exceptions.UnexpectedValueException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Bag extends GameComponent {

    private final Random rand;

    public Bag(byte studentsPerColor) {
        super(studentsPerColor);
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
                } catch (GameException e) {
                    e.printStackTrace();
                }

            }
            // if this becomes true it means that the bag is now empty
            if (howManyStudents() == 0)
                throw new EndGameException(false);
        }
    }
}

