package it.polimi.ingsw.server.model.GameComponents;

import it.polimi.ingsw.exceptions.serverExceptions.EndGameException;
import it.polimi.ingsw.exceptions.serverExceptions.GameException;
import it.polimi.ingsw.exceptions.serverExceptions.NotAllowedException;
import it.polimi.ingsw.exceptions.serverExceptions.NotEnoughStudentsException;
import it.polimi.ingsw.utils.Color;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Bag class represents the game's bag, from which new students are drawn
 */
public class Bag extends GameComponent {

    private final Random rand;

    /**
     * Constructor Bag creates a new instance of Bag.
     *
     * @param studentsPerColor of type {@link byte} - number of students for each color available on the bag.
     */
    public Bag(byte studentsPerColor) {
        super(studentsPerColor, (byte) 69);
        rand = new Random(System.currentTimeMillis());
    }

    /**
     * Method drawStudent moves a selected amount of students to a target component. Each student color is random.
     *
     * @param gameComponent of type {@link GameComponent} - instance of the target component.
     * @param number of type {@code byte} - number of students to draw.
     * @throws EndGameException when there are no more students left on the bag.
     * @throws GameException if the target component cannot receive all the students to draw.
     */
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
                    // for some reason can't move students of this color but for sure there is other free space, not extracting that color anymore
                    availableColors.remove(color);
                }

            }
            // if this becomes true it means that the bag is now empty
            if (howManyStudents() == 0)
                throw new EndGameException(false);
        }

    }

    /**
     * Method moveAll not available for Bag.
     *
     * @param destination of type {@link GameComponent} - the instance of the target component.
     * @throws NotAllowedException when this method is called on a Bag.
     */
    @Override
    public void moveAll(GameComponent destination) throws NotAllowedException {
        throw new NotAllowedException("You can't moveAll from the bag");
    }

    /**
     * Method canAddStudents checks if the bag can receive enough students of a selected color.
     *
     * @param color  of type {@link Color} - color of the students.
     * @param number of type {@code byte} - number of students.
     * @return {@code boolean} - true if the bag can receive the specified number of students of the selected color, false else.
     */
    @Override
    protected boolean canAddStudents(Color color, byte number) {
        return howManyStudents(color) + number <= (getMaxStudents()/Color.values().length);
    }
}

