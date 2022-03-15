package it.polimi.ingsw;

public abstract class Hall implements GameComponent{
    private Dashboard dashboard;

    abstract public void addPiece(Piece piece);
    abstract public void removePiece(Piece piece);

}
