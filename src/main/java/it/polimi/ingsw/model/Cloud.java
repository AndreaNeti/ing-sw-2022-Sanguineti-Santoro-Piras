package it.polimi.ingsw.model;

public class Cloud extends GameComponent{
    private final boolean frontBack;

    public Cloud(int players) {
        super();
        frontBack = players != 3;
    }

    public boolean isFrontBack() {
        return frontBack;
    }
}
