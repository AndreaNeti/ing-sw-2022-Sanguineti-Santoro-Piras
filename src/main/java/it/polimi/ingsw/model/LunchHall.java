package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.NotAllowedException;

//Lunch hall is that part of the boar that is called Dining Room. There is only one for player and it has a reference to it.
//It extends GameComponent which is the superclass of all the components that contains students.
public class LunchHall extends GameComponent{
    private final Player player;

    public LunchHall(Player player) {
        super();
        this.player = player;
    }

    @Override
    public void addStudents(Color color, byte number) throws NotAllowedException{
        if(howManyStudents(color) + number > 10)
            throw new NotAllowedException("Cannot add any more " + color + " students");
        super.addStudents(color, number);
    }

    //returns the player associated with the board
    public Player getPlayer() {
        return player;
    }
}
