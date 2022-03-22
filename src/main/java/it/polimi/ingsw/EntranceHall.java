package it.polimi.ingsw;

public class EntranceHall extends GameComponent{
    private final Player player;

    public EntranceHall(Player player) {
        super(0);
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }
}
