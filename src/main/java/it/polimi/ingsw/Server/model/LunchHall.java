package it.polimi.ingsw.Server.model;

import it.polimi.ingsw.Enum.Color;

//Lunch hall is that part of the boar that is called Dining Room. There is only one for player and it has a reference to it.
//It extends GameComponent which is the superclass of all the components that contains students.
public class LunchHall extends GameComponent {

    public LunchHall(int lunchHallSize, byte idGameComponent) {
        super(lunchHallSize, idGameComponent);
    }

    @Override
    public boolean canAddStudents(Color c, byte number) {
        if (c == null) throw new IllegalArgumentException("Null color");
        return howManyStudents(c) + number <= 10 && super.canAddStudents(c, number);
    }

    @Override
    public void moveAll(GameComponent destination) {
        throw new IllegalArgumentException("You can't do moveAll from the lunchHall");
    }
}
