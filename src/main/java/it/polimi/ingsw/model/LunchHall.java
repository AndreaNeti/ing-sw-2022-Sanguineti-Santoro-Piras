package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.GameException;
import it.polimi.ingsw.exceptions.NotAllowedException;

//Lunch hall is that part of the boar that is called Dining Room. There is only one for player and it has a reference to it.
//It extends GameComponent which is the superclass of all the components that contains students.
public class LunchHall extends GameComponent {

    public LunchHall(int lunchHallSize) {
        super(lunchHallSize);
    }

    @Override
    public void moveStudents(Color color, byte number, GameComponent destination) throws GameException {
        boolean check = true;
        for (Color c : Color.values()) {
            if (howManyStudents(c) >= 10) {
                check = false;
                break; // :D
            }
        }
        if (check) super.moveStudents(color, number, destination);
        else
            throw new NotAllowedException("Lunch hall can't have more than 10 " + color.name().toLowerCase() + " students");
    }
}
