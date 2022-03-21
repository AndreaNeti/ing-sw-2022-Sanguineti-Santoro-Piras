package it.polimi.ingsw;

public class Island extends GameComponent{
    private Player player;
    private boolean prohibition;
    private byte number;

    public Player getPlayer() {
        return player;
    }

    public void merge(Island island) {

    }

    public byte getNumber() {
        return number;
    }
}
