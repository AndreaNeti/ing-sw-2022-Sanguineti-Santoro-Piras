package it.polimi.ingsw;

public class Island extends GameComponent{
    private Player player;
    private boolean prohibition;
    private byte number;

    public Island() {
        super(0);
    }

    public Player getPlayer() {
        return player;
    }
    public void merge(Island island){}

    public byte getNumber() {
        return number;
    }

    public boolean isProhibition() {
        return prohibition;
    }
}
