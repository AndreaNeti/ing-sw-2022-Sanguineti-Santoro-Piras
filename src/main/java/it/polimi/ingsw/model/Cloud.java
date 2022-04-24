package it.polimi.ingsw.model;

public class Cloud extends GameComponent {

    public Cloud(int players) {
        super((players != 3) ? 3 : 4);
    }
}
