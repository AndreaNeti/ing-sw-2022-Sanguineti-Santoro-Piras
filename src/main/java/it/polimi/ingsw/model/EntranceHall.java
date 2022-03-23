package it.polimi.ingsw.model;

public class EntranceHall extends GameComponent{
    private final Player player;

    public EntranceHall(Player player) {
        super();
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }
}
