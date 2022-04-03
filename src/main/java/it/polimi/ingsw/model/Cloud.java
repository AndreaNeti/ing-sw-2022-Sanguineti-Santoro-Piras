package it.polimi.ingsw.model;

public class Cloud extends GameComponent{
    //boolean that is true when there are 2-4 players, false otherwise
    private final boolean frontBack;

    public Cloud(int players) {
        super();
        frontBack = (players != 3);
    }

    public boolean getFrontBack() {
        return frontBack;
    }
}
