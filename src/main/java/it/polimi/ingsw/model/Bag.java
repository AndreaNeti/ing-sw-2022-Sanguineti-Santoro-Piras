package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.*;

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

    public void drawStudent(GameComponent gameComponent, byte number) throws EndGameException, GameException {
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
                }

            }
            // if this becomes true it means that the bag is now empty
            if (howManyStudents() == 0)
                throw new EndGameException(false);
        }

    }
    @Override
    public void moveAll(GameComponent destination) throws NotAllowedException {
        throw new NotAllowedException("It's a bag i can't moveMyStudents");
    }
}

