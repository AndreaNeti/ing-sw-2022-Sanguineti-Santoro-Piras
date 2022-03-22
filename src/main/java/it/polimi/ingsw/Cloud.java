package it.polimi.ingsw;

import java.util.List;

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
