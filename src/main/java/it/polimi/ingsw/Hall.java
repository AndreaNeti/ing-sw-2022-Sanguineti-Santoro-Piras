package it.polimi.ingsw;

public abstract class Hall implements GameComponent{
    private DashBoard dashboard;

    abstract public void addPiece(Piece piece);
    abstract public void removePiece(Piece piece);

}
