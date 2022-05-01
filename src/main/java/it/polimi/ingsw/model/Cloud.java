package it.polimi.ingsw.model;

public class Cloud extends GameComponent {
    public Cloud(int players, byte idGameComponent) {
        super(((players != 3) ? 3 : 4), idGameComponent);
    }
}
