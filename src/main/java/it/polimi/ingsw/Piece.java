package it.polimi.ingsw;

public class Piece {
    private final Color color;
    private GameComponent component;

    public Piece(Color color){
        this.color = color;
    }

    public Color getColor(){
        return this.color;
    }

    public void movePiece(GameComponent gc){
    }
}
