package it.polimi.ingsw;

import java.util.List;

public class Island implements GameComponent{
    private final int idIsland;
    private List<Piece> pieces;

    public Island(int id) {
        this.idIsland = id;
    }

    public int getIdIsland() {
        return idIsland;
    }

    @Override
    public void addPiece(Piece p) {
        pieces.add(p);
    }

    @Override
    public void removePiece(Piece p) {
        pieces.remove(p);
    }


}
