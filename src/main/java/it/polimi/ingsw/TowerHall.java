package it.polimi.ingsw;

public class TowerHall extends Hall{
    private final Color color;

    public TowerHall(Color c){
        this.color = c;
    }
    @Override
    public void addPiece(Piece piece) {

    }

    @Override
    public void removePiece(Piece piece) {

    }

    public Color getColor(){
        return this.color;
    }
}
