package it.polimi.ingsw;

public class ExpertPlayer {
    private byte coins;

    public void addCoins(byte coins){
        this.coins+=coins;
    }

    public void removeCoins(byte coins){
        this.coins-=coins;

    }
    public byte getCoins() {
        return coins;
    }
}
