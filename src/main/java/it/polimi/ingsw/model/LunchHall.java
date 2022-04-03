package it.polimi.ingsw.model;
//Lunch hall is that part of the boar that is called Dining Room. There is only one for player and it has a reference to it.
//It extends GameComponent which is the superclass of all the components that contains students.
public class LunchHall extends GameComponent{
    private final Player player;

    public LunchHall(Player player) {
        super();
        this.player = player;
    }

    /*@Override
    TODO Delete this add Student and implement the coins in expertGame
    public void addStudents(Color color, byte number) {
        super.addStudents(color, number);
         byte coins = (byte) ((number + getStudents()[color]%3) / 3
    }*/

    //returns the player associated with the board
    public Player getPlayer() {
        return player;
    }
}
