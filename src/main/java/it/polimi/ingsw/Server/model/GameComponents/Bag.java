package it.polimi.ingsw.Server.model.GameComponents;

import it.polimi.ingsw.Enum.Color;
import it.polimi.ingsw.exceptions.serverExceptions.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Bag extends GameComponent {

    private final Random rand;

    public Bag(byte studentsPerColor) {
        super(studentsPerColor, (byte) 69);
        rand = new Random(System.currentTimeMillis());
    }

    public void drawStudent(GameComponent gameComponent, byte number) throws EndGameException, GameException {
        if (gameComponent == null) throw new IllegalArgumentException("Cannot draw students to null gameComponent");
        if (number < 0) throw new IllegalArgumentException("Cannot draw negative students");
        else if (number > gameComponent.getMaxStudents() - gameComponent.howManyStudents())
            throw new NotAllowedException("Can't draw " + number + " students to this gameComponent");
        byte i = 0;
        int studentsToDraw = Math.min(number, howManyStudents());
        if (studentsToDraw > 0) {
            List<Color> availableColors = new ArrayList<>(Arrays.asList(Color.values()));
            while (i < studentsToDraw) {
                Color color = availableColors.get(rand.nextInt(availableColors.size()));
                try {
                    moveStudents(color, (byte) 1, gameComponent);
                    i++;
                } catch (NotEnoughStudentsException | NotAllowedException ex) {
                    // for some reason can't more students of this color but for sure there is other free space, not extracting that color anymore
                    availableColors.remove(color);
                }

            }
            // if this becomes true it means that the bag is now empty
            if (howManyStudents() == 0)
                throw new EndGameException(false);
        }

    }

    @Override
    public void moveAll(GameComponent destination) {
        throw new IllegalArgumentException("It's a bag, cannot do move All");
    }
}

