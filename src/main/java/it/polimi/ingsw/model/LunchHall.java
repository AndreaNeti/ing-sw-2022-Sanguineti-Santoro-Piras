package it.polimi.ingsw.model;

//Lunch hall is that part of the boar that is called Dining Room. There is only one for player and it has a reference to it.
//It extends GameComponent which is the superclass of all the components that contains students.
public class LunchHall extends GameComponent {

    public LunchHall(int lunchHallSize) {
        super(lunchHallSize);
    }

    @Override
    public boolean canAddStudents(Color c, byte number) {
        return howManyStudents(c) + number <= 10 && super.canAddStudents(c, number);
    }
}
